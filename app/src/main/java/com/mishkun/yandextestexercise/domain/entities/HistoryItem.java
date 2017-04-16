package com.mishkun.yandextestexercise.domain.entities;

/**
 * Created by Mishkun on 28.03.2017.
 */


public class HistoryItem {
    private boolean favored;
    private Translation translation;

    public HistoryItem(boolean favored, Translation translation) {
        this.favored = favored;
        this.translation = translation;
    }

    public boolean isFavored() {
        return favored;
    }

    public void setFavored(boolean favored) {
        this.favored = favored;
    }

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }
}
