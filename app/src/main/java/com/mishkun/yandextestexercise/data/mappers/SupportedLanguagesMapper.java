package com.mishkun.yandextestexercise.data.mappers;

import com.mishkun.yandextestexercise.data.LanguageEntity;
import com.mishkun.yandextestexercise.data.responses.SupportedLanguagesResponse;
import com.mishkun.yandextestexercise.domain.entities.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mishkun on 17.04.2017.
 */

public class SupportedLanguagesMapper {

    public static List<Language> transform(SupportedLanguagesResponse response) {
        Map<String, String> languageMap = response.getLanguages();
        List<Language> languages = new ArrayList<>();
        for (String keycode : languageMap.keySet()) {
            languages.add(new Language(keycode, languageMap.get(keycode)));
        }
        return languages;
    }

    public static Language transform(LanguageEntity languageEntity) {
        return new Language(languageEntity.getKeyCode(), languageEntity.getDisplayName());
    }

    public static LanguageEntity transform(Language language) {
        LanguageEntity entity = new LanguageEntity();
        entity.setKeyCode(language.getCode());
        entity.setDisplayName(language.getDisplayName());
        return entity;
    }

    public static  List<LanguageEntity> transform(List<Language> languages){
        List<LanguageEntity> languageEntities = new ArrayList<>(languages.size());
        for (Language language: languages){
            languageEntities.add(transform(language));
        }
        return languageEntities;
    }
}
