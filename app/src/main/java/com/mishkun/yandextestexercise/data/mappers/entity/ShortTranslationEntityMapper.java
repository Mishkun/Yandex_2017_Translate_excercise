package com.mishkun.yandextestexercise.data.mappers.entity;

import com.mishkun.yandextestexercise.data.ShortTranslationEntity;
import com.mishkun.yandextestexercise.domain.entities.ShortTranslationModel;
import com.mishkun.yandextestexercise.domain.entities.Language;

/**
 * Created by Mishkun on 21.04.2017.
 */

public class ShortTranslationEntityMapper {
    public static ShortTranslationModel transform(ShortTranslationEntity shortTranslationEntity){
        return new ShortTranslationModel(shortTranslationEntity.getTranslation(), shortTranslationEntity.getOriginal(), shortTranslationEntity.isFavored(),
                                         new Language(shortTranslationEntity.getDirectionFrom(), null), new Language(shortTranslationEntity.getDirectionTo(), null));
    }
}
