package com.mishkun.yandextestexercise.views;

import com.mishkun.yandextestexercise.model.entities.HistoryItem;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Mishkun on 28.03.2017.
 */

public interface ListContentOrNothingView{
    Flowable<String> getSearchQueryStream();
    void setData(List<HistoryItem> historyItems);
}
