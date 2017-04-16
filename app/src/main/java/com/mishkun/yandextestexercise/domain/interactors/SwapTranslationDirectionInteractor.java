package com.mishkun.yandextestexercise.domain.interactors;

import com.mishkun.yandextestexercise.di.modules.DomainModule;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
import com.mishkun.yandextestexercise.domain.providers.TranslationDirectionProvider;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class SwapTranslationDirectionInteractor extends Interactor<TranslationDirection,TranslationDirection> {

    @Inject
    SwapTranslationDirectionInteractor(@Named(DomainModule.JOB) Scheduler threadExecutor, @Named(DomainModule.UI) Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    Observable<TranslationDirection> buildUseCaseObservable(TranslationDirection params) {
        return Observable.just(new TranslationDirection(params.getTranslationTo(), params.getTranslationFrom()));
    }
}
