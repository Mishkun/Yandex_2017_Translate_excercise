package com.mishkun.yandextestexercise.data;

import android.util.Log;

import com.mishkun.yandextestexercise.data.api.YandexDictionaryRetrofitApi;
import com.mishkun.yandextestexercise.data.mappers.DictionaryResponseMapper;
import com.mishkun.yandextestexercise.data.mappers.TranslationDirectionMapper;
import com.mishkun.yandextestexercise.data.responses.DictionaryResponse;
import com.mishkun.yandextestexercise.domain.entities.Definition;
import com.mishkun.yandextestexercise.domain.entities.Translation;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
import com.mishkun.yandextestexercise.domain.providers.DictionarySupportedLanguagesProvider;
import com.mishkun.yandextestexercise.domain.providers.ExpandedTranslationProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class YandexDictionaryApi implements ExpandedTranslationProvider, DictionarySupportedLanguagesProvider {
    private static final String API_KEY = "dict.1.1.20170416T200201Z.0cb24acccf64ee4b.3662166758ddd66d0d174e44f6c1b4ae5611540d";
    private final YandexDictionaryRetrofitApi yandexDictionaryRetrofitApi;
    private final String TAG = YandexDictionaryApi.class.getSimpleName();

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

    @Override
    public Observable<List<TranslationDirection>> getSupportedLanguages() {
        return yandexDictionaryRetrofitApi.getSupportedLangs(API_KEY).map(new Function<List<String>, List<TranslationDirection>>() {
            @Override
            public List<TranslationDirection> apply(List<String> directions) throws Exception {
                List<TranslationDirection> translationDirections = new ArrayList<TranslationDirection>(directions.size());
                for (String direction : directions) {
                    translationDirections.add(TranslationDirectionMapper.transform(direction));
                }
                return translationDirections;
            }
        });
    }
}
