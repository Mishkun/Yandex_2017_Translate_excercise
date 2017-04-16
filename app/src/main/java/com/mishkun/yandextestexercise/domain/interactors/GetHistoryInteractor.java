package com.mishkun.yandextestexercise.domain.interactors;

import com.mishkun.yandextestexercise.di.modules.DomainModule;
import com.mishkun.yandextestexercise.domain.entities.HistoryItem;
import com.mishkun.yandextestexercise.domain.providers.HistoryProvider;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class GetHistoryInteractor extends Interactor<List<HistoryItem>, Void> {
    private HistoryProvider historyProvider;

    @Inject
    GetHistoryInteractor(@Named(DomainModule.JOB) Scheduler threadExecutor, @Named(DomainModule.UI) Scheduler postExecutionThread,
                         HistoryProvider historyProvider) {
        super(threadExecutor, postExecutionThread);
        this.historyProvider = historyProvider;
    }

    @Override
    Observable<List<HistoryItem>> buildUseCaseObservable(Void params) {
        return historyProvider.getHistoryItems();
    }
}
