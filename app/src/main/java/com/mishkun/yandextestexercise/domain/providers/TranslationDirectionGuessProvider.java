package com.mishkun.yandextestexercise.domain.providers;

import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;

import io.reactivex.Observable;

/**
 * Created by Mishkun on 16.04.2017.
 */

public interface TranslationDirectionGuessProvider {
    public Observable<Language> guessLanguage(String query);
}
