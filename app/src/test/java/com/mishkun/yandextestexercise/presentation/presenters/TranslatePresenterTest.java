package com.mishkun.yandextestexercise.presentation.presenters;

import com.mishkun.yandextestexercise.domain.interactors.GetSupportedLanguagesInteractor;
import com.mishkun.yandextestexercise.domain.interactors.TranslationInteractor;
import com.mishkun.yandextestexercise.presentation.views.TranslateView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Created by Mishkun on 14.04.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TranslatePresenterTest {

    private TranslatePresenter presenter;

    @Mock
    private TranslationInteractor translationInteractor;

    @Mock
    private GetSupportedLanguagesInteractor supportedLanguagesInteractor;

    @Mock
    private TranslateView translationView;

    @Before
    public void setUp() throws Exception {
     presenter.attachView(translationView);
    }
}