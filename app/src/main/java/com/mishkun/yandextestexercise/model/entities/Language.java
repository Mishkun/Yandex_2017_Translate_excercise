package com.mishkun.yandextestexercise.model.entities;

/**
 * Created by Mishkun on 28.03.2017.
 */

public class Language {
    private String mCode;
    private String mDisplayName;

    public Language(String code, String name) {
        mCode = code;
        mDisplayName = name;
    }


    public String getCode() {
        return mCode;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Language language = (Language) o;

        return mCode.equals(language.mCode);

    }

    @Override
    public int hashCode() {
        return mCode.hashCode();
    }
}
