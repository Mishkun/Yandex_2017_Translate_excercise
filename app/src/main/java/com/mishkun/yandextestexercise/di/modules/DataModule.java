package com.mishkun.yandextestexercise.di.modules;

import com.mishkun.yandextestexercise.data.providers.HistoryDatabase;
import com.mishkun.yandextestexercise.data.providers.TranslationDirectionHolder;
import com.mishkun.yandextestexercise.data.providers.YandexDictionaryProvider;
import com.mishkun.yandextestexercise.data.providers.YandexTranslationProvider;
import com.mishkun.yandextestexercise.domain.providers.DictionarySupportedLanguagesProvider;
import com.mishkun.yandextestexercise.domain.providers.ExpandedTranslationProvider;
import com.mishkun.yandextestexercise.domain.providers.HistoryProvider;
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
    TranslationDirectionProvider provideTranslationDirectionHolder(TranslationDirectionHolder holder) {
        return holder;
    }

    @Provides
    @Singleton
    ExpandedTranslationProvider provideExpandedTranslationApi(YandexDictionaryProvider yandexDictionaryProvider) {
        return yandexDictionaryProvider;
    }

    @Provides
    @Singleton
    ShortTranslationProvider provideShortTranslationApi(YandexTranslationProvider yandexApi) {
        return yandexApi;
    }

    @Provides
    @Singleton
    TranslationDirectionGuessProvider provideGuessTranslationDirectionApi(YandexTranslationProvider yandexApi) {
        return yandexApi;
    }

    @Provides
    @Singleton
    SupportedLanguagesProvider provideSupportedLanguagesApi(YandexTranslationProvider yandexApi) {
        return yandexApi;
    }

    @Provides
    @Singleton
    DictionarySupportedLanguagesProvider provideDictionarySupportedLanguagesApi(YandexDictionaryProvider yandexApi) {
        return yandexApi;
    }

    @Provides
    @Singleton
    HistoryProvider provideHistory(HistoryDatabase historyDatabase){
        return historyDatabase;
    }
}
