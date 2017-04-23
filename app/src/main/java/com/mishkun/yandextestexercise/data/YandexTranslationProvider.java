package com.mishkun.yandextestexercise.data;

import android.content.Context;

import com.mishkun.yandextestexercise.InternetConnection;
import com.mishkun.yandextestexercise.R;
import com.mishkun.yandextestexercise.data.api.YandexTranslationRetrofitApi;
import com.mishkun.yandextestexercise.data.mappers.DetectionResponseMapper;
import com.mishkun.yandextestexercise.data.mappers.SupportedLanguagesMapper;
import com.mishkun.yandextestexercise.data.mappers.TranslationDirectionMapper;
import com.mishkun.yandextestexercise.data.responses.DetectionResponse;
import com.mishkun.yandextestexercise.data.responses.SupportedLanguagesResponse;
import com.mishkun.yandextestexercise.data.responses.TranslationResponse;
import com.mishkun.yandextestexercise.domain.entities.HistoryItem;
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
    public Observable<HistoryItem> getShortTranslation(String query, TranslationDirection direction) {
        return Observable.concat(getShortTranslationFromDatabase(query, direction),
                                 getShortTranslationFromApi(query, direction),
                                 Observable.just(new HistoryItem(query, "", false)))
                         .filter(new Predicate<HistoryItem>() {
                             @Override
                             public boolean test(HistoryItem translation) throws Exception {
                                 return translation != null;
                             }
                         })
                         .firstElement()
                         .toObservable();
    }

    private Observable<HistoryItem> getShortTranslationFromDatabase(String query, TranslationDirection direction) {
        return reactiveEntityStore.select(ShortTranslationEntity.class)
                                  .where(ShortTranslationEntity.ORIGINAL.eq(query)
                                                                        .and(ShortTranslationEntity.DIRECTION_FROM
                                                                                     .eq(direction.getTranslationFrom()
                                                                                                  .getCode())
                                                                                     .and(ShortTranslationEntity.DIRECTION_TO
                                                                                                  .eq(direction.getTranslationTo()
                                                                                                               .getCode()))))
                                  .get().observable().map(
                        new Function<ShortTranslationEntity, HistoryItem>() {
                            @Override
                            public HistoryItem apply(ShortTranslationEntity shortTranslationEntity) throws Exception {
                                return new HistoryItem(shortTranslationEntity.getOriginal(), shortTranslationEntity.getTranslation(),
                                                       shortTranslationEntity.isFavored());
                            }
                        });
    }


    private Observable<HistoryItem> getShortTranslationFromApi(final String query, final TranslationDirection direction) {
        return internetConnection.isInternetOnObservable().switchMap(new Function<Boolean, ObservableSource<HistoryItem>>() {
            @Override
            public ObservableSource<HistoryItem> apply(Boolean isInternetOn) throws Exception {
                if (isInternetOn) {
                    return yandexTranslateRetrofit.translate(API_KEY, TranslationDirectionMapper.transform(direction), query).map(
                            new Function<TranslationResponse, HistoryItem>() {
                                @Override
                                public HistoryItem apply(TranslationResponse translationResponse) throws Exception {
                                    return new HistoryItem(query, translationResponse.getTranslation().get(0), false);
                                }
                            }).doOnNext(new Consumer<HistoryItem>() {
                        @Override
                        public void accept(HistoryItem translation) throws Exception {
                            ShortTranslationEntity shortTranslationEntity = reactiveEntityStore.select(
                                    ShortTranslationEntity.class).where(ShortTranslationEntity.ORIGINAL.eq(query)
                                                                                                       .and(ShortTranslationEntity.DIRECTION_FROM
                                                                                                                    .eq(direction.getTranslationFrom()
                                                                                                                                 .getCode())
                                                                                                                    .and(ShortTranslationEntity.DIRECTION_TO
                                                                                                                                 .eq(direction
                                                                                                                                             .getTranslationTo()
                                                                                                                                             .getCode()))))
                                                                                               .get()
                                                                                               .firstOr(new ShortTranslationEntity());
                            shortTranslationEntity.setTranslation(translation.getShortTranslation());
                            shortTranslationEntity.setOriginal(query);
                            shortTranslationEntity.setDirectionFrom(direction.getTranslationFrom()
                                                                             .getCode());
                            shortTranslationEntity.setDirectionTo(direction.getTranslationTo()
                                                                           .getCode());
                            reactiveEntityStore.upsert(shortTranslationEntity).subscribe();
                        }
                    });
                } else {
                    return Observable.empty();
                }
            }
        });
    }

    @Override
    public Observable<Language> guessLanguage(String query) {
        return Observable.concat(guessLanguageFromDatabase(query),
                                 getLanguageGuessFromApi(query),
                                 Observable.just(new Language("ru", null)))
                         .filter(new Predicate<Language>() {
                             @Override
                             public boolean test(Language language) throws Exception {
                                 return language != null;
                             }
                         })
                         .firstElement()
                         .toObservable();
    }

    private Observable<Language> guessLanguageFromDatabase(String query) {
        return reactiveEntityStore.findByKey(LanguageGuessEntity.class, query).toObservable().map(new Function<LanguageGuessEntity, Language>() {
            @Override
            public Language apply(LanguageGuessEntity languageGuessEntity) throws Exception {
                return new Language(languageGuessEntity.getLanguage(), null);
            }
        });
    }

    private Observable<Language> getLanguageGuessFromApi(final String query) {
        return internetConnection.isInternetOnObservable().switchMap(new Function<Boolean, ObservableSource<Language>>() {
            @Override
            public ObservableSource<Language> apply(Boolean isInternetOn) throws Exception {
                if (isInternetOn) {
                    return yandexTranslateRetrofit.detectLanguage(API_KEY, query).map(new Function<DetectionResponse, Language>() {
                        @Override
                        public Language apply(DetectionResponse detectionResponse) throws Exception {
                            return DetectionResponseMapper.transform(detectionResponse);
                        }
                    }).doOnNext(new Consumer<Language>() {
                        @Override
                        public void accept(Language language) throws Exception {
                            LanguageGuessEntity guessEntity = new LanguageGuessEntity();
                            guessEntity.setLanguage(language.getCode());
                            guessEntity.setQuery(query);
                            reactiveEntityStore.upsert(guessEntity).subscribe();
                        }
                    });
                } else {
                    return Observable.empty();
                }
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
                    return yandexTranslateRetrofit.getSupportedLanguages(API_KEY, UI).map(new Function<SupportedLanguagesResponse, List<Language>>() {
                        @Override
                        public List<Language> apply(SupportedLanguagesResponse supportedLanguagesResponse) throws Exception {
                            return SupportedLanguagesMapper.transform(supportedLanguagesResponse);
                        }
                    }).doOnNext(new Consumer<List<Language>>() {
                        @Override
                        public void accept(List<Language> languages) throws Exception {
                            reactiveEntityStore.delete(LanguageEntity.class).get().single().subscribe();
                            reactiveEntityStore.upsert(SupportedLanguagesMapper.transform(languages)).subscribe();
                        }
                    });
                } else {
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
