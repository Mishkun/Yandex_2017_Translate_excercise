package com.mishkun.yandextestexercise.domain.entities;

/**
 * Created by Mishkun on 28.03.2017.
 */

public class TranslationDirection {
    private Language mFrom;

    private Language mTo;

    public TranslationDirection(Language from, Language to) {
        this.mFrom = from;
        this.mTo = to;
    }



    public Language getTranslationFrom() {
        return mFrom;
    }

    public Language getTranslationTo() {
        return mTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TranslationDirection that = (TranslationDirection) o;

        if (!mFrom.equals(that.mFrom)) return false;
        return mTo.equals(that.mTo);

    }

    @Override
    public int hashCode() {
        int result = mFrom.hashCode();
        result = 31 * result + mTo.hashCode();
        return result;
    }
}