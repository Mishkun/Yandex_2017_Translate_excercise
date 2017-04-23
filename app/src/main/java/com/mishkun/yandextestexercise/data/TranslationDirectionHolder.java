package com.mishkun.yandextestexercise.data;

import android.content.Context;
import android.util.Log;

import com.mishkun.yandextestexercise.R;
import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
import com.mishkun.yandextestexercise.domain.providers.TranslationDirectionProvider;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class TranslationDirectionHolder implements TranslationDirectionProvider {
    private static final String TAG = TranslationDirectionHolder.class.getSimpleName();

    private PublishSubject<TranslationDirection> translationDirectionBehaviorSubject;

    @Inject
    public TranslationDirectionHolder() {
        translationDirectionBehaviorSubject = PublishSubject.create();
    }

    @Override
    public Observable<TranslationDirection> getTranslationDirection() {
        return translationDirectionBehaviorSubject;
    }

    @Override
    public void setTranslationDirection(TranslationDirection translationDirection) {
        // Log.d(TAG, translationDirection.getTranslationFrom().getCode() + " " + translationDirection.getTranslationTo().getCode());
        translationDirectionBehaviorSubject.onNext(translationDirection);
    }
}
