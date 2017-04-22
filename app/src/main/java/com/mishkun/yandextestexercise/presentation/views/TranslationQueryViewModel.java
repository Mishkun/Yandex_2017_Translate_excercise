package com.mishkun.yandextestexercise.presentation.views;

import com.mishkun.yandextestexercise.domain.entities.Language;

/**
 * Created by Mishkun on 18.04.2017.
 */

public class TranslationQueryViewModel {
    private Language translationTo;
    private Language translationFrom;
    private String query;

    public TranslationQueryViewModel( Language translationFrom, Language translationTo, String query) {
        this.translationTo = translationTo;
        this.translationFrom = translationFrom;
        this.query = query;
    }

    public Language getTranslationTo() {
        return translationTo;
    }

    public Language getTranslationFrom() {
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
        int result = translationTo.hashCode();
        result = 31 * result + translationFrom.hashCode();
        result = 31 * result + query.hashCode();
        return result;
    }
}
