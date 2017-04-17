package com.mishkun.yandextestexercise.data.responses;

import android.util.ArrayMap;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by Mishkun on 17.04.2017.
 */

public class SupportedLanguagesResponse {
    @SerializedName("langs")
    private ArrayMap<String, String> languages;

    public Map<String, String> getLanguages() {
        return languages;
    }
}
