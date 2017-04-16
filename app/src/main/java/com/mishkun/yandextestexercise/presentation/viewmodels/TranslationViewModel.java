package com.mishkun.yandextestexercise.presentation.viewmodels;

/**
 * Created by Mishkun on 14.04.2017.
 */

public class TranslationViewModel {
    private String translation;
    private String expandedTranslation;

    public TranslationViewModel(String translation, String expandedTranslation) {
        this.translation = translation;
        this.expandedTranslation = expandedTranslation;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getExpandedTranslation() {
        return expandedTranslation;
    }

    public void setExpandedTranslation(String expandedTranslation) {
        this.expandedTranslation = expandedTranslation;
    }
}
