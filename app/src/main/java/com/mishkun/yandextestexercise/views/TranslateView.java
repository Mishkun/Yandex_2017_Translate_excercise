package com.mishkun.yandextestexercise.views;

import com.mishkun.yandextestexercise.model.entities.TranslationDirection;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Mishkun on 28.03.2017.
 */

public interface TranslateView{
    void setTranslationTo(String To);
    void setTranslationFrom(String From);
    void setSupportedLanguages(List<String> supportedLanguages);
    void setTranslation(String translation);
    void setExpandedTranslation(String expandedTranslation);
    void showError(String errorMessage);
    void hideError();
    Flowable<String> getTextToTranslateStream();
}
