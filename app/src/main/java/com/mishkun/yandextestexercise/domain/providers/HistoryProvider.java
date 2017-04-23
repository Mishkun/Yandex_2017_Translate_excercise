package com.mishkun.yandextestexercise.domain.providers;

import com.mishkun.yandextestexercise.domain.entities.ShortTranslationModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Mishkun on 16.04.2017.
 */

public interface HistoryProvider {
    public Observable<List<ShortTranslationModel>> getHistoryItems();
    public Observable<List<ShortTranslationModel>> getFavoredItems();

    public void addOrUpdateHistoryItem(ShortTranslationModel item);

    public void clearFavoritesData();


    public void clearHistoryData();

    void deleteHistoryItem(ShortTranslationModel item);
}
