package com.mishkun.yandextestexercise.domain.interactors;

import com.mishkun.yandextestexercise.di.modules.DomainModule;
import com.mishkun.yandextestexercise.domain.entities.HistoryItem;
import com.mishkun.yandextestexercise.domain.providers.HistoryProvider;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class GetFavoritesInteractor extends Interactor<List<HistoryItem>, Void> {
    private HistoryProvider historyProvider;

    @Inject
    GetFavoritesInteractor(@Named(DomainModule.JOB) Scheduler threadExecutor, @Named(DomainModule.UI) Scheduler postExecutionThread,
                           HistoryProvider historyProvider) {
        super(threadExecutor, postExecutionThread);
        this.historyProvider = historyProvider;
    }

    @Override
    Observable<List<HistoryItem>> buildUseCaseObservable(Void params) {
        return historyProvider.getHistoryItems().map(new Function<List<HistoryItem>, List<HistoryItem>>() {
            @Override
            public List<HistoryItem> apply(List<HistoryItem> historyItems) throws Exception {
                Iterator<HistoryItem> historyIterator = historyItems.iterator();
                while (historyIterator.hasNext()) {
                    HistoryItem item = historyIterator.next();
                    if (!item.isFavored()) {
                        historyIterator.remove();
                    }
                }
                return historyItems;
            }
        });
    }
}