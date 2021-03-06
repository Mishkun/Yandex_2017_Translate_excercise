package com.mishkun.yandextestexercise.domain.interactors;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Mishkun on 15.04.2017.
 */

public abstract class Interactor<R, P> {
    protected final Scheduler jobScheduler;
    private final CompositeDisposable subscriptions;
    private final Scheduler uiScheduler;


    Interactor(Scheduler threadExecutor, Scheduler postExecutionThread) {
        this.jobScheduler = threadExecutor;
        this.uiScheduler = postExecutionThread;
        this.subscriptions = new CompositeDisposable();
    }


    abstract Observable<R> buildUseCaseObservable(P params);

    public void execute(DisposableObserver<R> observer, P params) {
        final Observable<R> observable = this.buildUseCaseObservable(params)
                                             .subscribeOn(jobScheduler)
                                             .observeOn(uiScheduler);
        addSubscription(observable.subscribeWith(observer));
    }

    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    public void dispose() {
        if (!subscriptions.isDisposed()) {
            subscriptions.dispose();
        }
    }

    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    private void addSubscription(Disposable disposable) {
        subscriptions.add(disposable);
    }
}

