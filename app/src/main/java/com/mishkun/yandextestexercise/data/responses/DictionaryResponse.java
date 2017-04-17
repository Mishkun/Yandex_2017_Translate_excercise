package com.mishkun.yandextestexercise.data.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mishkun on 17.04.2017.
 */

public class DictionaryResponse {
    @SerializedName("def")
    public List<DefinitionResponse> definitions;

    public static class DefinitionResponse {
        @SerializedName("text")
        public String original;
        @SerializedName("tr")
        public List<TranslationDefinitionResponse> translationDefinitionResponses;
        @SerializedName("pos")
        public String partOfSpeech;
        @SerializedName("ts")
        public String transcription;
    }

    public static class TranslationDefinitionResponse {
        @SerializedName("text")
        public String translation;
        @SerializedName("syn")
        public List<TextResponse> synonyms;
        @SerializedName("mean")
        public List<TextResponse> meanings;
    }

    public static class TextResponse {
        @SerializedName("text")
        public String text;
    }
}
