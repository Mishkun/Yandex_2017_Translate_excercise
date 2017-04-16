package com.mishkun.yandextestexercise.data;

import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
import com.mishkun.yandextestexercise.domain.providers.TranslationDirectionProvider;

import io.reactivex.Observable;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class TranslationDirectionHolder implements TranslationDirectionProvider {
    @Override
    public Observable<TranslationDirection> getTranslationDirection() {
        return null;
    }

    @Override
    public TranslationDirection setTranslationDirection(TranslationDirection translationDirection) {
        return null;
    }
}
