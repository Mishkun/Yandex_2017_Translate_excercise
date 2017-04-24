package com.mishkun.yandextestexercise.domain.entities;

/**
 * Created by Mishkun on 28.03.2017.
 */


public class ShortTranslationModel {
    private boolean favored;
    private String original;
    private String translation;

    public TranslationDirection getDirection() {
        return direction;
    }

    public void setDirection(TranslationDirection direction) {
        this.direction = direction;
    }

    private TranslationDirection direction;

    public ShortTranslationModel(String original, String translation, boolean favored, Language from, Language to) {
        this.favored = favored;
        this.original = original;
        this.translation = translation;
        direction = new TranslationDirection(from, to);
    }

    public boolean isFavored() {
        return favored;
    }

    public void setFavored(boolean favored) {
        this.favored = favored;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }



    public Language getFrom() {
        return direction.getTranslationFrom();
    }

    public void setFrom(Language from) {
        this.direction.setFrom(from);
    }

    public Language getTo() {
        return direction.getTranslationTo();
    }

    public void setTo(Language to) {
        this.direction.setTo(to);
    }
}
