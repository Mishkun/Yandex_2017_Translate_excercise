package com.mishkun.yandextestexercise.domain.providers;

import com.mishkun.yandextestexercise.domain.entities.Language;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Mishkun on 15.04.2017.
 */

public interface SupportedLanguagesProvider {
    Observable<List<Language>> getSupportedLanguages();
}
