package com.mishkun.yandextestexercise.presentation.views;

import com.mishkun.yandextestexercise.domain.entities.ShortTranslationModel;

import java.util.List;

/**
 * Created by Mishkun on 28.03.2017.
 */

public interface HistoryView {
    void setData(List<ShortTranslationModel> shortTranslationModels);
}
