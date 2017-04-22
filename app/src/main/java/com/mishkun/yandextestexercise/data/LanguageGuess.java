package com.mishkun.yandextestexercise.data;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Key;
import io.requery.OneToOne;
import io.requery.Persistable;

/**
 * Created by Mishkun on 21.04.2017.
 */

@Entity
public interface LanguageGuess extends Persistable{
    @Key
    String getQuery();

    String getLanguage();
}
