package com.mishkun.yandextestexercise.domain.providers;

import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Mishkun on 19.04.2017.
 */

public interface DictionarySupportedLanguagesProvider {
     Observable<List<TranslationDirection>> getSupportedLanguages();
}
