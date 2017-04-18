package com.mishkun.yandextestexercise.presentation.views;

/**
 * Created by Mishkun on 18.04.2017.
 */

public class TranslationQueryViewModel {
    private int translationTo;
    private int translationFrom;
    private String query;

    public TranslationQueryViewModel( int translationFrom, int translationTo, String query) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TranslationQueryViewModel that = (TranslationQueryViewModel) o;

        if (translationTo != that.translationTo) return false;
        if (translationFrom != that.translationFrom) return false;
        return query.equals(that.query);

    }

    @Override
    public int hashCode() {
        int result = translationTo;
        result = 31 * result + translationFrom;
        result = 31 * result + query.hashCode();
        return result;
    }
}
