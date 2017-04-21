package com.mishkun.yandextestexercise.domain.interactors;

import com.mishkun.yandextestexercise.di.modules.DomainModule;
import com.mishkun.yandextestexercise.domain.providers.HistoryProvider;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by Mishkun on 21.04.2017.
 */

public class ClearFavoritesInteractor extends ParameterlessInteractor<Void> {

    private HistoryProvider historyProvider;

    @Inject
    ClearFavoritesInteractor(@Named(DomainModule.JOB) Scheduler threadExecutor, @Named(DomainModule.UI) Scheduler postExecutionThread,
                             HistoryProvider historyProvider) {
        super(threadExecutor, postExecutionThread);
        this.historyProvider = historyProvider;
    }

    @Override
    Observable<Void> buildUseCaseObservable() {
        historyProvider.clearFavoritesData();
        return Observable.never();
    }
}
