package com.mishkun.yandextestexercise.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.mishkun.yandextestexercise.R;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import retrofit2.Retrofit;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class YandexTranslationApi implements ShortTranslationProvider, TranslationDirectionGuessProvider, SupportedLanguagesProvider {

    private static final String API_KEY = "trnsl.1.1.20170414T212056Z.25cea1a11b69f402.e460c7b40c14a59917a1f93580ad7beeadc2d559";
    private static final String UI = "ru";
    private final YandexTranslationRetrofitApi yandexTranslateRetrofit;
    private final Context context;
    private final String TAG = YandexTranslationApi.class.getSimpleName();
    @Inject
    public YandexTranslationApi(YandexTranslationRetrofitApi yandexTranslateRetrofit, Context context) {
        this.yandexTranslateRetrofit = yandexTranslateRetrofit;
        this.context = context;
    }

    @Override
    public Observable<String> getShortTranslation(String query, TranslationDirection direction) {
        Log.d(TAG, TranslationDirectionMapper.transform(direction) + " " + query);
        return yandexTranslateRetrofit.translate(API_KEY, TranslationDirectionMapper.transform(direction), query).map(
                new Function<TranslationResponse, String>() {
                    @Override
                    public String apply(TranslationResponse translationResponse) throws Exception {
                        return translationResponse.getTranslation().get(0);
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
        }).onErrorResumeNext(Observable.just(getDefaultLanguages()));
    }

    private List<Language> getDefaultLanguages() {
        String[] keycodes = context.getResources().getStringArray(R.array.keyCodes);
        String[] displayNames = context.getResources().getStringArray(R.array.names);
        List<Language> languages = new ArrayList<>(keycodes.length);
        for (int i = 0; i < keycodes.length; i++) {
            languages.add(new Language(keycodes[i], displayNames[i]));
        }
        return languages;
    }

}
