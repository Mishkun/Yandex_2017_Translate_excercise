package com.mishkun.yandextestexercise.di.modules;

import android.app.Activity;

import com.mishkun.yandextestexercise.di.PerActivity;
import com.mishkun.yandextestexercise.domain.interactors.GetSupportedLanguagesInteractor;
import com.mishkun.yandextestexercise.domain.interactors.TranslationInteractor;
import com.mishkun.yandextestexercise.presentation.presenters.TranslatePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mishkun on 12.04.2017.
 */
@Module
public class MainActivityModule {
    private final Activity activity;

    public MainActivityModule(Activity activity) {
        this.activity = activity;
    }

    /**
     * Expose the activity to dependents in the graph.
     */
    @Provides
    @PerActivity
    public Activity provideActivity() {
        return this.activity;
    }

    @Provides
    @PerActivity
    public TranslatePresenter provideTranslationPresenter(){
        return new TranslatePresenter(new TranslationInteractor(), translationDirectionInteractor, new GetSupportedLanguagesInteractor(),
                                      swapTranslationDirectionInteractor);
    }
}
