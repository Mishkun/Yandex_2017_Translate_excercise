package com.mishkun.yandextestexercise.domain.providers;

import com.mishkun.yandextestexercise.domain.entities.ShortTranslationModel;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;

import io.reactivex.Observable;

/**
 * Created by Mishkun on 15.04.2017.
 */

public interface ShortTranslationProvider {
    Observable<ShortTranslationModel> getShortTranslation(String query, TranslationDirection direction);
}
