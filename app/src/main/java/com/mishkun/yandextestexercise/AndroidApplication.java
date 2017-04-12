package com.mishkun.yandextestexercise;

import android.app.Application;

import com.mishkun.yandextestexercise.di.components.ApplicationComponent;
import com.mishkun.yandextestexercise.di.components.DaggerApplicationComponent;
import com.mishkun.yandextestexercise.di.modules.ApplicationModule;

/**
 * Created by Mishkun on 12.04.2017.
 */

public class AndroidApplication extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate(){
        super.onCreate();
        initializeInjector();
    }

    private void initializeInjector() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }


}
