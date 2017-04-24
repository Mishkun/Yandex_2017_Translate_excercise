package com.mishkun.yandextestexercise.data.providers;

import android.content.Context;

import com.mishkun.yandextestexercise.InternetConnection;
import com.mishkun.yandextestexercise.R;
import com.mishkun.yandextestexercise.data.LanguageEntity;
import com.mishkun.yandextestexercise.data.LanguageGuessEntity;
import com.mishkun.yandextestexercise.data.ShortTranslationEntity;
import com.mishkun.yandextestexercise.data.api.YandexTranslationRetrofitApi;
import com.mishkun.yandextestexercise.data.mappers.entity.SupportedLanguagesEntityMapper;
import com.mishkun.yandextestexercise.data.mappers.response.DetectionResponseMapper;
import com.mishkun.yandextestexercise.data.mappers.response.SupportedLanguagesResponseMapper;
import com.mishkun.yandextestexercise.data.mappers.response.TranslationDirectionResponseMapper;
import com.mishkun.yandextestexercise.data.responses.DetectionResponse;
import com.mishkun.yandextestexercise.data.responses.SupportedLanguagesResponse;
import com.mishkun.yandextestexercise.data.responses.TranslationResponse;
import com.mishkun.yandextestexercise.domain.entities.ShortTranslationModel;
import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
import com.mishkun.yandextestexercise.domain.providers.ShortTranslationProvider;
import com.mishkun.yandextestexercise.domain.providers.SupportedLanguagesProvider;
import com.mishkun.yandextestexercise.domain.providers.TranslationDirectionGuessProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class YandexTranslationProvider extends ConnectedDataSource implements ShortTranslationProvider, TranslationDirectionGuessProvider, SupportedLanguagesProvider {

    private static final String API_KEY = "trnsl.1.1.20170414T212056Z.25cea1a11b69f402.e460c7b40c14a59917a1f93580ad7beeadc2d559";
    private  final String UI;
    private final YandexTranslationRetrofitApi yandexTranslateRetrofit;
    private final Context context;
    private final String TAG = YandexTranslationProvider.class.getSimpleName();

    private final ReactiveEntityStore<Persistable> reactiveEntityStore;

    @Inject
    public YandexTranslationProvider(YandexTranslationRetrofitApi yandexTranslateRetrofit,
                                     Context context,
                                     ReactiveEntityStore<Persistable> reactiveEntityStore, InternetConnection internetConnection) {
        super(internetConnection);
        this.yandexTranslateRetrofit = yandexTranslateRetrofit;
        this.reactiveEntityStore = reactiveEntityStore;
        this.context = context;
        UI = context.getResources().getString(R.string.ui);
    }

    @Override
    public Observable<ShortTranslationModel> getShortTranslation(String query, TranslationDirection direction) {
        return collectFromSources(getShortTranslationFromApi(query, direction),
                                  getShortTranslationFromDatabase(query, direction),
                                  Observable.just(new ShortTranslationModel(query, "", false, null, null)));
    }


    private Observable<ShortTranslationModel> getShortTranslationFromDatabase(String query, TranslationDirection direction) {
        return reactiveEntityStore.select(ShortTranslationEntity.class)
                                  .where(ShortTranslationEntity.ORIGINAL.eq(query).and(ShortTranslationEntity.DIRECTION_FROM
                                                                                               .eq(direction.getTranslationFrom().getCode())
                                                                                               .and(ShortTranslationEntity.DIRECTION_TO
                                                                                                            .eq(direction.getTranslationTo()
                                                                                                                         .getCode()))))
                                  .get()
                                  .observable()
                                  .map(new Function<ShortTranslationEntity, ShortTranslationModel>() {
                                      @Override
                                      public ShortTranslationModel apply(ShortTranslationEntity shortTranslationEntity) throws Exception {
                                          return new ShortTranslationModel(shortTranslationEntity.getOriginal(),
                                                                           shortTranslationEntity.getTranslation(),
                                                                           shortTranslationEntity.isFavored(),
                                                                           new Language(shortTranslationEntity.getDirectionFrom(), null),
                                                                           new Language(shortTranslationEntity.getDirectionTo(), null));
                                      }
                                  });
    }


    private Observable<ShortTranslationModel> getShortTranslationFromApi(final String query, final TranslationDirection direction) {
        return getIfInternet(yandexTranslateRetrofit.translate(API_KEY, TranslationDirectionResponseMapper.transform(direction), query)
                                                    .map(new Function<TranslationResponse, ShortTranslationModel>() {
                                                        @Override
                                                        public ShortTranslationModel apply(TranslationResponse translationResponse) throws Exception {
                                                            return new ShortTranslationModel(query, translationResponse.getTranslation().get(0), false,
                                                                                             direction.getTranslationFrom(),
                                                                                             direction.getTranslationTo());
                                                        }
                                                    })
                                                    .doOnNext(new ShortTranslationCacher()));
    }

    @Override
    public Observable<Language> guessLanguage(String query) {
        return collectFromSources(guessLanguageFromDatabase(query), getLanguageGuessFromApi(query), Observable.just(new Language("", null)));
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
        return getIfInternet(yandexTranslateRetrofit.detectLanguage(API_KEY, query)
                                                    .map(new Function<DetectionResponse, Language>() {
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
                }));
    }

    @Override
    public Observable<List<Language>> getSupportedLanguages() {
        return collectFromSources(getSupportedLanguagesFromDatabase(),
                                  getSupportedLanguagesFromApi(),
                                  getDefaultSupportedLanguages());
    }

    private Observable<List<Language>> getSupportedLanguagesFromDatabase() {
        ArrayList<Language> supportedLanguages = new ArrayList<Language>();
        return reactiveEntityStore.select(LanguageEntity.class).get().observable().map(new Function<LanguageEntity, Language>() {
            @Override
            public Language apply(LanguageEntity languageEntity) throws Exception {
                return SupportedLanguagesEntityMapper.transform(languageEntity);
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
        return getIfInternet(yandexTranslateRetrofit.getSupportedLanguages(API_KEY, UI)
                                                    .map(new Function<SupportedLanguagesResponse, List<Language>>() {
                                                        @Override
                                                        public List<Language> apply(
                                                                SupportedLanguagesResponse supportedLanguagesResponse) throws Exception {
                                                            return SupportedLanguagesResponseMapper.transform(supportedLanguagesResponse);
                                                        }
                                                    }).doOnNext(new Consumer<List<Language>>() {
                    @Override
                    public void accept(List<Language> languages) throws Exception {
                        reactiveEntityStore.delete(LanguageEntity.class).get().single().subscribe();
                        reactiveEntityStore.upsert(SupportedLanguagesEntityMapper.transform(languages)).subscribe();
                    }
                }));

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

    private class ShortTranslationCacher implements Consumer<ShortTranslationModel> {

        @Override
        public void accept(ShortTranslationModel shortTranslationModel) throws Exception {
            ShortTranslationEntity shortTranslationEntity = reactiveEntityStore.select(
                    ShortTranslationEntity.class).where(ShortTranslationEntity.ORIGINAL.eq(shortTranslationModel.getOriginal())
                                                                                       .and(ShortTranslationEntity.DIRECTION_FROM
                                                                                                    .eq(shortTranslationModel.getFrom().getCode())
                                                                                                    .and(ShortTranslationEntity.DIRECTION_TO
                                                                                                                 .eq(shortTranslationModel.getTo().getCode()))))
                                                                               .get()
                                                                               .firstOr(new ShortTranslationEntity());
            shortTranslationEntity.setTranslation(shortTranslationModel.getTranslation());
            shortTranslationEntity.setOriginal(shortTranslationModel.getOriginal());
            shortTranslationEntity.setDirectionFrom(shortTranslationModel.getFrom().getCode());
            shortTranslationEntity.setDirectionTo(shortTranslationModel.getTo().getCode());
            reactiveEntityStore.upsert(shortTranslationEntity).subscribe();
        }
    }

}
