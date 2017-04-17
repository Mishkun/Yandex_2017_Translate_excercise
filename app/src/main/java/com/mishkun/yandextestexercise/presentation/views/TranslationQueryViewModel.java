package com.mishkun.yandextestexercise.presentation.views;

/**
 * Created by Mishkun on 18.04.2017.
 */

public class TranslationQueryViewModel {
    private int translationTo;
    private int translationFrom;
    private String query;

    public TranslationQueryViewModel(int translationTo, int translationFrom, String query) {
        this.translationTo = translationTo;
        this.translationFrom = translationFrom;
        this.query = query;
    }

    public int getTranslationTo() {
        return translationTo;
    }

    public int getTranslationFrom() {
        return translationFrom;
    }

    public String getQuery() {
        return query;
    }
}
