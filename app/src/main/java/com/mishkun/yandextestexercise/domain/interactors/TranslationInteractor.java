package com.mishkun.yandextestexercise.domain.interactors;

import com.mishkun.yandextestexercise.di.modules.DomainModule;
import com.mishkun.yandextestexercise.domain.entities.Translation;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
import com.mishkun.yandextestexercise.domain.providers.ExpandedTranslationProvider;
import com.mishkun.yandextestexercise.domain.providers.ShortTranslationProvider;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * Created by Mishkun on 12.04.2017.
 */

public class TranslationInteractor extends Interactor<Translation, TranslationInteractor.TranslationQuery> {

    private ShortTranslationProvider shortTranslationProvider;
    private ExpandedTranslationProvider expandedTranslationProvider;

    private final String oneWordRegex = "\\S+";

    @Inject
    TranslationInteractor(@Named(DomainModule.JOB) Scheduler threadExecutor, @Named(DomainModule.UI) Scheduler postExecutionThread, ShortTranslationProvider shortTranslationProvider, ExpandedTranslationProvider expandedTranslationProvider) {
        super(threadExecutor, postExecutionThread);
        this.shortTranslationProvider = shortTranslationProvider;
        this.expandedTranslationProvider = expandedTranslationProvider;
    }

    @Override
    Observable<Translation> buildUseCaseObservable(final TranslationQuery params) {
        final TranslationQuery query = params.normalize();
        if (query.string.matches(oneWordRegex)) {
            return Observable
                    .zip(shortTranslationProvider.getShortTranslation(query.getString(), query
                            .getDirection()), expandedTranslationProvider
                                 .getExpandedTranslation(query.getString(), query
                                         .getDirection()), new BiFunction<String, String, Translation>() {
                        @Override
                        public Translation apply(String shortTranslation, String expandedTranslation) throws Exception {
                            return new Translation(shortTranslation, expandedTranslation, query
                                    .getString(), query.getDirection());
                        }
                    });
        } else {
            return shortTranslationProvider
                    .getShortTranslation(query.getString(), query.getDirection())
                    .map(new Function<String, Translation>() {
                        @Override
                        public Translation apply(String shortTranslation) throws Exception {
                            return new Translation(shortTranslation, "", query.getString(), query
                                    .getDirection());
                        }
                    });
        }
    }

    public class TranslationQuery {
        private String string;
        private TranslationDirection direction;

        public TranslationQuery(String query, TranslationDirection direction) {
            this.string = query;
            this.direction = direction;
        }

        public TranslationDirection getDirection() {
            return direction;
        }

        public String getString() {
            return string;
        }

        public TranslationQuery normalize() {
            return new TranslationQuery(string.trim(), direction);
        }
    }
}
