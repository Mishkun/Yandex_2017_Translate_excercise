package com.mishkun.yandextestexercise.data.mappers;

import com.mishkun.yandextestexercise.data.ShortTranslationEntity;
import com.mishkun.yandextestexercise.domain.entities.HistoryItem;

/**
 * Created by Mishkun on 21.04.2017.
 */

public class ShortTranslationEntityMapper {
    public static HistoryItem transform(ShortTranslationEntity shortTranslationEntity){
        return new HistoryItem(shortTranslationEntity.getTranslation(), shortTranslationEntity.getOriginal(), shortTranslationEntity.isFavored());
    }
}
