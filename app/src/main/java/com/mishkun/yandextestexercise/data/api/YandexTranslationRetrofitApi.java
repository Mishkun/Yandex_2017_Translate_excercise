package com.mishkun.yandextestexercise.data.api;

import com.mishkun.yandextestexercise.data.responses.DetectionResponse;
import com.mishkun.yandextestexercise.data.responses.SupportedLanguagesResponse;
import com.mishkun.yandextestexercise.data.responses.TranslationResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mishkun on 16.04.2017.
 */

public interface YandexTranslationRetrofitApi {

    @GET("getLangs")
    Observable<SupportedLanguagesResponse> getSupportedLanguages(@Query("key") String key, @Query("ui") String ui);

    @GET("detect")
    Observable<DetectionResponse> detectLanguage(@Query("key") String key, @Query("text") String text);

    @GET("translate")
    Observable<TranslationResponse> translate(@Query("key") String key, @Query("lang") String languageDirection, @Query("text") String text);

}
