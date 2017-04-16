package com.mishkun.yandextestexercise.domain.interactors;

import com.mishkun.yandextestexercise.di.modules.DomainModule;
import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.domain.providers.SupportedLanguagesProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by Mishkun on 12.04.2017.
 */

public class GetSupportedLanguagesInteractor extends ParameterlessInteractor<List<Language>> {


    private final SupportedLanguagesProvider supportedLanguagesProvider;

    @Inject
    GetSupportedLanguagesInteractor(@Named(DomainModule.JOB) Scheduler threadExecutor, @Named(DomainModule.UI) Scheduler postExecutionThread,
                                    SupportedLanguagesProvider supportedLanguagesProvider) {
        super(threadExecutor, postExecutionThread);
        this.supportedLanguagesProvider = supportedLanguagesProvider;
    }


    @Override
    Observable<List<Language>> buildUseCaseObservable() {
        return supportedLanguagesProvider.getSupportedLanguages();
    }
}

