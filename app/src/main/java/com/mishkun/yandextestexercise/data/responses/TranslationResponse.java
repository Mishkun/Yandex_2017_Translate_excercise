package com.mishkun.yandextestexercise.data.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mishkun on 17.04.2017.
 */

public class TranslationResponse {

    @SerializedName("text")
    private String translation;

    public String getTranslation() {
        return translation;
    }
}
