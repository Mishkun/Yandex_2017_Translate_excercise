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
        presenter = new TranslatePresenter(translationInteractor, translationDirectionInteractor, supportedLanguagesInteractor,
                                           swapTranslationDirectionInteractor);

        presenter.attachView(translationView);
    }
/*
    @Test
    public void onReverseTranslationButtonClicked() throws Exception {

    }

    @Test
    public void onToDirectionButtonClicked() throws Exception {

    }

    @Test
    public void onFromDirectionButtonClicked() throws Exception {

    }

    @Test
    public void onFavoriteButtonClicked() throws Exception {

    }

*/
    @Test
    public void shouldAttachView() throws Exception{
        assertEquals(presenter.attachedView, translationView);
    }

    @Test
    public void shouldSetTranslationFrom() throws Exception {
        int testNumber = 1337;

        presenter.setTranslationTo(testNumber);
        verify(translationView).setTranslationTo(testNumber);
    }

    @Test
    public void shouldSetTranslateString() throws Exception {
        String testString = "test";

        presenter.setTranslationString(testString);
        verify(translationView).setTranslation(testString);
    }



    @Test
    public void shouldSetExpandedTranslateString() throws Exception {
        String testString = "test";

        presenter.setExpandedTranslationString(testString);
        verify(translationView).setExpandedTranslation(testString);
    }
/*
    @Test
    public void shouldTranslateString() throws Exception{
        String testString = "test";
        TranslationViewModel translationViewModel = new TranslationViewModel(testString,testString+testString);

        when(translationView.getTextToTranslateStream()).thenReturn(Observable.just(testString));
        when(translationInteractor.getTranslation(anyString())).thenReturn(Observable.just(testString));
        presenter.subscribeToUserTextInput();
        verify(translationView).setTranslation(testString);
        verify(translationView).setExpandedTranslation(testString);
    }
*/
}