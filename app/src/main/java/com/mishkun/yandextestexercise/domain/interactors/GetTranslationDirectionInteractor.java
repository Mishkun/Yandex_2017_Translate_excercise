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

public class GetTranslationDirectionInteractor extends ParameterlessInteractor<TranslationDirection> {

    private TranslationDirectionProvider translationDirectionProvider;

    @Inject
    GetTranslationDirectionInteractor(@Named(DomainModule.JOB) Scheduler threadExecutor, @Named(DomainModule.UI) Scheduler postExecutionThread, TranslationDirectionProvider translationDirectionProvider) {
        super(threadExecutor, postExecutionThread);
        this.translationDirectionProvider = translationDirectionProvider;
    }

    @Override
    Observable<TranslationDirection> buildUseCaseObservable() {
        return translationDirectionProvider.getTranslationDirection();
    }
}
