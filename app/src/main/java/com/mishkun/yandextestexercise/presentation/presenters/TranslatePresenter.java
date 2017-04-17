package com.mishkun.yandextestexercise.presentation.presenters;

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


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

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
        translationDirectionInteractor.execute(new TranslationDirectionObserver());
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
        attachedView.setTranslationTo(to);
        attachedView.setTranslationFrom(from);
    }

    public void onQueryChanged() {
        translate();
    }

    private void translate() {
        String queryString = attachedView.getTextToTranslate();
        TranslationDirection direction = new TranslationDirection(translationDirectionMapper.transform(attachedView.getTranslationFrom()),
                                                                  translationDirectionMapper.transform(attachedView.getTranslationTo()));
        TranslationInteractor.TranslationQuery query = new TranslationInteractor.TranslationQuery(queryString, direction, true);
        translationInteractor.execute(new TranslationObserver(), query);
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

    private final class TranslationDirectionObserver extends MutedObserver<TranslationDirection> {

        @Override
        public void onNext(TranslationDirection translationDirection) {
            setTranslationTo(translationDirection.getTranslationTo());
            setTranslationFrom(translationDirection.getTranslationFrom());
        }
    }

    private final class SupportedLanguagesObserver extends MutedObserver<List<Language>> {

        @Override
        public void onNext(List<Language> value) {
            TranslatePresenter.this.translationDirectionMapper = new TranslationDirectionMapper(value);
            setSupportedLanguages(value);
        }
    }

    private final class TranslationObserver extends MutedObserver<Translation> {

        @Override
        public void onNext(Translation value) {
            TranslatePresenter.this.setTranslationFrom(value.getDirection().getTranslationFrom());
            TranslatePresenter.this.setTranslationTo(value.getDirection().getTranslationTo());
            TranslatePresenter.this.setTranslationString(value.getShortTranslation());
            TranslatePresenter.this.setExpandedTranslationString(value.getExpandedTranslation());
        }
    }

}
