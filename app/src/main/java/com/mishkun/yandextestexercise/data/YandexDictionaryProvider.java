package com.mishkun.yandextestexercise.data;

import android.content.Context;
import android.util.Log;

import com.mishkun.yandextestexercise.InternetConnection;
import com.mishkun.yandextestexercise.R;
import com.mishkun.yandextestexercise.data.api.YandexDictionaryRetrofitApi;
import com.mishkun.yandextestexercise.data.mappers.DictionaryResponseMapper;
import com.mishkun.yandextestexercise.data.mappers.TranslationDirectionMapper;
import com.mishkun.yandextestexercise.data.responses.DictionaryResponse;
import com.mishkun.yandextestexercise.domain.entities.Definition;
import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.domain.entities.TranslationDirection;
import com.mishkun.yandextestexercise.domain.providers.DictionarySupportedLanguagesProvider;
import com.mishkun.yandextestexercise.domain.providers.ExpandedTranslationProvider;

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

public class YandexDictionaryProvider implements ExpandedTranslationProvider, DictionarySupportedLanguagesProvider {
    private static final String API_KEY = "dict.1.1.20170416T200201Z.0cb24acccf64ee4b.3662166758ddd66d0d174e44f6c1b4ae5611540d";
    private final YandexDictionaryRetrofitApi yandexDictionaryRetrofitApi;
    private final String TAG = YandexDictionaryProvider.class.getSimpleName();
    private final InternetConnection internetConnection;

    private final ReactiveEntityStore<Persistable> reactiveEntityStore;
    private Context context;

    @Inject
    public YandexDictionaryProvider(YandexDictionaryRetrofitApi yandexDictionaryRetrofitApi, InternetConnection internetConnection,
                                    ReactiveEntityStore<Persistable> reactiveEntityStore, Context context) {
        this.yandexDictionaryRetrofitApi = yandexDictionaryRetrofitApi;
        this.internetConnection = internetConnection;
        this.reactiveEntityStore = reactiveEntityStore;
        this.context = context;
    }

    @Override
    public Observable<Definition> getExpandedTranslation(String query, TranslationDirection direction) {
        return Observable.concat(getDefinitionFromDatabase(query, direction),
                                 getDefinitionFromApi(query, direction),
                                 Observable.just(new Definition(null, null, null)))
                         .filter(new Predicate<Definition>() {
                             @Override
                             public boolean test(Definition definition) throws Exception {
                                 return definition != null;
                             }
                         })
                         .firstElement()
                         .toObservable();
    }

    private Observable<Definition> getDefinitionFromDatabase(String query, TranslationDirection direction) {
        return reactiveEntityStore.select(ExpandedTranslationEntity.class)
                                  .where(ExpandedTranslationEntity.ORIGINAL.eq(query)
                                                                           .and(ExpandedTranslationEntity.TRANSLATION_FROM
                                                                                        .eq(direction.getTranslationFrom().getCode())
                                                                                        .and(ExpandedTranslationEntity.TRANSLATION_TO
                                                                                                     .eq(direction.getTranslationTo().getCode()))))
                                  .get()
                                  .observable()
                                  .map(new Function<ExpandedTranslationEntity, Definition>() {
                                      @Override
                                      public Definition apply(ExpandedTranslationEntity expandedTranslationEntity) throws Exception {
                                          List<Definition.DefinitionItem> definitionItems = new ArrayList<Definition.DefinitionItem>();
                                          for (DefinitionItemEntity entity : expandedTranslationEntity.getDefinitions()) {
                                              definitionItems.add(new Definition.DefinitionItem(entity.getSynonyms(), entity.getMeanings()));
                                          }
                                          return new Definition(expandedTranslationEntity.getOriginal(), expandedTranslationEntity.getTranscription(),
                                                                definitionItems);
                                      }
                                  });
    }

    private Observable<Definition> getDefinitionFromApi(final String query, final TranslationDirection direction) {
        return internetConnection.isInternetOnObservable().switchMap(new Function<Boolean, ObservableSource<Definition>>() {
            @Override
            public ObservableSource<Definition> apply(Boolean isInternetOn) throws Exception {
                if (isInternetOn) {
                    return yandexDictionaryRetrofitApi.getDictionaryTranslation(API_KEY, TranslationDirectionMapper.transform(direction), query).map(
                            new Function<DictionaryResponse, Definition>() {
                                @Override
                                public Definition apply(DictionaryResponse dictionaryResponse) throws Exception {
                                    return DictionaryResponseMapper.transform(dictionaryResponse);
                                }
                            }).doOnNext(new Consumer<Definition>() {
                        @Override
                        public void accept(Definition definition) throws Exception {
                            ExpandedTranslationEntity expandedTranslationEntity = reactiveEntityStore.select(ExpandedTranslationEntity.class)
                                                                                                     .where(ExpandedTranslationEntity.ORIGINAL
                                                                                                                    .eq(query)
                                                                                                                    .and(ExpandedTranslationEntity.TRANSLATION_FROM
                                                                                                                                 .eq(direction
                                                                                                                                             .getTranslationFrom()
                                                                                                                                             .getCode())
                                                                                                                                 .and(ExpandedTranslationEntity.TRANSLATION_TO
                                                                                                                                              .eq(direction
                                                                                                                                                          .getTranslationTo()
                                                                                                                                                          .getCode()))))
                                                                                                     .get().firstOr(new ExpandedTranslationEntity());
                            expandedTranslationEntity.setTranslation(definition.getText());
                            expandedTranslationEntity.setOriginal(query);
                            expandedTranslationEntity.setTranslationTo(direction.getTranslationTo().getCode());
                            expandedTranslationEntity.setTranslationFrom(direction.getTranslationFrom().getCode());
                            List<DefinitionItemEntity> definitionItems = expandedTranslationEntity.getDefinitions();
                            if (definitionItems.isEmpty()) {
                                definitionItems = new ArrayList<DefinitionItemEntity>();
                                if (definition.getDefinitionItems() != null) {
                                    for (Definition.DefinitionItem definitionItem : definition.getDefinitionItems()) {
                                                                        DefinitionItemEntity definitionItemEntity = new DefinitionItemEntity();
                                                                        definitionItemEntity.setSynonyms((ArrayList<String>) definitionItem.getSynonyms());
                                                                        definitionItemEntity.setMeanings((ArrayList<String>) definitionItem.getMeanings());
                                                                        definitionItemEntity.setOwner(expandedTranslationEntity);
                                                                        definitionItems.add(definitionItemEntity);
                                                                    }
                                }
                                reactiveEntityStore.upsert(expandedTranslationEntity).subscribe();
                                reactiveEntityStore.upsert(definitionItems).subscribe();
                            }
                            reactiveEntityStore.upsert(expandedTranslationEntity).subscribe();
                        }
                    });
                } else {
                    return Observable.empty();
                }
            }
        });
    }

    @Override
    public Observable<List<TranslationDirection>> getSupportedLanguages() {
        return Observable.concat(getSupportedLanguagesFromDatabase(),
                                 getSupportedLanguagesFromApi(),
                                 getDefaultDirections())
                         .filter(new Predicate<List<TranslationDirection>>() {
                             @Override
                             public boolean test(List<TranslationDirection> directions) throws Exception {
                                 return directions != null;
                             }
                         })
                         .firstElement()
                         .toObservable();
    }

    private Observable<List<TranslationDirection>> getSupportedLanguagesFromDatabase() {
        List<TranslationDirection> supportedLanguages = new ArrayList<TranslationDirection>();
        return reactiveEntityStore.select(TranslationDirectionEntity.class)
                                  .get()
                                  .observable()
                                  .collectInto(supportedLanguages, new BiConsumer<List<TranslationDirection>, TranslationDirectionEntity>() {
                                      @Override
                                      public void accept(
                                              List<TranslationDirection> languages,
                                              TranslationDirectionEntity language) throws Exception {
                                          languages.add(new TranslationDirection(new Language(language.getTranslationFrom(), null),
                                                                                 new Language(language.getTranslationTo(), null)));
                                      }
                                  })
                                  .toObservable()
                                  .switchMap(new Function<List<TranslationDirection>, Observable<List<TranslationDirection>>>() {
                                      @Override
                                      public Observable<List<TranslationDirection>> apply(List<TranslationDirection> languages) throws Exception {
                                          if (languages.size() > 0) {
                                              return Observable.just(languages);
                                          } else {
                                              return Observable.empty();
                                          }
                                      }
                                  });
    }

    private Observable<List<TranslationDirection>> getSupportedLanguagesFromApi() {
        return internetConnection.isInternetOnObservable().switchMap(new Function<Boolean, ObservableSource<List<TranslationDirection>>>() {
            @Override
            public ObservableSource<List<TranslationDirection>> apply(Boolean isInternetOn) throws Exception {
                if (isInternetOn) {
                    return yandexDictionaryRetrofitApi.getSupportedLangs(API_KEY).map(new Function<List<String>, List<TranslationDirection>>() {
                        @Override
                        public List<TranslationDirection> apply(List<String> directions) throws Exception {
                            List<TranslationDirection> translationDirections = new ArrayList<TranslationDirection>(directions.size());
                            for (String direction : directions) {
                                translationDirections.add(TranslationDirectionMapper.transform(direction));
                            }
                            return translationDirections;
                        }
                    }).doOnNext(new Consumer<List<TranslationDirection>>() {
                        @Override
                        public void accept(List<TranslationDirection> directions) throws Exception {
                            List<TranslationDirectionEntity> translationDirectionEntities = new ArrayList<TranslationDirectionEntity>();
                            for (TranslationDirection translationDirection : directions) {
                                TranslationDirectionEntity entity = new TranslationDirectionEntity();
                                entity.setTranslationFrom(translationDirection.getTranslationFrom().getCode());
                                entity.setTranslationTo(translationDirection.getTranslationTo().getCode());
                                translationDirectionEntities.add(entity);
                            }
                            reactiveEntityStore.delete(TranslationDirectionEntity.class).get().single().subscribe();
                            reactiveEntityStore.insert(translationDirectionEntities).subscribe();
                        }
                    });
                } else {
                    return Observable.empty();
                }
            }
        });
    }

    private Observable<List<TranslationDirection>> getDefaultDirections() {
        String[] from = context.getResources().getStringArray(R.array.translation_from);
        String[] to = context.getResources().getStringArray(R.array.translation_to);
        List<TranslationDirection> languages = new ArrayList<>(from.length);
        for (int i = 0; i < from.length; i++) {
            languages.add(new TranslationDirection(new Language(from[i], null), new Language(to[i], null)));
        }
        return Observable.just(languages);
    }
}
