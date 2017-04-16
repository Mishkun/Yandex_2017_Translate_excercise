package com.mishkun.yandextestexercise.domain.entities;

/**
 * Created by Mishkun on 15.04.2017.
 */

public class Translation {
    private String shortTranslation;
    private String expandedTranslation;
    private String original;
    private TranslationDirection direction;

    public Translation(String shortTranslation, String expandedTranslation, String original, TranslationDirection direction) {
        this.shortTranslation = shortTranslation;
        this.expandedTranslation = expandedTranslation;
        this.original = original;
        this.direction = direction;
    }

    public String getShortTranslation() {
        return shortTranslation;
    }

    public void setShortTranslation(String shortTranslation) {
        this.shortTranslation = shortTranslation;
    }

    public String getExpandedTranslation() {
        return expandedTranslation;
    }

    public void setExpandedTranslation(String expandedTranslation) {
        this.expandedTranslation = expandedTranslation;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public TranslationDirection getDirection() {
        return direction;
    }

    public void setDirection(TranslationDirection direction) {
        this.direction = direction;
    }
}
