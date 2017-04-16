package com.mishkun.yandextestexercise.presentation.presenters;

import com.mishkun.yandextestexercise.domain.interactors.SupportedLanguagesInteractor;
import com.mishkun.yandextestexercise.domain.interactors.TranslationInteractor;
import com.mishkun.yandextestexercise.presentation.viewmodels.TranslationViewModel;
import com.mishkun.yandextestexercise.presentation.views.TranslateView;
import com.mishkun.yandextestexercise.presentation.views.fragments.HomeFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mishkun on 14.04.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TranslatePresenterTest {

    private TranslatePresenter presenter;

    @Mock
    private TranslationInteractor translationInteractor;

    @Mock
    private SupportedLanguagesInteractor supportedLanguagesInteractor;

    @Mock
    private TranslateView translationView;

    @Before
    public void setUp() throws Exception {
        presenter = new TranslatePresenter(translationInteractor, supportedLanguagesInteractor);

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