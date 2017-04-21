package com.mishkun.yandextestexercise.data;

import com.mishkun.yandextestexercise.ListStringsConverter;

import java.util.ArrayList;
import java.util.List;

import io.requery.Convert;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToMany;
import io.requery.ManyToOne;
import io.requery.OneToMany;
import io.requery.Persistable;

/**
 * Created by Mishkun on 21.04.2017.
 */
@Entity
public interface DefinitionItem extends Persistable {
    @Key
    @Generated
    int getId();


    @ManyToOne
    ExpandedTranslationEntity getOwner();

    @Convert(ListStringsConverter.class)
    ArrayList<String> getSynonyms();

    @Convert(ListStringsConverter.class)
    ArrayList<String> getMeanings();
}
