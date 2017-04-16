package com.mishkun.yandextestexercise.data;

import com.mishkun.yandextestexercise.domain.entities.HistoryItem;
import com.mishkun.yandextestexercise.domain.providers.HistoryProvider;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class HistoryDatabase implements HistoryProvider {
    @Override
    public Observable<List<HistoryItem>> getHistoryItems() {
        return null;
    }

    @Override
    public Completable addHistoryItem(HistoryItem item) {
        return null;
    }
}
