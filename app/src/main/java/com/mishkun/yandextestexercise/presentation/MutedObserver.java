package com.mishkun.yandextestexercise.presentation;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class MutedObserver<T> extends DisposableObserver<T> {
    @Override
    public void onNext(T value) {
        
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
