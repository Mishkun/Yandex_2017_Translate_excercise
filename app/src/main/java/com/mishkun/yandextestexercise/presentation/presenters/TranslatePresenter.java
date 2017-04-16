package com.mishkun.yandextestexercise.presentation.presenters;

import com.mishkun.yandextestexercise.di.PerActivity;
import com.mishkun.yandextestexercise.domain.interactors.GetSupportedLanguagesInteractor;
import com.mishkun.yandextestexercise.domain.interactors.TranslationInteractor;
import com.mishkun.yandextestexercise.presentation.views.TranslateView;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mishkun on 28.03.2017.
 */

@PerActivity
public class TranslatePresenter extends Presenter<TranslateView> {

    private static final String TAG = TranslatePresenter.class.getSimpleName();

    private final TranslationInteractor translationInteractor;
    private final GetSupportedLanguagesInteractor supportedLanguagesInteractor;



    @Inject
    public TranslatePresenter(TranslationInteractor translationInteractor, GetSupportedLanguagesInteractor supportedLanguagesInteractor) {
        this.translationInteractor = translationInteractor;
        this.supportedLanguagesInteractor = supportedLanguagesInteractor;
    }

    public void OnReverseTranslationButtonClicked() {

    }


    public void OnToDirectionButtonClicked(int position) {

    }


    public void OnFromDirectionButtonClicked(int position) {

    }


    public void OnFavoriteButtonClicked() {

    }

    @Override
    public void attachView(TranslateView view) {
        super.attachView(view);

    }

    public void setTranslationString(String translationString) {
        attachedView.setTranslation(translationString);
    }

    public void setExpandedTranslationString(String expandedTranslationString){
        attachedView.setExpandedTranslation(expandedTranslationString);
    }

    public void setTranslationTo(int To){
        attachedView.setTranslationTo(To);
    }

    public void setTranslationFrom(int From){
        attachedView.setTranslationTo(From);
    }

    @Override
    public void resume() {
        subscribeToUserTextInput();

        supportedLanguagesInteractor.getSupportedLanguages().subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> supported) throws Exception {
                attachedView.setSupportedLanguages(supported);
            }
        });
    }

    void subscribeToUserTextInput() {
        attachedView.getTextToTranslateStream()
                .observeOn(Schedulers.io())
                .concatMap(new Function<String, Observable<String>>() {
                    @Override
                    public Observable<String> apply(String query) throws Exception {
                        return translationInteractor.getTranslation(query);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String text) throws Exception {
                        TranslatePresenter.this.setTranslationString(text);
                    }
                });
    }

    @Override
    public void pause() {

    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
