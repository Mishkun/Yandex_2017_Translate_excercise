package com.mishkun.yandextestexercise.domain.interactors;

import com.mishkun.yandextestexercise.domain.entities.HistoryItem;
import com.mishkun.yandextestexercise.domain.providers.HistoryProvider;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class AddEditHistoryInteractor extends Interactor<List<HistoryItem>, HistoryItem> {

    private HistoryProvider historyProvider;

    AddEditHistoryInteractor(Scheduler threadExecutor, Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    Observable<List<HistoryItem>> buildUseCaseObservable(HistoryItem params) {
        return historyProvider.addHistoryItem(params)
                              .andThen(historyProvider.getHistoryItems());
    }
}
