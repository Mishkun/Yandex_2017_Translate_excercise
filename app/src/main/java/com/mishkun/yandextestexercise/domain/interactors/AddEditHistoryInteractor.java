package com.mishkun.yandextestexercise.domain.interactors;

import com.mishkun.yandextestexercise.di.modules.DomainModule;
import com.mishkun.yandextestexercise.domain.entities.ShortTranslationModel;
import com.mishkun.yandextestexercise.domain.providers.HistoryProvider;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class AddEditHistoryInteractor extends Interactor<Void, ShortTranslationModel> {

    private HistoryProvider historyProvider;

    @Inject
    AddEditHistoryInteractor(@Named(DomainModule.JOB) Scheduler threadExecutor, @Named(DomainModule.UI) Scheduler postExecutionThread,
                             HistoryProvider historyProvider) {
        super(threadExecutor, postExecutionThread);
        this.historyProvider = historyProvider;
    }

    @Override
    Observable<Void> buildUseCaseObservable(ShortTranslationModel params) {
        historyProvider.addOrUpdateHistoryItem(params);
        return Observable.never();
    }
}
