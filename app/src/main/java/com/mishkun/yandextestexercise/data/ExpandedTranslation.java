package com.mishkun.yandextestexercise.data;

import java.util.List;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.OneToMany;
import io.requery.Persistable;

/**
 * Created by Mishkun on 21.04.2017.
 */
@Entity
public interface ExpandedTranslation extends Persistable{
    @Key
    @Generated
    int getId();

    String getOriginal();

    String getTranslation();

    String getTranscription();

    @OneToMany
    List<DefinitionItem> getDefinitions();
}
