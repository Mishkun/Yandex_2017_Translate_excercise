package com.mishkun.yandextestexercise.presentation.views;

import com.mishkun.yandextestexercise.domain.entities.Definition;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Mishkun on 28.03.2017.
 */

public interface TranslateView{
    void setTranslationTo(int To);
    void setTranslationFrom(int From);

    int getTranslationTo();
    int getTranslationFrom();

    Observable<TranslationQueryViewModel> getQueries();
    void reverseText();
    void setSupportedLanguages(List<String> supportedLanguages);
    void setTranslation(String translation);
    void setExpandedTranslation(Definition expandedTranslation);
    void showError(String errorMessage);
    void hideError();
}
