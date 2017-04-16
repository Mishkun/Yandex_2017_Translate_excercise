package com.mishkun.yandextestexercise.presentation.views;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Mishkun on 28.03.2017.
 */

public interface TranslateView{
    void setTranslationTo(int To);
    void setTranslationFrom(int From);
    void setSupportedLanguages(List<String> supportedLanguages);
    void setTranslation(String translation);
    void setExpandedTranslation(String expandedTranslation);
    void showError(String errorMessage);
    void hideError();
    Observable<String> getTextToTranslateStream();
}
