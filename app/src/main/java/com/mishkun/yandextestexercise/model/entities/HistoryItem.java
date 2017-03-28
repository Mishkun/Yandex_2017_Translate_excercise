package com.mishkun.yandextestexercise.model.entities;

/**
 * Created by Mishkun on 28.03.2017.
 */


public class HistoryItem {
    private String mOriginalText;
    private String mTranslatedText;
    private String mFavored;

    public HistoryItem() {

    }

    public HistoryItem(String mOriginalText, String mTranslatedText, String mFavored) {
        this.mOriginalText = mOriginalText;
        this.mTranslatedText = mTranslatedText;
        this.mFavored = mFavored;
    }


    public String getOriginalText() {
        return mOriginalText;
    }

    public void setOriginalText(String originalText) {
        this.mOriginalText = originalText;
    }

    public String getmTranslatedText() {
        return mTranslatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.mTranslatedText = translatedText;
    }

    public String getFavored() {
        return mFavored;
    }

    public void setFavored(String favored) {
        this.mFavored = favored;
    }
}
