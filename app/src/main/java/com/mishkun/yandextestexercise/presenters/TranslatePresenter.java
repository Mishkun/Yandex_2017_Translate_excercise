package com.mishkun.yandextestexercise.presenters;

import android.os.Debug;
import android.util.Log;

import com.mishkun.yandextestexercise.di.PerActivity;

import com.mishkun.yandextestexercise.views.TranslateView;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

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
    public void attachView(TranslateView view) {
        super.attachView(view);
        view.getTextToTranslateStream().subscribe(new Consumer<String>() {
            @Override
            public void accept(String text) throws Exception {
                TranslatePresenter.this.setTranslationString(text);
            }
        });
    }

    public void setTranslationString(String translationString){
        attachedView.setTranslation(translationString);
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
