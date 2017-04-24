package com.mishkun.yandextestexercise.data.mappers.entity;

import com.mishkun.yandextestexercise.data.ShortTranslationEntity;
import com.mishkun.yandextestexercise.domain.entities.ShortTranslationModel;
import com.mishkun.yandextestexercise.domain.entities.Language;

import io.reactivex.functions.Function;

/**
 * Created by Mishkun on 21.04.2017.
 */

public class ShortTranslationEntityMapper implements Function<ShortTranslationEntity, ShortTranslationModel> {
    @Override
    public ShortTranslationModel apply(ShortTranslationEntity shortTranslationEntity){
        return new ShortTranslationModel(shortTranslationEntity.getOriginal(), shortTranslationEntity.getTranslation(), shortTranslationEntity.isFavored(),
                                         new Language(shortTranslationEntity.getDirectionFrom(), null), new Language(shortTranslationEntity.getDirectionTo(), null));
    }
}
