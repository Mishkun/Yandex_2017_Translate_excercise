package com.mishkun.yandextestexercise.data;

import com.mishkun.yandextestexercise.data.api.YandexDictionaryRetrofitApi;
import com.mishkun.yandextestexercise.domain.entities.Definition;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
import com.mishkun.yandextestexercise.domain.providers.ExpandedTranslationProvider;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class YandexDictionaryApi implements ExpandedTranslationProvider {
    private final YandexDictionaryRetrofitApi yandexDictionaryRetrofitApi;

    @Inject
    public YandexDictionaryApi(YandexDictionaryRetrofitApi yandexDictionaryRetrofitApi) {
        this.yandexDictionaryRetrofitApi = yandexDictionaryRetrofitApi;
    }

    @Override
    public Observable<Definition> getExpandedTranslation(String query, TranslationDirection direction) {
        return null;
    }
}
