package com.mishkun.yandextestexercise.presentation.views;

import com.mishkun.yandextestexercise.domain.entities.HistoryItem;

/**
 * Created by Mishkun on 21.04.2017.
 */

public interface FavButtonListener {
    void favButtonChecked(HistoryItem item, boolean favored);
}
