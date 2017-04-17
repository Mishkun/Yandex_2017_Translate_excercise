package com.mishkun.yandextestexercise.data.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mishkun on 17.04.2017.
 */

public class DetectionResponse {
    @SerializedName("lang")
    private String languageCode;

    public String getLanguageCode() {
        return languageCode;
    }
}
