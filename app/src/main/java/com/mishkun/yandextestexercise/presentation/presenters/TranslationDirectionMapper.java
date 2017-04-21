package com.mishkun.yandextestexercise.presentation.presenters;

import android.util.Log;

import com.mishkun.yandextestexercise.domain.entities.Language;

import java.util.List;

/**
 * Created by Mishkun on 16.04.2017.
 */

class TranslationDirectionMapper {
private static final String TAG = TranslationDirectionMapper.class.getSimpleName();
    private List<Language> supportedLanguages;


    TranslationDirectionMapper(List<Language> supportedLanguages) {

        Log.d(TAG, "Constructor: " + supportedLanguages);
        this.supportedLanguages = supportedLanguages;
    }

    public Language transform(int position){
        return supportedLanguages.get(position);
    }

    public int transform(Language language){
        Log.d(TAG, "transform: " + language.getCode());
        return supportedLanguages.indexOf(language);
    }
}
