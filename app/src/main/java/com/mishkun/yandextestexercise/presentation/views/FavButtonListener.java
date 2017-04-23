package com.mishkun.yandextestexercise.presentation.views;

import com.mishkun.yandextestexercise.domain.entities.ShortTranslationModel;

/**
 * Created by Mishkun on 21.04.2017.
 */

public interface FavButtonListener {
    void favButtonChecked(ShortTranslationModel item, boolean favored);
}
