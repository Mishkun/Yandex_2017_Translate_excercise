package com.mishkun.yandextestexercise.presentation.presenters;

/**
 * Created by Mishkun on 11.04.2017.
 */

public abstract class Presenter<V> {
    protected V attachedView;

    public void attachView(V view) {
        attachedView = view;
    }

    public abstract void resume();

    public abstract void pause();

    public void detachView() {
        attachedView = null;
    }
}

