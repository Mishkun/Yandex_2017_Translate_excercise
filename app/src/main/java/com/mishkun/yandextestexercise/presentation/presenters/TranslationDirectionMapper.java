package com.mishkun.yandextestexercise.presentation.presenters;

import com.mishkun.yandextestexercise.domain.entities.Language;

import java.util.List;

/**
 * Created by Mishkun on 16.04.2017.
 */

class TranslationDirectionMapper {

    private List<Language> supportedLanguages;

    TranslationDirectionMapper(List<Language> supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    public Language transform(int position){
        return supportedLanguages.get(position);
    }

    public int transform(Language language){
        return supportedLanguages.indexOf(language);
    }
}
