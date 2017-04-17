package com.mishkun.yandextestexercise.di.modules;

import com.mishkun.yandextestexercise.data.TranslationDirectionHolder;
import com.mishkun.yandextestexercise.data.YandexDictionaryApi;
import com.mishkun.yandextestexercise.data.YandexTranslationApi;
import com.mishkun.yandextestexercise.domain.providers.ExpandedTranslationProvider;
import com.mishkun.yandextestexercise.domain.providers.ShortTranslationProvider;
import com.mishkun.yandextestexercise.domain.providers.SupportedLanguagesProvider;
import com.mishkun.yandextestexercise.domain.providers.TranslationDirectionGuessProvider;
import com.mishkun.yandextestexercise.domain.providers.TranslationDirectionProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mishkun on 16.04.2017.
 */
@Module
public class DataModule {


    @Provides
    @Singleton
    TranslationDirectionHolder provideTranslationDirectionHolder() {
        return new TranslationDirectionHolder();
    }

    @Provides
    @Singleton
    ExpandedTranslationProvider provideExpandedTranslationApi(YandexDictionaryApi yandexDictionaryApi) {
        return yandexDictionaryApi;
    }

    @Provides
    @Singleton
    ShortTranslationProvider provideShortTranslationApi(YandexTranslationApi yandexApi) {
        return yandexApi;
    }

    @Provides
    @Singleton
    TranslationDirectionGuessProvider provideGuessTranslationDirectionApi(YandexTranslationApi yandexApi) {
        return yandexApi;
    }

    @Provides
    @Singleton
    SupportedLanguagesProvider provideSupportedLanguagesApi(YandexTranslationApi yandexApi) {
        return yandexApi;
    }

    @Provides
    @Singleton
    TranslationDirectionProvider provideTranslationDirectionApi(TranslationDirectionHolder holder) {
        return holder;
    }
}
