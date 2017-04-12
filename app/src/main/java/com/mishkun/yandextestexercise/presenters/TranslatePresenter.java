package com.mishkun.yandextestexercise.presenters;

import android.os.Debug;
import android.util.Log;

import com.mishkun.yandextestexercise.di.PerActivity;

import com.mishkun.yandextestexercise.views.TranslateView;

import javax.inject.Inject;

/**
 * Created by Mishkun on 28.03.2017.
 */

@PerActivity
public class TranslatePresenter extends Presenter<TranslateView> {

    private static final String TAG = TranslatePresenter.class.getSimpleName();

    @Inject
    public TranslatePresenter() {
    }

    public void OnReverseTranslationButtonClicked() {
        Log.d(TAG, "OnReverseTranslationButtonClicked");
    }


    public void OnToDirectionButtonClicked() {

    }


    public void OnFromDirectionButtonClicked() {

    }


    public void OnFavoriteButtonClicked() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }
}
