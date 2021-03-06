package com.mishkun.yandextestexercise.data.mappers.response;

import com.mishkun.yandextestexercise.data.responses.SupportedLanguagesResponse;
import com.mishkun.yandextestexercise.domain.entities.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mishkun on 17.04.2017.
 */

public class SupportedLanguagesResponseMapper {

    public static List<Language> transform(SupportedLanguagesResponse response) {
        Map<String, String> languageMap = response.getLanguages();
        List<Language> languages = new ArrayList<>();
        for (String keycode : languageMap.keySet()) {
            languages.add(new Language(keycode, languageMap.get(keycode)));
        }
        return languages;
    }

}
