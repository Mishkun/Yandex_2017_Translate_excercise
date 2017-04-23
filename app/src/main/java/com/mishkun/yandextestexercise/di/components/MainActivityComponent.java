package com.mishkun.yandextestexercise.di.components;

import com.mishkun.yandextestexercise.MainActivity;
import com.mishkun.yandextestexercise.di.modules.DomainModule;
import com.mishkun.yandextestexercise.di.modules.MainActivityModule;
import com.mishkun.yandextestexercise.di.PerActivity;
import com.mishkun.yandextestexercise.presentation.views.fragments.BookmarksFragment;
import com.mishkun.yandextestexercise.presentation.views.fragments.HomeFragment;

import dagger.Component;

/**
 * Created by Mishkun on 12.04.2017.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = MainActivityModule.class)
public interface MainActivityComponent {
    void inject(MainActivity activity);
}
