package com.mishkun.yandextestexercise.presentation.presenters;

import android.nfc.Tag;
import android.util.Log;

import com.mishkun.yandextestexercise.di.PerActivity;
import com.mishkun.yandextestexercise.domain.entities.Definition;
import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.domain.entities.Translation;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
import com.mishkun.yandextestexercise.domain.interactors.GetSupportedLanguagesInteractor;
import com.mishkun.yandextestexercise.domain.interactors.GetTranslationDirectionInteractor;
import com.mishkun.yandextestexercise.domain.interactors.TranslationInteractor;
import com.mishkun.yandextestexercise.presentation.MutedObserver;
import com.mishkun.yandextestexercise.presentation.views.TranslateView;
import com.mishkun.yandextestexercise.presentation.views.TranslationQueryViewModel;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import dagger.Module;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Mishkun on 28.03.2017.
 */

@PerActivity
public class TranslatePresenter extends Presenter<TranslateView> {

    private static final String TAG = TranslatePresenter.class.getSimpleName();

    private final TranslationInteractor translationInteractor;
    private final GetTranslationDirectionInteractor translationDirectionInteractor;
    private final GetSupportedLanguagesInteractor supportedLanguagesInteractor;

    private TranslationDirectionMapper translationDirectionMapper;


    @Inject
    public TranslatePresenter(TranslationInteractor translationInteractor,
                              GetTranslationDirectionInteractor translationDirectionInteractor,
                              GetSupportedLanguagesInteractor supportedLanguagesInteractor) {
        this.translationInteractor = translationInteractor;
        this.translationDirectionInteractor = translationDirectionInteractor;
        this.supportedLanguagesInteractor = supportedLanguagesInteractor;
    }

    @Override
    public void resume() {
        supportedLanguagesInteractor.execute(new SupportedLanguagesObserver());
        Log.d(TAG, "onResume called");
    }


    @Override
    public void pause() {

    }


    private void setSupportedLanguages(List<Language> supportedLanguages) {
        List<String> representedLanguages = new ArrayList<>(supportedLanguages.size());
        for (Language language : supportedLanguages) {
            representedLanguages.add(language.getDisplayName());
        }
        attachedView.setSupportedLanguages(representedLanguages);
    }


    public void OnReverseTranslationButtonClicked() {
        int from = attachedView.getTranslationTo();
        int to = attachedView.getTranslationFrom();
        attachedView.setTranslationFrom(from);
        attachedView.setTranslationTo(to);
        attachedView.reverseText();
    }

    private void setTranslationString(String translationString) {
        attachedView.setTranslation(translationString);
    }

    private void setExpandedTranslationString(Definition expandedTranslationString) {
        attachedView.setExpandedTranslation(expandedTranslationString);
    }

    private void setTranslationTo(Language To) {
        attachedView.setTranslationTo(translationDirectionMapper.transform(To));
    }

    private void setTranslationFrom(Language From) {
        attachedView.setTranslationTo(translationDirectionMapper.transform(From));
    }

    private final class UserInputObserver extends MutedObserver<TranslationQueryViewModel> {
        private final String TAG = UserInputObserver.class.getSimpleName();

        @Override
        public void onNext(TranslationQueryViewModel value) {
            String queryString = value.getQuery();

            TranslationDirection direction = new TranslationDirection(translationDirectionMapper.transform(value.getTranslationFrom()),
                                                                      translationDirectionMapper.transform(value.getTranslationTo()));
            TranslationInteractor.TranslationQuery query = new TranslationInteractor.TranslationQuery(queryString, direction, true);
            translationInteractor.execute(new TranslationObserver(), query);
        }
    }

    private final class TranslationDirectionObserver extends MutedObserver<TranslationDirection> {

        private final String TAG = TranslationDirectionObserver.class.getSimpleName();

        @Override
        public void onNext(TranslationDirection translationDirection) {
            setTranslationTo(translationDirection.getTranslationTo());
            setTranslationFrom(translationDirection.getTranslationFrom());
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private final class SupportedLanguagesObserver extends MutedObserver<List<Language>> {

        private final String TAG = SupportedLanguagesObserver.class.getSimpleName();

        @Override
        public void onNext(List<Language> value) {
            Log.d(TAG, "SupportedLanguagesList arrived");
            TranslatePresenter.this.translationDirectionMapper = new TranslationDirectionMapper(value);
            setSupportedLanguages(value);
            translationDirectionInteractor.execute(new TranslationDirectionObserver());
            attachedView.getQueries().distinctUntilChanged().subscribe(new UserInputObserver());
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private final class TranslationObserver extends MutedObserver<Translation> {
        private final String TAG = TranslationObserver.class.getSimpleName();

        @Override
        public void onNext(Translation value) {
            TranslatePresenter.this.setTranslationString(value.getShortTranslation());
            TranslatePresenter.this.setExpandedTranslationString(value.getExpandedTranslation());
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, e.getMessage());
        }
    }

}
