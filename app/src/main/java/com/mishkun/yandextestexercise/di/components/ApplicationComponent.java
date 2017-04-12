package com.mishkun.yandextestexercise.di.components;

import com.mishkun.yandextestexercise.AndroidApplication;
import com.mishkun.yandextestexercise.MainActivity;
import com.mishkun.yandextestexercise.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Mishkun on 12.04.2017.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(AndroidApplication application);
}
