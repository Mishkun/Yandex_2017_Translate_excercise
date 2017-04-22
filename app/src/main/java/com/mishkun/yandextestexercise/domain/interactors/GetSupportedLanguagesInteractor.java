package com.mishkun.yandextestexercise.domain.interactors;

import android.util.Log;

import com.mishkun.yandextestexercise.di.modules.DomainModule;
import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.domain.providers.SupportedLanguagesProvider;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

/**
 * Created by Mishkun on 12.04.2017.
 */

public class GetSupportedLanguagesInteractor extends ParameterlessInteractor<List<Language>> {

private static final String TAG = GetSupportedLanguagesInteractor.class.getSimpleName();
    private final SupportedLanguagesProvider supportedLanguagesProvider;

    @Inject
    GetSupportedLanguagesInteractor(@Named(DomainModule.JOB) Scheduler threadExecutor, @Named(DomainModule.UI) Scheduler postExecutionThread,
                                    SupportedLanguagesProvider supportedLanguagesProvider) {
        super(threadExecutor, postExecutionThread);
        this.supportedLanguagesProvider = supportedLanguagesProvider;
    }


    @Override
    Observable<List<Language>> buildUseCaseObservable() {
        return supportedLanguagesProvider.getSupportedLanguages().map(new Function<List<Language>, List<Language>>() {
            @Override
            public List<Language> apply(List<Language> languages) throws Exception {
                for (Iterator<Language> iterator = languages.iterator(); iterator.hasNext(); ) {
                    Language lang = iterator.next();
                    if (lang.getDisplayName() == null) {
                        iterator.remove();
                        Log.d(TAG, "removed: " + lang.getCode());
                    }
                }
                return languages;
            }
        }).map(new Function<List<Language>, List<Language>>() {
            @Override
            public List<Language> apply(List<Language> languages) throws Exception {
                java.util.Collections.sort(languages, new Comparator<Language>() {
                    @Override
                    public int compare(Language o1, Language o2) {
                        return o1.getDisplayName().compareTo(o2.getDisplayName());
                    }
                });
                return languages;
            }
        });
    }
}

