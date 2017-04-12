package com.mishkun.yandextestexercise.model.interactors;

import io.reactivex.Flowable;

/**
 * Created by Mishkun on 12.04.2017.
 */

public class TranslationInteractor {
    public Flowable<String> getTranslation(String query){
        return Flowable.just(query);
    }
}
