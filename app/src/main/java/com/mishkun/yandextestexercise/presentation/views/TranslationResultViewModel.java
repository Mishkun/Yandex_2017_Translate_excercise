package com.mishkun.yandextestexercise.presentation.views;

import com.mishkun.yandextestexercise.domain.entities.Definition;

/**
 * Created by Mishkun on 20.04.2017.
 */

public class TranslationResultViewModel {
    public final String input;
    public final String translation;
    public final Definition expandedTranslation;
    public final int translationFrom;
    public final int translationTo;
    public final boolean favored;

    public TranslationResultViewModel(String input, String translation, Definition expandedTranslation, int translationFrom, int translationTo,
                                      boolean favored) {
        this.input = input;
        this.translation = translation;
        this.expandedTranslation = expandedTranslation;
        this.translationFrom = translationFrom;
        this.translationTo = translationTo;
        this.favored = favored;
    }
}
