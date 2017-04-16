package com.mishkun.yandextestexercise.data;

import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
import com.mishkun.yandextestexercise.domain.providers.ExpandedTranslationProvider;

import io.reactivex.Observable;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class YandexDictionaryApi implements ExpandedTranslationProvider {
    @Override
    public Observable<String> getExpandedTranslation(String query, TranslationDirection direction) {
        return null;
    }
}
