package com.mishkun.yandextestexercise.data;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;

/**
 * Created by Mishkun on 21.04.2017.
 */
@Entity
public interface ShortTranslation extends Persistable {
    @Key
    @Generated
    int getId();

    String getOriginal();

    String getTranslation();

    String getDirectionTo();

    String getDirectionFrom();


    boolean isSaved();

    boolean isFavored();

}
