package com.mishkun.yandextestexercise.domain.interactors;

import com.mishkun.yandextestexercise.di.modules.DomainModule;
import com.mishkun.yandextestexercise.domain.entities.Translation;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
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
 * Created by Mishkun on 12.04.2017.
 */

public class TranslationInteractor extends Interactor<Translation, TranslationInteractor.TranslationQuery> {

    private final String oneWordRegex = "\\S+";
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
            return translationDirectionGuessProvider.getTranslationDirection(params.getString())
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
        if (query.string.matches(oneWordRegex)) {
            return Observable.zip(shortTranslationProvider.getShortTranslation(query.getString(), query.getDirection()),
                                  expandedTranslationProvider.getExpandedTranslation(query.getString(), query.getDirection()),
                                  new BiFunction<String, String, Translation>() {
                                      @Override
                                      public Translation apply(String shortTranslation, String expandedTranslation) throws Exception {
                                          return new Translation(shortTranslation, expandedTranslation, query.getString(), query.getDirection());
                                      }
                                  });
        } else {
            return shortTranslationProvider
                    .getShortTranslation(query.getString(), query.getDirection())
                    .map(new Function<String, Translation>() {
                        @Override
                        public Translation apply(String shortTranslation) throws Exception {
                            return new Translation(shortTranslation, "", query.getString(), query.getDirection());
                        }
                    });
        }
    }

    public static class TranslationQuery {
        private String string;
        private TranslationDirection direction;

        private boolean shouldGuess;

        public TranslationQuery(String query, TranslationDirection direction, boolean shouldGuess) {
            this.string = query;
            this.direction = direction;
            this.shouldGuess = shouldGuess;
        }

        public boolean shouldGuess() {
            return shouldGuess;
        }

        public TranslationDirection getDirection() {
            return direction;
        }

        public String getString() {
            return string;
        }

        public TranslationQuery normalize() {
            return new TranslationQuery(string.trim(), direction, shouldGuess);
        }
    }
}