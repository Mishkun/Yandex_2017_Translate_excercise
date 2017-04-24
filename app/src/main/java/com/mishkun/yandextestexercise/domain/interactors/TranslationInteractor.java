package com.mishkun.yandextestexercise.domain.interactors;

import android.util.Log;

import com.mishkun.yandextestexercise.di.modules.DomainModule;
import com.mishkun.yandextestexercise.domain.entities.Definition;
import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.domain.entities.ShortTranslationModel;
import com.mishkun.yandextestexercise.domain.entities.Translation;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
import com.mishkun.yandextestexercise.domain.entities.TranslationQuery;
import com.mishkun.yandextestexercise.domain.providers.DictionarySupportedLanguagesProvider;
import com.mishkun.yandextestexercise.domain.providers.ExpandedTranslationProvider;
import com.mishkun.yandextestexercise.domain.providers.ShortTranslationProvider;
import com.mishkun.yandextestexercise.domain.providers.TranslationDirectionGuessProvider;
import com.mishkun.yandextestexercise.domain.providers.TranslationDirectionProvider;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Main Interactor of the app. Collects data from all sorts of sources and gives it back in form of Translation
 */
public class TranslationInteractor extends Interactor<Translation, TranslationQuery> {
    private static final String TAG = TranslationInteractor.class.getSimpleName();

    private ShortTranslationProvider shortTranslationProvider;
    private ExpandedTranslationProvider expandedTranslationProvider;
    private TranslationDirectionProvider translationDirectionProvider;
    private TranslationDirectionGuessProvider translationDirectionGuessProvider;

    @Inject
    TranslationInteractor(@Named(DomainModule.JOB) Scheduler threadExecutor, @Named(DomainModule.UI) Scheduler postExecutionThread,
                          ShortTranslationProvider shortTranslationProvider, ExpandedTranslationProvider expandedTranslationProvider,
                          TranslationDirectionProvider translationDirectionProvider,
                          TranslationDirectionGuessProvider translationDirectionGuessProvider) {
        super(threadExecutor, postExecutionThread);
        this.shortTranslationProvider = shortTranslationProvider;
        this.expandedTranslationProvider = expandedTranslationProvider;
        this.translationDirectionProvider = translationDirectionProvider;
        this.translationDirectionGuessProvider = translationDirectionGuessProvider;
    }

    @Override
    Observable<Translation> buildUseCaseObservable(final TranslationQuery params) {
                if (params.shouldGuess()) {
            // Yandex Translate can't into empty strings
            return translationDirectionGuessProvider.guessLanguage(params.getString())
                                                    .map(new Function<Language, TranslationDirection>() {
                                                        @Override
                                                        public TranslationDirection apply(Language language) throws Exception {
                                                            Log.d(TAG, "getdirection");
                                                            if (!language.getCode().equals("")) {
                                                                if (language.equals(params.getDirection().getTranslationTo())) {
                                                                    return new TranslationDirection(language,
                                                                                                    params.getDirection().getTranslationFrom());
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
                                                            // propagate the received direction
                                                            translationDirectionProvider.setTranslationDirection(direction);
                                                        }
                                                    })
                                                    .switchMap(new Function<TranslationDirection, ObservableSource<? extends Translation>>() {
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
        Log.d(TAG, "getTranslation: ");
        final TranslationQuery query = params.normalize();
        return Observable.zip(shortTranslationProvider.getShortTranslation(query.getString(), query.getDirection()),
                              expandedTranslationProvider.getExpandedTranslation(query.getString(), query.getDirection()), new TranslationMapper());
    }

    private static class TranslationMapper implements BiFunction<ShortTranslationModel, Definition, Translation> {
        @Override
        public Translation apply(ShortTranslationModel shortTranslation,
                                 Definition expandedTranslation) throws Exception {
            Translation translation = new Translation(shortTranslation.getTranslation(), expandedTranslation,
                                                      shortTranslation.getOriginal(), shortTranslation.getDirection());
            translation.setFavored(shortTranslation.isFavored());
            return translation;
        }
    }
}

