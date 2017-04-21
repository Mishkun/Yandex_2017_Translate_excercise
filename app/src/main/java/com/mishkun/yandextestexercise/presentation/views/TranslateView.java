package com.mishkun.yandextestexercise.presentation.views;

import com.mishkun.yandextestexercise.domain.entities.Definition;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Mishkun on 28.03.2017.
 */

public interface TranslateView extends HistoryView {
    int getTranslationTo();

    void setTranslationTo(int To);

    int getTranslationFrom();

    void setTranslationFrom(int From);

    boolean getGuessLanguage();

    Observable<TranslationQueryViewModel> getQueries();

    void reverseText();

    void setSupportedLanguages(List<String> supportedLanguages);

    void setTranslation(String translation);

    void setExpandedTranslation(Definition expandedTranslation);

    void showError(String errorMessage);

    void hideError();
}
