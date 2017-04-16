package com.mishkun.yandextestexercise.domain.providers;

import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;

import io.reactivex.Observable;

/**
 * Created by Mishkun on 15.04.2017.
 */

public interface ExpandedTranslationProvider {
    // TODO - look how actually translation is provided
    Observable<String> getExpandedTranslation(String query, TranslationDirection direction);
}
