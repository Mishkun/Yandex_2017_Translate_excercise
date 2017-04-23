package com.mishkun.yandextestexercise.presentation.views;

import com.mishkun.yandextestexercise.domain.entities.Definition;
import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.domain.entities.TranslationQuery;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Mishkun on 28.03.2017.
 */

public interface TranslateView extends HistoryView {
    Language getTranslationTo();

    void setTranslationTo(Language To);

    Language getTranslationFrom();

    void setTranslationFrom(Language From);

    boolean getGuessLanguage();

    void showProgressBar();
    void hideProgressBar();

    Observable<TranslationQuery> getQueries();

    void reverseText();

    void setSupportedLanguages(List<Language> supportedLanguages);

    void setTranslation(String translation);

    void setExpandedTranslation(Definition expandedTranslation);

    void setIsCurrentTranslationFavored(boolean favored);
}
