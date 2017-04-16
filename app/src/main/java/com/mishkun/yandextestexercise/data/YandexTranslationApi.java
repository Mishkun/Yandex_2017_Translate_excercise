package com.mishkun.yandextestexercise.data;

import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
import com.mishkun.yandextestexercise.domain.providers.ShortTranslationProvider;
import com.mishkun.yandextestexercise.domain.providers.SupportedLanguagesProvider;
import com.mishkun.yandextestexercise.domain.providers.TranslationDirectionGuessProvider;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class YandexTranslationApi implements ShortTranslationProvider, TranslationDirectionGuessProvider, SupportedLanguagesProvider{
    @Override
    public Observable<String> getShortTranslation(String query, TranslationDirection direction) {
        return null;
    }

    @Override
    public Observable<TranslationDirection> getTranslationDirection(String query) {
        return null;
    }

    @Override
    public Observable<List<Language>> getSupportedLanguages() {
        return null;
    }
}
