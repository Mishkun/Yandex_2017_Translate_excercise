package com.mishkun.yandextestexercise.di.modules;

import com.mishkun.yandextestexercise.data.YandexDictionaryApi;
import com.mishkun.yandextestexercise.data.api.YandexDictionaryRetrofitApi;
import com.mishkun.yandextestexercise.data.api.YandexTranslationRetrofitApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Mishkun on 17.04.2017.
 */
@Module
public class RetrofitModule {
    private static final String BASE_URL_TRANSLATE = "https://translate.yandex.net/api/v1.5/tr.json/";
    private static final String BASE_URL_DICTIONARY = "https://dictionary.yandex.net/api/v1/dicservice.json/";

    @Provides
    @Singleton
    YandexTranslationRetrofitApi provideRetrofitForYandexTranslateApi() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL_TRANSLATE)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(YandexTranslationRetrofitApi.class);
    }

    @Provides
    @Singleton
    YandexDictionaryRetrofitApi provideRetrofitYandexDictionaryApi() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL_DICTIONARY)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(YandexDictionaryRetrofitApi.class);
    }
}