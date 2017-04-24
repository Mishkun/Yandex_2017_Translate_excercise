package com.mishkun.yandextestexercise.data.mappers.entity;

import com.mishkun.yandextestexercise.data.LanguageEntity;
import com.mishkun.yandextestexercise.domain.entities.Language;

import java.util.ArrayList;
import java.util.List;

public class SupportedLanguagesEntityMapper {
    public static Language transform(LanguageEntity languageEntity) {
        return new Language(languageEntity.getKeyCode(), languageEntity.getDisplayName());
    }

    public static LanguageEntity transform(Language language) {
        LanguageEntity entity = new LanguageEntity();
        entity.setKeyCode(language.getCode());
        entity.setDisplayName(language.getDisplayName());
        return entity;
    }

    public static List<LanguageEntity> transform(List<Language> languages) {
        List<LanguageEntity> languageEntities = new ArrayList<LanguageEntity>(languages.size());
        for (Language language : languages) {
            languageEntities.add(transform(language));
        }
        return languageEntities;
    }
}