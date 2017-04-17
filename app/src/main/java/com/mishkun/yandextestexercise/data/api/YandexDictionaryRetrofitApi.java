package com.mishkun.yandextestexercise.data.api;

import com.mishkun.yandextestexercise.data.responses.DictionaryResponse;

import io.reactivex.Observable;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mishkun on 17.04.2017.
 */

public interface YandexDictionaryRetrofitApi {
    @GET("/lookup")
    Observable<DictionaryResponse> getDictionaryTranslation(@Query("key") String key, @Query("lang") String direction, @Query("text") String text);
}
