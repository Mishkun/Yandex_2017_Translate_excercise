package com.mishkun.yandextestexercise.presentation.presenters;

import android.util.Log;

import com.mishkun.yandextestexercise.domain.entities.Definition;
import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.domain.entities.ShortTranslationModel;
import com.mishkun.yandextestexercise.domain.entities.Translation;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
import com.mishkun.yandextestexercise.domain.entities.TranslationQuery;
import com.mishkun.yandextestexercise.domain.interactors.AddEditHistoryInteractor;
import com.mishkun.yandextestexercise.domain.interactors.DeleteHistoryItemInteractor;
import com.mishkun.yandextestexercise.domain.interactors.GetHistoryInteractor;
import com.mishkun.yandextestexercise.domain.interactors.GetSupportedLanguagesInteractor;
import com.mishkun.yandextestexercise.domain.interactors.GetTranslationDirectionInteractor;
import com.mishkun.yandextestexercise.domain.interactors.TranslationInteractor;
import com.mishkun.yandextestexercise.presentation.Presenter;
import com.mishkun.yandextestexercise.presentation.views.TranslateView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mishkun on 28.03.2017.
 */


public class TranslatePresenter extends Presenter<TranslateView> {

    private static final String TAG = TranslatePresenter.class.getSimpleName();

    private final TranslationInteractor translationInteractor;
    private final GetTranslationDirectionInteractor translationDirectionInteractor;
    private final GetSupportedLanguagesInteractor supportedLanguagesInteractor;
    private final AddEditHistoryInteractor addEditHistoryInteractor;
    private final GetHistoryInteractor getHistoryInteractor;
    private final DeleteHistoryItemInteractor deleteHistoryItemInteractor;


    @Inject
    public TranslatePresenter(TranslationInteractor translationInteractor,
                              GetTranslationDirectionInteractor translationDirectionInteractor,
                              GetSupportedLanguagesInteractor supportedLanguagesInteractor,
                              AddEditHistoryInteractor addEditHistoryInteractor,
                              GetHistoryInteractor getHistoryInteractor, DeleteHistoryItemInteractor deleteHistoryItemInteractor) {
        this.translationInteractor = translationInteractor;
        this.translationDirectionInteractor = translationDirectionInteractor;
        this.supportedLanguagesInteractor = supportedLanguagesInteractor;
        this.addEditHistoryInteractor = addEditHistoryInteractor;
        this.getHistoryInteractor = getHistoryInteractor;
        this.deleteHistoryItemInteractor = deleteHistoryItemInteractor;
    }

    @Override
    public void resume() {
        supportedLanguagesInteractor.execute(new SupportedLanguagesObserver());
        getHistory();
        Log.d(TAG, "onResume called");
    }


    @Override
    public void pause() {

    }


    private void setSupportedLanguages(List<Language> supportedLanguages) {

        attachedView.setSupportedLanguages(supportedLanguages);
    }


    public void OnReverseTranslationButtonClicked() {
        Language from = attachedView.getTranslationTo();
        Language to = attachedView.getTranslationFrom();
        attachedView.setTranslationFrom(from);
        attachedView.setTranslationTo(to);
        attachedView.reverseText();
    }

    public void getHistory() {
        getHistoryInteractor.execute(new HistoryObserver(attachedView));
    }

    private void setTranslationString(String translationString) {
        attachedView.setTranslation(translationString);
    }

    private void setExpandedTranslationString(Definition expandedTranslationString) {
        attachedView.setExpandedTranslation(expandedTranslationString);
    }

    private void setFavored(boolean favored) {
        attachedView.setIsCurrentTranslationFavored(favored);
    }

    private void setTranslationTo(Language To) {
        attachedView.setTranslationTo(To);
    }

    private void setTranslationFrom(Language From) {
        attachedView.setTranslationFrom(From);
    }

    public void onFavored(ShortTranslationModel item, boolean favored) {
        item.setFavored(favored);
        addEditHistoryInteractor.execute(new MutedObserver<Void>(), item);
    }

    public void onHistoryItemDismissed(ShortTranslationModel shortTranslationModel) {
        deleteHistoryItemInteractor.execute(new MutedObserver<Void>(), shortTranslationModel);
    }

    public void addHistoryItem(ShortTranslationModel shortTranslationModel) {
        addEditHistoryInteractor.execute(new MutedObserver<Void>(), shortTranslationModel);
    }


    private final class UserInputObserver extends MutedObserver<TranslationQuery> {
        private final String TAG = UserInputObserver.class.getSimpleName();

        @Override
        public void onNext(TranslationQuery value) {
            translationInteractor.execute(new TranslationObserver(), value);

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
            setSupportedLanguages(value);
            attachedView.getQueries().distinctUntilChanged().subscribe(new UserInputObserver());
            translationDirectionInteractor.execute(new TranslationDirectionObserver());
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private final class TranslationObserver extends MutedObserver<Translation> {
        private final String TAG = TranslationObserver.class.getSimpleName();

        @Override
        protected void onStart() {
            super.onStart();
            attachedView.showProgressBar();
        }

        @Override
        public void onNext(Translation value) {
            attachedView.hideProgressBar();
            TranslatePresenter.this.setFavored(value.isFavored());
            TranslatePresenter.this.setTranslationString(value.getShortTranslation());
            TranslatePresenter.this.setExpandedTranslationString(value.getExpandedTranslation());
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, "ERROR:" + e.getClass() + " message:" + e.getMessage() + "\n" + Log.getStackTraceString(e));
        }
    }

}
