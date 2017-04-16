package com.mishkun.yandextestexercise.domain.providers;

import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;

import io.reactivex.Observable;

/**
 * Created by Mishkun on 16.04.2017.
 */

public interface TranslationDirectionProvider {
    public Observable<TranslationDirection> getTranslationDirection();

    public TranslationDirection setTranslationDirection(TranslationDirection translationDirection);
}
