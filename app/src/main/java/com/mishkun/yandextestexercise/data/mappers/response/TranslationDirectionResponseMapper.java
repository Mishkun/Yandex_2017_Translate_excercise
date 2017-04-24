package com.mishkun.yandextestexercise.data.mappers.response;

import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;

/**
 * Created by Mishkun on 17.04.2017.
 */

public class TranslationDirectionResponseMapper {
    public static String transform(TranslationDirection translationDirection) {
        return translationDirection.getTranslationFrom().getCode() + "-" + translationDirection.getTranslationTo().getCode();
    }

    public static TranslationDirection transform(String direction) {
        String[] split = direction.split("-");
        return new TranslationDirection(new Language(split[0], null), new Language(split[1], null));
    }
}