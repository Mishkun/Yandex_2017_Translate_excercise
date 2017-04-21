package com.mishkun.yandextestexercise.data;

import android.util.Log;

import com.mishkun.yandextestexercise.domain.entities.HistoryItem;
import com.mishkun.yandextestexercise.domain.providers.HistoryProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
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
    public Observable<List<HistoryItem>> getHistoryItems() {
        return reactiveEntityStore.select(ShortTranslationEntity.class).where(ShortTranslationEntity.SAVED.eq(true)).get().observableResult().map(
                new Function<ReactiveResult<ShortTranslationEntity>, List<HistoryItem>>() {
                    @Override
                    public List<HistoryItem> apply(ReactiveResult<ShortTranslationEntity> shortTranslationEntities) throws Exception {
                        List<HistoryItem> historyItems = new ArrayList<HistoryItem>();
                        for (ShortTranslationEntity shortTranslationEntity : shortTranslationEntities) {
                            historyItems.add(new HistoryItem(shortTranslationEntity.getOriginal(), shortTranslationEntity.getTranslation(),
                                                             shortTranslationEntity.isFavored()
                            ));
                        }
                        return historyItems;
                    }
                });
    }

    @Override
    public Observable<List<HistoryItem>> getFavoredItems() {
        return reactiveEntityStore.select(ShortTranslationEntity.class).where(ShortTranslationEntity.FAVORED.eq(true).and(ShortTranslationEntity.SAVED.eq(true))).get().observableResult().map(
                new Function<ReactiveResult<ShortTranslationEntity>, List<HistoryItem>>() {
                    @Override
                    public List<HistoryItem> apply(ReactiveResult<ShortTranslationEntity> shortTranslationEntities) throws Exception {
                        List<HistoryItem> historyItems = new ArrayList<HistoryItem>();
                        for (ShortTranslationEntity shortTranslationEntity : shortTranslationEntities) {
                            historyItems.add(new HistoryItem(shortTranslationEntity.getOriginal(), shortTranslationEntity.getTranslation(),
                                                             shortTranslationEntity.isFavored()
                            ));
                        }
                        return historyItems;
                    }
                });
    }

    @Override
    public void addOrUpdateHistoryItem(HistoryItem item) {

        ShortTranslationEntity itemEntity = reactiveEntityStore.select(ShortTranslationEntity.class)
                                                               .where(ShortTranslationEntity.ORIGINAL.eq(item.getOriginal())
                                                                                                     .and(ShortTranslationEntity.TRANSLATION
                                                                                                                  .eq(item.getShortTranslation())))
                                                               .get()
                                                               .firstOrNull();
        if (itemEntity == null) {
            itemEntity = new ShortTranslationEntity();
            List<ShortTranslationEntity> entities = reactiveEntityStore.select(ShortTranslationEntity.class).get().toList();
        }

        itemEntity.setTranslation(item.getShortTranslation());
        itemEntity.setOriginal(item.getOriginal());
        itemEntity.setSaved(true);
        itemEntity.setFavored(item.isFavored());

        if (itemEntity.getId() == 0) {
            Log.d(TAG, "insert");
            reactiveEntityStore.insert(itemEntity).subscribe();
        } else {
            Log.d(TAG, "update");
            reactiveEntityStore.update(itemEntity).subscribe();
        }
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
