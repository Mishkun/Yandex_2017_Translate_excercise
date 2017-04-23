package com.mishkun.yandextestexercise.domain.entities;

/**
 * Created by Mishkun on 28.03.2017.
 */

public class TranslationDirection {
    private static final String TAG = TranslationDirection.class.getSimpleName();
    private Language from;
    private Language to;

    public TranslationDirection(Language from, Language to) {
        this.from = from;
        this.to = to;
    }

    public void setFrom(Language from) {
        this.from = from;
    }

    public void setTo(Language to) {
        this.to = to;
    }

    public Language getTranslationFrom() {
        return from;
    }

    public Language getTranslationTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TranslationDirection that = (TranslationDirection) o;

        if (!from.equals(that.from)) return false;
        return to.equals(that.to);

    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }
}
