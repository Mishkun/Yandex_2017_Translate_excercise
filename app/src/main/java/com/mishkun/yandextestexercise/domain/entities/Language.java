package com.mishkun.yandextestexercise.domain.entities;

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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Language language = (Language) obj;

        return mCode.equals(language.mCode);

    }

    @Override
    public int hashCode() {
        return mCode.hashCode();
    }
}
