package com.mishkun.yandextestexercise.domain.entities;

import android.support.annotation.NonNull;

/**
 * Created by Mishkun on 28.03.2017.
 */

public class Language  {
    private String code;
    private String displayName;

    public Language(String code, String name) {
        this.code = code;
        displayName = name;
    }


    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Language language = (Language) obj;

        return code.equals(language.getCode());

    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

}
