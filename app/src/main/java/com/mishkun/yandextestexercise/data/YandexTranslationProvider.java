package com.mishkun.yandextestexercise.data;

import android.content.Context;
import android.util.Log;

import com.mishkun.yandextestexercise.InternetConnection;
import com.mishkun.yandextestexercise.R;
import com.mishkun.yandextestexercise.data.api.YandexTranslationRetrofitApi;
import com.mishkun.yandextestexercise.data.mappers.DetectionResponseMapper;
import com.mishkun.yandextestexercise.data.mappers.SupportedLanguagesMapper;
import com.mishkun.yandextestexercise.data.mappers.TranslationDirectionMapper;
import com.mishkun.yandextestexercise.data.responses.DetectionResponse;
import com.mishkun.yandextestexercise.data.responses.SupportedLanguagesResponse;
import com.mishkun.yandextestexercise.data.responses.TranslationResponse;
import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
import com.mishkun.yandextestexercise.domain.providers.ShortTranslationProvider;
import com.mishkun.yandextestexercise.domain.providers.SupportedLanguagesProvider;
import com.mishkun.yandextestexercise.domain.providers.TranslationDirectionGuessProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class YandexTranslationProvider implements ShortTranslationProvider, TranslationDirectionGuessProvider, SupportedLanguagesProvider {

    private static final String API_KEY = "trnsl.1.1.20170414T212056Z.25cea1a11b69f402.e460c7b40c14a59917a1f93580ad7beeadc2d559";
    private static final String UI = "ru";
    private final YandexTranslationRetrofitApi yandexTranslateRetrofit;
    private final Context context;
    private final String TAG = YandexTranslationProvider.class.getSimpleName();

    private final ReactiveEntityStore<Persistable> reactiveEntityStore;
    private final InternetConnection internetConnection;

    @Inject
    public YandexTranslationProvider(YandexTranslationRetrofitApi yandexTranslateRetrofit,
                                     Context context,
                                     ReactiveEntityStore<Persistable> reactiveEntityStore, InternetConnection internetConnection) {
        this.yandexTranslateRetrofit = yandexTranslateRetrofit;
        this.context = context;
        this.reactiveEntityStore = reactiveEntityStore;
        this.internetConnection = internetConnection;
    }

    @Override
    public Observable<String> getShortTranslation(String query, TranslationDirection direction) {
        return getShortTranslationFromApi(query, direction);
    }

    

    private Observable<String> getShortTranslationFromApi(String query, TranslationDirection direction) {
        return yandexTranslateRetrofit.translate(API_KEY, TranslationDirectionMapper.transform(direction), query)
                                      .map(new Function<TranslationResponse, String>() {
                                          @Override
                                          public String apply(TranslationResponse translationResponse) throws Exception {
                                              return translationResponse.getTranslation().get(0);
                                          }
                                      });
    }

    @Override
    public Observable<Language> guessLanguage(String query) {
        return getLanguageGuessFromApi(query);
    }

    private Observable<Language> getLanguageGuessFromApi(String query) {
        return yandexTranslateRetrofit.detectLanguage(API_KEY, query).map(new Function<DetectionResponse, Language>() {
            @Override
            public Language apply(DetectionResponse detectionResponse) throws Exception {
                return DetectionResponseMapper.transform(detectionResponse);
            }
        });
    }

    @Override
    public Observable<List<Language>> getSupportedLanguages() {
        return Observable.concat(getSupportedLanguagesFromDatabase(),
                                 getSupportedLanguagesFromApi(),
                                 getDefaultSupportedLanguages())
                         .filter(new Predicate<List<Language>>() {
                             @Override
                             public boolean test(List<Language> languages) throws Exception {
                                 return languages != null;
                             }
                         })
                         .firstElement().toObservable();
    }

    private Observable<List<Language>> getSupportedLanguagesFromDatabase() {
        ArrayList<Language> supportedLanguages = new ArrayList<Language>();
        return reactiveEntityStore.select(LanguageEntity.class).get().observable().map(new Function<LanguageEntity, Language>() {
            @Override
            public Language apply(LanguageEntity languageEntity) throws Exception {
                return SupportedLanguagesMapper.transform(languageEntity);
            }
        }).collectInto(supportedLanguages, new BiConsumer<List<Language>, Language>() {
            @Override
            public void accept(List<Language> languages, Language language) throws Exception {
                languages.add(language);
            }
        }).toObservable().switchMap(new Function<ArrayList<Language>, Observable<List<Language>>>() {
            @Override
            public Observable<List<Language>> apply(ArrayList<Language> languages) throws Exception {
                if (languages.size() > 0) {
                    return Observable.just((List<Language>) languages);
                } else {
                    return Observable.empty();
                }
            }
        });
    }

    private Observable<List<Language>> getSupportedLanguagesFromApi() {
        return internetConnection.isInternetOnObservable().switchMap(new Function<Boolean, ObservableSource<? extends List<Language>>>() {
            @Override
            public ObservableSource<? extends List<Language>> apply(Boolean isInternetOn) throws Exception {
                if (isInternetOn) {
                    Log.d(TAG, "InternetIsOn");
                    return yandexTranslateRetrofit.getSupportedLanguages(API_KEY, UI).map(new Function<SupportedLanguagesResponse, List<Language>>() {
                        @Override
                        public List<Language> apply(SupportedLanguagesResponse supportedLanguagesResponse) throws Exception {
                            return SupportedLanguagesMapper.transform(supportedLanguagesResponse);
                        }
                    }).doOnNext(new Consumer<List<Language>>() {
                        @Override
                        public void accept(List<Language> languages) throws Exception {
                            reactiveEntityStore.upsert(SupportedLanguagesMapper.transform(languages)).subscribe();
                        }
                    });
                } else {
                    Log.d(TAG, "InternetIsOff");
                    return Observable.empty();
                }
            }
        });

    }

    private Observable<List<Language>> getDefaultSupportedLanguages() {
        String[] keycodes = context.getResources().getStringArray(R.array.keyCodes);
        String[] displayNames = context.getResources().getStringArray(R.array.names);
        List<Language> languages = new ArrayList<>(keycodes.length);
        for (int i = 0; i < keycodes.length; i++) {
            languages.add(new Language(keycodes[i], displayNames[i]));
        }
        return Observable.just(languages);
    }

}
