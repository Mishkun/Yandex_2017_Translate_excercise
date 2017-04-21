package com.mishkun.yandextestexercise.di.modules;

import android.content.Context;

import com.mishkun.yandextestexercise.BuildConfig;
import com.mishkun.yandextestexercise.data.Models;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;

/**
 * Created by Mishkun on 21.04.2017.
 */
@Module
public class DbModule {

    @Singleton
    @Provides
    ReactiveEntityStore<Persistable> provideReactiveEntityStore(Context context) {
        DatabaseSource source = new DatabaseSource(context, Models.DEFAULT, 1);
        if (BuildConfig.DEBUG) {
            // use this in development mode to drop and recreate the tables on every upgrade
            source.setTableCreationMode(TableCreationMode.DROP_CREATE);
        }
        Configuration configuration = source.getConfiguration();
        return ReactiveSupport.toReactiveStore(
                new EntityDataStore<Persistable>(configuration));
    }
}
