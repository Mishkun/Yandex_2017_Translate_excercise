package com.mishkun.yandextestexercise.data;

import com.mishkun.yandextestexercise.data.api.YandexDictionaryRetrofitApi;
import com.mishkun.yandextestexercise.data.api.YandexTranslationRetrofitApi;
import com.mishkun.yandextestexercise.data.mappers.DetectionResponseMapper;
import com.mishkun.yandextestexercise.data.mappers.SupportedLanguagesMapper;
import com.mishkun.yandextestexercise.data.mappers.TranslationDirectionMapper;
import com.mishkun.yandextestexercise.data.responses.DetectionResponse;
import com.mishkun.yandextestexercise.data.responses.SupportedLanguagesResponse;
import com.mishkun.yandextestexercise.data.responses.TranslationResponse;
import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
import com.mishkun.yandextestexercise.domain.providers.ShortTranslationProvider;
import com.mishkun.yandextestexercise.domain.providers.SupportedLanguagesProvider;
import com.mishkun.yandextestexercise.domain.providers.TranslationDirectionGuessProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Retrofit;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class YandexTranslationApi implements ShortTranslationProvider, TranslationDirectionGuessProvider, SupportedLanguagesProvider {

    private static final String API_KEY = "trnsl.1.1.20170414T212056Z.25cea1a11b69f402.e460c7b40c14a59917a1f93580ad7beeadc2d559";
    private static final String UI = "ru";
    private final YandexTranslationRetrofitApi yandexTranslateRetrofit;

    @Inject
    public YandexTranslationApi(YandexTranslationRetrofitApi yandexTranslateRetrofit) {
        this.yandexTranslateRetrofit = yandexTranslateRetrofit;
    }

    @Override
    public Observable<String> getShortTranslation(String query, TranslationDirection direction) {
        return yandexTranslateRetrofit.translate(API_KEY, TranslationDirectionMapper.transform(direction), query).map(
                new Function<TranslationResponse, String>() {
                    @Override
                    public String apply(TranslationResponse translationResponse) throws Exception {
                        return translationResponse.getTranslation();
                    }
                });
    }

    @Override
    public Observable<Language> guessLanguage(String query) {
        return yandexTranslateRetrofit.detectLanguage(API_KEY, query).map(new Function<DetectionResponse, Language>() {
            @Override
            public Language apply(DetectionResponse detectionResponse) throws Exception {
                return DetectionResponseMapper.transform(detectionResponse);
            }
        });
    }

    @Override
    public Observable<List<Language>> getSupportedLanguages() {
        return yandexTranslateRetrofit.getSupportedLanguages(API_KEY, UI).map(new Function<SupportedLanguagesResponse, List<Language>>() {
            @Override
            public List<Language> apply(SupportedLanguagesResponse supportedLanguagesResponse) throws Exception {
                return SupportedLanguagesMapper.transform(supportedLanguagesResponse);
            }
        });
    }
}
