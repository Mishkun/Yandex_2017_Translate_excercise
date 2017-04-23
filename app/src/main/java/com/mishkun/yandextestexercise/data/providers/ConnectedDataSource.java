package com.mishkun.yandextestexercise.data.providers;

import com.mishkun.yandextestexercise.InternetConnection;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by Mishkun on 23.04.2017.
 */

public abstract class ConnectedDataSource {

    protected final InternetConnection internetConnection;

    protected ConnectedDataSource(InternetConnection internetConnection) {
        this.internetConnection = internetConnection;
    }

    <T> Observable<T> collectFromSources(Observable<T> getFromApi, Observable<T> getFromDatabase, Observable<T> getDefaultValue) {
        return Observable.concat(getFromDatabase, getFromApi, getDefaultValue)
                         .filter(new Predicate<T>() {
                             @Override
                             public boolean test(T t) throws Exception {
                                 return t != null;
                             }
                         })
                         .firstElement()
                         .toObservable();
    }

    <T> Observable<T> getIfInternet(final Observable<T> observable) {
        return internetConnection.isInternetOnObservable().switchMap(new Function<Boolean, ObservableSource<T>>() {
            @Override
            public ObservableSource<T> apply(Boolean isInternetOn) throws Exception {
                if (isInternetOn) {
                    return observable;
                } else {
                    return Observable.empty();
                }
            }
        });
    }
}
