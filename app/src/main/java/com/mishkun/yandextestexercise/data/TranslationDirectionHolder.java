package com.mishkun.yandextestexercise.data;

import android.content.Context;

import com.mishkun.yandextestexercise.R;
import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
import com.mishkun.yandextestexercise.domain.providers.TranslationDirectionProvider;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class TranslationDirectionHolder implements TranslationDirectionProvider {


    private BehaviorSubject<TranslationDirection> translationDirectionBehaviorSubject;

    @Inject
    public TranslationDirectionHolder(Context context) {
        TranslationDirection defaultTranslationDrection = new TranslationDirection(
                new Language(context.getResources().getString(R.string.language_from), null),
                new Language(context.getResources().getString(R.string.language_to), null));
        translationDirectionBehaviorSubject = BehaviorSubject.createDefault(defaultTranslationDrection);
    }

    @Override
    public Observable<TranslationDirection> getTranslationDirection() {
        return translationDirectionBehaviorSubject;
    }

    @Override
    public void setTranslationDirection(TranslationDirection translationDirection) {
        translationDirectionBehaviorSubject.onNext(translationDirection);
    }
}
