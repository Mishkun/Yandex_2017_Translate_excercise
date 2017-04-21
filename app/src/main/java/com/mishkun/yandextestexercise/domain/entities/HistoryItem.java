package com.mishkun.yandextestexercise.domain.entities;

/**
 * Created by Mishkun on 28.03.2017.
 */


public class HistoryItem {
    private boolean favored;
    private String original;
    private String shortTranslation;

    public HistoryItem(String original, String shortTranslation, boolean favored) {
        this.favored = favored;
        this.original = original;
        this.shortTranslation = shortTranslation;
    }

    public boolean isFavored() {
        return favored;
    }

    public void setFavored(boolean favored) {
        this.favored = favored;
    }

    public String getShortTranslation() {
        return shortTranslation;
    }

    public void setShortTranslation(String shortTranslation) {
        this.shortTranslation = shortTranslation;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
}
