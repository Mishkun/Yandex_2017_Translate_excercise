package com.mishkun.yandextestexercise.data.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mishkun on 17.04.2017.
 */

public class TranslationResponse {

    @SerializedName("text")
    private List<String> translations;

    public TranslationResponse() {
    }

    public TranslationResponse(List<String> translations) {
        this.translations = translations;
    }

    public List<String> getTranslation() {
        return translations;
    }
}

