package com.mishkun.yandextestexercise.model.entities;

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

    public TranslationDirection(Language to) {
        this.mTo = to;
    }

    public TranslationDirection getReversedDirection() {
        if (mFrom != null) {
            return new TranslationDirection(mTo, mFrom);
        } else {
            return null;
        }
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

        if (mFrom != null ? !mFrom.equals(that.mFrom) : that.mFrom != null) return false;
        return mTo.equals(that.mTo);

    }

    @Override
    public int hashCode() {
        int result = mFrom != null ? mFrom.hashCode() : 0;
        result = 31 * result + mTo.hashCode();
        return result;
    }
}
