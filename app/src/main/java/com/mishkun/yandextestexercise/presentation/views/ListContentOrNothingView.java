package com.mishkun.yandextestexercise.presentation.views;

import com.mishkun.yandextestexercise.domain.entities.HistoryItem;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Mishkun on 28.03.2017.
 */

public interface ListContentOrNothingView{
    Flowable<String> getSearchQueryStream();
    void setData(List<HistoryItem> historyItems);
}
