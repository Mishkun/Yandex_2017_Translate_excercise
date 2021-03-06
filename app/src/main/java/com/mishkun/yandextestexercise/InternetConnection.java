package com.mishkun.yandextestexercise;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Mishkun on 21.04.2017.
 */

public class InternetConnection {
    private final int maxRetryLimit = 5;
    private Context context;
    private BroadcastReceiver broadcastReceiver;
    private PublishSubject<Boolean> internetStatusHotObservable;
    private int delayBetweenRetry = 100;
    private int currentRepeatCount = 1;

    public InternetConnection(Context context) {
        this.context = context;
    }

    public Observable<Boolean> isInternetOnObservable() {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return isInternetOn();
            }
        });
    }

    public boolean isInternetOn() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Observable<Boolean> getInternetStatusHotObservable() {
        internetStatusHotObservable = PublishSubject.create();
        return internetStatusHotObservable.serialize();
    }

    /* Register for Internet connection change broadcast receiver */
    public void registerBroadCastReceiver() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                NetworkInfo info = extras.getParcelable("networkInfo");
                assert info != null;
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    checkForWorkingInternetConnection();
                } else {
                    internetStatusHotObservable.onNext(isInternetOn());
                }
            }
        };

        context.registerReceiver(broadcastReceiver, filter);
    }

    /* It takes a few milliseconds, from the connection is on
     * to we get an active working internet connection. */
    private void checkForWorkingInternetConnection() {
        currentRepeatCount = 1;
        delayBetweenRetry = 100;

        isInternetOnObservable().repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
                return objectObservable.flatMap(new Function<Object, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Object o) throws Exception {
                        if (currentRepeatCount >= maxRetryLimit) {
                            return Observable.empty();
                        }
                        currentRepeatCount++;
                        delayBetweenRetry += 300;
                        return Observable.timer(delayBetweenRetry, TimeUnit.MILLISECONDS);
                    }
                });
            }
        }).filter(new Predicate<Boolean>() {
            @Override
            public boolean test(Boolean connectionStatus) {
                return connectionStatus;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean connectionStatus) {
                InternetConnection.this.currentRepeatCount = InternetConnection.this.maxRetryLimit;
                InternetConnection.this.internetStatusHotObservable.onNext(InternetConnection.this.isInternetOn());
            }
        });
    }

    /* unRegister for Internet connection change broadcast receiver */
    public void unRegisterBroadCastReceiver() {
        context.unregisterReceiver(broadcastReceiver);
    }
}
