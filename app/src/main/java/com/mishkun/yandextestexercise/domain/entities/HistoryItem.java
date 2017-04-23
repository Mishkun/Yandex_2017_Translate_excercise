package com.mishkun.yandextestexercise.domain.entities;

/**
 * Created by Mishkun on 28.03.2017.
 */


public class HistoryItem {
    private boolean saved = true;
    private boolean favored;
    private String original;
    private String shortTranslation;
    private Language from;
    private Language to;

    public HistoryItem(String original, String shortTranslation, boolean favored, Language from, Language to) {
        this.favored = favored;
        this.original = original;
        this.shortTranslation = shortTranslation;
        this.from = from;
        this.to = to;
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

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public Language getFrom() {
        return from;
    }

    public void setFrom(Language from) {
        this.from = from;
    }

    public Language getTo() {
        return to;
    }

    public void setTo(Language to) {
        this.to = to;
    }
}
