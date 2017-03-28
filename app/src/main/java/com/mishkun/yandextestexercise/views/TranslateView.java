package com.mishkun.yandextestexercise.views;

import com.arellomobile.mvp.MvpView;
import com.mishkun.yandextestexercise.model.entities.TranslationDirection;

import io.reactivex.Flowable;

/**
 * Created by Mishkun on 28.03.2017.
 */

public interface TranslateView extends MvpView{
    void setTranslationDirection(TranslationDirection direction);
    void setTranslation(String translation);
    void setExpandedTranslation(String expandedTranslation);
    void showFromLanguageDialog();
    void showToLanguageDialog();
    void showError(String errorMessage);
    void hideError();
    Flowable<String> getTextToTranslateStream();
}
