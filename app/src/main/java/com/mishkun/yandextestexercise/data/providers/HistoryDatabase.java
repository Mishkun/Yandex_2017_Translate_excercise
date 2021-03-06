package com.mishkun.yandextestexercise.data.providers;

import android.util.Log;

import com.mishkun.yandextestexercise.data.ShortTranslationEntity;
import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.domain.entities.ShortTranslationModel;
import com.mishkun.yandextestexercise.domain.providers.HistoryProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveResult;

/**
 * Created by Mishkun on 16.04.2017.
 */

public class HistoryDatabase implements HistoryProvider {
    private static final String TAG = HistoryDatabase.class.getSimpleName();
    private final ReactiveEntityStore<Persistable> reactiveEntityStore;

    @Inject
    public HistoryDatabase(ReactiveEntityStore<Persistable> shortTranslationEntityReactiveEntityStore) {
        this.reactiveEntityStore = shortTranslationEntityReactiveEntityStore;
    }

    @Override
    public Observable<List<ShortTranslationModel>> getHistoryItems() {
        return reactiveEntityStore.select(ShortTranslationEntity.class).where(ShortTranslationEntity.SAVED.eq(true)).get().observableResult().map(
                new Function<ReactiveResult<ShortTranslationEntity>, List<ShortTranslationModel>>() {
                    @Override
                    public List<ShortTranslationModel> apply(ReactiveResult<ShortTranslationEntity> shortTranslationEntities) throws Exception {
                        List<ShortTranslationModel> shortTranslationModels = new ArrayList<ShortTranslationModel>();
                        for (ShortTranslationEntity shortTranslationEntity : shortTranslationEntities) {
                            shortTranslationModels.add(
                                    new ShortTranslationModel(shortTranslationEntity.getOriginal(), shortTranslationEntity.getTranslation(),
                                                              shortTranslationEntity.isFavored(),
                                                              new Language(shortTranslationEntity.getDirectionFrom(), null),
                                                              new Language(shortTranslationEntity.getDirectionTo(), null)));
                        }
                        return shortTranslationModels;
                    }
                });
    }

    @Override
    public Observable<List<ShortTranslationModel>> getFavoredItems() {
        return reactiveEntityStore.select(ShortTranslationEntity.class).where(
                ShortTranslationEntity.FAVORED.eq(true).and(ShortTranslationEntity.SAVED.eq(true))).get().observableResult().map(
                new Function<ReactiveResult<ShortTranslationEntity>, List<ShortTranslationModel>>() {
                    @Override
                    public List<ShortTranslationModel> apply(ReactiveResult<ShortTranslationEntity> shortTranslationEntities) throws Exception {
                        List<ShortTranslationModel> shortTranslationModels = new ArrayList<ShortTranslationModel>();
                        for (ShortTranslationEntity shortTranslationEntity : shortTranslationEntities) {
                            shortTranslationModels.add(
                                    new ShortTranslationModel(shortTranslationEntity.getOriginal(), shortTranslationEntity.getTranslation(),
                                                              shortTranslationEntity.isFavored(),
                                                              new Language(shortTranslationEntity.getDirectionFrom(), null),
                                                              new Language(shortTranslationEntity.getDirectionTo(), null)));
                        }
                        return shortTranslationModels;
                    }
                });
    }

    @Override
    public void addOrUpdateHistoryItem(ShortTranslationModel item) {
        ShortTranslationEntity itemEntity = reactiveEntityStore.select(ShortTranslationEntity.class)
                                                               .where(ShortTranslationEntity.ORIGINAL.eq(item.getOriginal())
                                                                                                     .and(ShortTranslationEntity.TRANSLATION
                                                                                                                  .eq(item.getTranslation())))
                                                               .get()
                                                               .firstOrNull();
        if (itemEntity == null) {
            itemEntity = new ShortTranslationEntity();
        }

        itemEntity.setDirectionFrom(item.getFrom().getCode());
        itemEntity.setDirectionTo(item.getTo().getCode());
        itemEntity.setSaved(true);
        itemEntity.setTranslation(item.getTranslation());
        itemEntity.setOriginal(item.getOriginal());
        itemEntity.setFavored(item.isFavored());

        reactiveEntityStore.upsert(itemEntity).subscribe();
    }

    @Override
    public void deleteHistoryItem(ShortTranslationModel item) {
        ShortTranslationEntity itemEntity = reactiveEntityStore.select(ShortTranslationEntity.class)
                                                               .where(ShortTranslationEntity.ORIGINAL.eq(item.getOriginal())
                                                                                                     .and(ShortTranslationEntity.TRANSLATION
                                                                                                                  .eq(item.getTranslation())))
                                                               .get()
                                                               .firstOrNull();
        if (itemEntity == null) {
            itemEntity = new ShortTranslationEntity();
        }
        itemEntity.setDirectionFrom(item.getFrom().getCode());
        itemEntity.setDirectionTo(item.getTo().getCode());
        itemEntity.setSaved(false);
        itemEntity.setFavored(false);
        itemEntity.setTranslation(item.getTranslation());
        itemEntity.setOriginal(item.getOriginal());
        itemEntity.setFavored(item.isFavored());

        reactiveEntityStore.upsert(itemEntity).subscribe();
    }

    @Override
    public void clearFavoritesData() {
        List<ShortTranslationEntity> shortTranslationEntities = reactiveEntityStore.select(ShortTranslationEntity.class).where(
                ShortTranslationEntity.FAVORED.eq(true).and(ShortTranslationEntity.SAVED.eq(true))).get().toList();
        for (ShortTranslationEntity shortTranslationEntity :
                shortTranslationEntities) {
            shortTranslationEntity.setFavored(false);
        }
        Log.d(TAG, "clearFavs");
        reactiveEntityStore.update(shortTranslationEntities).subscribe();
        reactiveEntityStore.refresh(shortTranslationEntities).subscribe();
    }

    @Override
    public void clearHistoryData() {
        List<ShortTranslationEntity> shortTranslationEntities = reactiveEntityStore.select(ShortTranslationEntity.class).where(
                ShortTranslationEntity.SAVED.eq(true)).get().toList();
        for (ShortTranslationEntity shortTranslationEntity :
                shortTranslationEntities) {
            shortTranslationEntity.setSaved(false);
            shortTranslationEntity.setFavored(false);
        }

        Log.d(TAG, "clearHis");
        reactiveEntityStore.update(shortTranslationEntities);
    }
}
