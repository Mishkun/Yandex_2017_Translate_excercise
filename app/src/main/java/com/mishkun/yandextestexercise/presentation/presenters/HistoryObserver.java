package com.mishkun.yandextestexercise.presentation.presenters;

import android.util.Log;

import com.mishkun.yandextestexercise.domain.entities.ShortTranslationModel;
import com.mishkun.yandextestexercise.presentation.views.HistoryView;

import java.util.List;

/**
 * Created by Mishkun on 21.04.2017.
 */

class HistoryObserver extends MutedObserver<List<ShortTranslationModel>> {

    private static final String TAG = HistoryObserver.class.getSimpleName();
    private HistoryView historyView;

    HistoryObserver(HistoryView historyView) {
        this.historyView = historyView;
    }

    @Override
    public void onNext(List<ShortTranslationModel> value) {
        historyView.setData(value);
    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, e.getStackTrace().toString());
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete");
    }
}
