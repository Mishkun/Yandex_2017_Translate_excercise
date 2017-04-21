package com.mishkun.yandextestexercise.di.modules;

import android.content.Context;

import com.mishkun.yandextestexercise.AndroidApplication;
import com.mishkun.yandextestexercise.InternetConnection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mishkun on 11.04.2017.
 */
@Module
public class ApplicationModule {

    private final AndroidApplication application;

    public ApplicationModule(AndroidApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    InternetConnection provideInternetConnection(Context context) {
        return new InternetConnection(context);
    }
}
