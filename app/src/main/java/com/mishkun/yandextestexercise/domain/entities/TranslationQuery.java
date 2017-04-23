package com.mishkun.yandextestexercise.domain.entities;

import com.mishkun.yandextestexercise.domain.interactors.TranslationInteractor;

/**
 * Created by Mishkun on 23.04.2017.
 */
public class TranslationQuery {
    private String string;
    private TranslationDirection direction;

    private boolean shouldGuess;

    public TranslationQuery(String query, TranslationDirection direction, boolean shouldGuess) {
        this.string = query;
        this.direction = direction;
        this.shouldGuess = shouldGuess;
    }

    public boolean shouldGuess() {
        return shouldGuess;
    }

    public TranslationDirection getDirection() {
        return direction;
    }

    public String getString() {
        return string;
    }

    public TranslationQuery normalize() {
        String newQuery = string.trim();
        // Yandex Translate can't into empty strings
        if (!newQuery.equals("")) {
            return new TranslationQuery(newQuery, direction, shouldGuess);
        } else {
            return new TranslationQuery(" ", direction, shouldGuess);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TranslationQuery that = (TranslationQuery) o;

        if (shouldGuess != that.shouldGuess) return false;
        if (!string.equals(that.string)) return false;
        return direction.equals(that.direction);

    }

    @Override
    public int hashCode() {
        int result = string.hashCode();
        result = 31 * result + direction.hashCode();
        result = 31 * result + (shouldGuess ? 1 : 0);
        return result;
    }
}
