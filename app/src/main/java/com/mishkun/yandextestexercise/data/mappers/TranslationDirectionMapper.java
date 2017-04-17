package com.mishkun.yandextestexercise.data.mappers;

import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;

/**
 * Created by Mishkun on 17.04.2017.
 */

public class TranslationDirectionMapper {
    public static String transform(TranslationDirection translationDirection) {
        return translationDirection.getTranslationFrom().getCode() + "-" + translationDirection.getTranslationTo().getCode();
    }

    public static TranslationDirection transform(String direction) {
        return new TranslationDirection(new Language(direction.substring(0, 2), null), new Language(direction.substring(3, 5), null));
    }
}