package com.mishkun.yandextestexercise.domain.providers;

import com.mishkun.yandextestexercise.domain.entities.HistoryItem;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Mishkun on 16.04.2017.
 */

public interface HistoryProvider {
    public Observable<List<HistoryItem>> getHistoryItems();
    public Completable addHistoryItem(HistoryItem item);
}
