package com.mishkun.yandextestexercise.data;

import java.util.List;

import io.requery.Generated;
import io.requery.Key;
import io.requery.OneToMany;
import io.requery.Persistable;

/**
 * Created by Mishkun on 21.04.2017.
 */

public interface DefinitionItem extends Persistable {
    @Key
    @Generated
    int getId();

    @OneToMany
    List<String> getSynonyms();

    @OneToMany
    List<String> getMeanings();
}
