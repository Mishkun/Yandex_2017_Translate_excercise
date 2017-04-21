package com.mishkun.yandextestexercise.di.components;

import com.mishkun.yandextestexercise.AndroidApplication;
import com.mishkun.yandextestexercise.MainActivity;
import com.mishkun.yandextestexercise.data.ShortTranslation;
import com.mishkun.yandextestexercise.di.modules.ApplicationModule;
import com.mishkun.yandextestexercise.di.modules.DataModule;
import com.mishkun.yandextestexercise.di.modules.DbModule;
import com.mishkun.yandextestexercise.di.modules.DomainModule;
import com.mishkun.yandextestexercise.di.modules.RetrofitModule;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
import com.mishkun.yandextestexercise.domain.providers.DictionarySupportedLanguagesProvider;
import com.mishkun.yandextestexercise.domain.providers.ExpandedTranslationProvider;
import com.mishkun.yandextestexercise.domain.providers.HistoryProvider;
import com.mishkun.yandextestexercise.domain.providers.ShortTranslationProvider;
import com.mishkun.yandextestexercise.domain.providers.SupportedLanguagesProvider;
import com.mishkun.yandextestexercise.domain.providers.TranslationDirectionGuessProvider;
import com.mishkun.yandextestexercise.domain.providers.TranslationDirectionProvider;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import io.reactivex.Scheduler;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

import static com.mishkun.yandextestexercise.di.modules.DomainModule.JOB;
import static com.mishkun.yandextestexercise.di.modules.DomainModule.UI;

/**
 * Created by Mishkun on 12.04.2017.
 */

@Singleton
@Component(modules = {ApplicationModule.class, DomainModule.class, DataModule.class, RetrofitModule.class, DbModule.class})
public interface ApplicationComponent {
    void inject(AndroidApplication application);

    //Exposed to sub-graphs
    ExpandedTranslationProvider expandedTranslationProvider();

    ShortTranslationProvider shortTranslationProvider();

    TranslationDirectionGuessProvider translationDirectionGuessProvider();

    SupportedLanguagesProvider supportedLanguagesProvider();

    DictionarySupportedLanguagesProvider dictionarySupportedLanguagesProvider();

    TranslationDirectionProvider translationDirectionProvider();

    HistoryProvider provideHistory();

    @Named(JOB)
    Scheduler provideJobScheduler();

    @Named(UI)
    Scheduler provideUiScheduler();
}
