package com.mishkun.yandextestexercise.domain.interactors;

import com.mishkun.yandextestexercise.di.modules.DomainModule;
import com.mishkun.yandextestexercise.domain.entities.Definition;
import com.mishkun.yandextestexercise.domain.entities.ShortTranslationModel;
import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.domain.entities.Translation;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
import com.mishkun.yandextestexercise.domain.entities.TranslationQuery;
import com.mishkun.yandextestexercise.domain.providers.DictionarySupportedLanguagesProvider;
import com.mishkun.yandextestexercise.domain.providers.ExpandedTranslationProvider;
import com.mishkun.yandextestexercise.domain.providers.ShortTranslationProvider;
import com.mishkun.yandextestexercise.domain.providers.TranslationDirectionGuessProvider;
import com.mishkun.yandextestexercise.domain.providers.TranslationDirectionProvider;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Mishkun on 12.04.2017.
 */

public class TranslationInteractor extends Interactor<Translation, TranslationQuery> {

    private final String oneWordRegex = "\\S+";
    private ShortTranslationProvider shortTranslationProvider;
    private ExpandedTranslationProvider expandedTranslationProvider;
    private TranslationDirectionProvider translationDirectionProvider;
    private TranslationDirectionGuessProvider translationDirectionGuessProvider;
    private DictionarySupportedLanguagesProvider dictionarySupportedLanguagesProvider;

    @Inject
    TranslationInteractor(@Named(DomainModule.JOB) Scheduler threadExecutor, @Named(DomainModule.UI) Scheduler postExecutionThread,
                          ShortTranslationProvider shortTranslationProvider, ExpandedTranslationProvider expandedTranslationProvider,
                          TranslationDirectionProvider translationDirectionProvider,
                          TranslationDirectionGuessProvider translationDirectionGuessProvider,
                          DictionarySupportedLanguagesProvider dictionarySupportedLanguagesProvider) {
        super(threadExecutor, postExecutionThread);
        this.shortTranslationProvider = shortTranslationProvider;
        this.expandedTranslationProvider = expandedTranslationProvider;
        this.translationDirectionProvider = translationDirectionProvider;
        this.translationDirectionGuessProvider = translationDirectionGuessProvider;
        this.dictionarySupportedLanguagesProvider = dictionarySupportedLanguagesProvider;
    }

    @Override
    Observable<Translation> buildUseCaseObservable(final TranslationQuery params) {
        if (params.shouldGuess()) {
            // Yandex Translate can't into empty strings
            return translationDirectionGuessProvider.guessLanguage(params.getString().length() > 0 ? params.getString() : " ")
                                                    .map(new Function<Language, TranslationDirection>() {
                                                        @Override
                                                        public TranslationDirection apply(Language language) throws Exception {
                                                            if (!language.getCode().equals("")) {
                                                                if (language.equals(params.getDirection().getTranslationTo())) {
                                                                    return new TranslationDirection(language, params.getDirection().getTranslationFrom());
                                                                } else {
                                                                    return new TranslationDirection(language,
                                                                                                    params.getDirection().getTranslationTo());
                                                                }
                                                            } else {
                                                                return params.getDirection();
                                                            }
                                                        }
                                                    })
                                                    .doOnNext(new Consumer<TranslationDirection>() {
                                                        @Override
                                                        public void accept(TranslationDirection direction) throws Exception {
                                                            translationDirectionProvider.setTranslationDirection(direction);
                                                        }
                                                    })
                                                    .concatMap(
                                                            new Function<TranslationDirection, ObservableSource<? extends Translation>>() {
                                                                @Override
                                                                public ObservableSource<Translation> apply(
                                                                        TranslationDirection direction) throws Exception {
                                                                    return getTranslation(new TranslationQuery(params.getString(), direction,
                                                                                                               params.shouldGuess()));
                                                                }
                                                            });
        } else {
            translationDirectionProvider.setTranslationDirection(params.getDirection());
            return getTranslation(params);
        }
    }

    private Observable<Translation> getTranslation(TranslationQuery params) {
        final TranslationQuery query = params.normalize();
        if (query.getString().matches(oneWordRegex)) {
            return Observable.zip(shortTranslationProvider.getShortTranslation(query.getString(), query.getDirection()),
                                  dictionarySupportedLanguagesProvider.getSupportedLanguages().concatMap(
                                          new Function<List<TranslationDirection>, ObservableSource<Definition>>() {
                                              @Override
                                              public Observable<Definition> apply(List<TranslationDirection> directions) throws Exception {
                                                  for (TranslationDirection supportedDirection : directions) {
                                                      if (supportedDirection.equals(query.getDirection())) {
                                                          return expandedTranslationProvider.getExpandedTranslation(query.getString(),
                                                                                                                    query.getDirection());
                                                      }
                                                  }
                                                  return Observable.just(new Definition(null, null, null));
                                              }
                                          }),
                                  new BiFunction<ShortTranslationModel, Definition, Translation>() {
                                      @Override
                                      public Translation apply(ShortTranslationModel shortTranslation, Definition expandedTranslation) throws Exception {
                                          Translation translation = new Translation(shortTranslation.getTranslation(), expandedTranslation,
                                                                                    query.getString(), query.getDirection());
                                          translation.setFavored(shortTranslation.isFavored());
                                          return translation;
                                      }
                                  });
        } else {
            return shortTranslationProvider
                    .getShortTranslation(query.getString(), query.getDirection())
                    .map(new Function<ShortTranslationModel, Translation>() {
                        @Override
                        public Translation apply(ShortTranslationModel shortTranslation) throws Exception {
                            Translation translation = new Translation(shortTranslation.getTranslation(), new Definition(null, null, null),
                                                                      query.getString(), query.getDirection());
                            translation.setFavored(shortTranslation.isFavored());
                            return translation;
                        }
                    });
        }
    }

}
