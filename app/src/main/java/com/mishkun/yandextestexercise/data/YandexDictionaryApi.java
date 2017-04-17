package com.mishkun.yandextestexercise.data;

import com.mishkun.yandextestexercise.data.api.YandexDictionaryRetrofitApi;
import com.mishkun.yandextestexercise.data.mappers.DictionaryResponseMapper;
import com.mishkun.yandextestexercise.data.mappers.TranslationDirectionMapper;
import com.mishkun.yandextestexercise.data.responses.DictionaryResponse;
import com.mishkun.yandextestexercise.domain.entities.Definition;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
import com.mishkun.yandextestexercise.domain.providers.ExpandedTranslationProvider;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class YandexDictionaryApi implements ExpandedTranslationProvider {
    private static final String API_KEY = "dict.1.1.20170416T200201Z.0cb24acccf64ee4b.3662166758ddd66d0d174e44f6c1b4ae5611540d";
    private final YandexDictionaryRetrofitApi yandexDictionaryRetrofitApi;

    @Inject
    public YandexDictionaryApi(YandexDictionaryRetrofitApi yandexDictionaryRetrofitApi) {
        this.yandexDictionaryRetrofitApi = yandexDictionaryRetrofitApi;
    }

    @Override
    public Observable<Definition> getExpandedTranslation(String query, TranslationDirection direction) {
        return yandexDictionaryRetrofitApi.getDictionaryTranslation(API_KEY, TranslationDirectionMapper.transform(direction), query).map(
                new Function<DictionaryResponse, Definition>() {
                    @Override
                    public Definition apply(DictionaryResponse dictionaryResponse) throws Exception {
                        return DictionaryResponseMapper.transform(dictionaryResponse);
                    }
                });
    }
}
