package com.mishkun.yandextestexercise.data;

import android.util.Log;

import com.mishkun.yandextestexercise.domain.entities.HistoryItem;
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
    public Observable<List<HistoryItem>> getHistoryItems() {
        return reactiveEntityStore.select(ShortTranslationEntity.class).where(ShortTranslationEntity.SAVED.eq(true)).get().observableResult().map(
                new Function<ReactiveResult<ShortTranslationEntity>, List<HistoryItem>>() {
                    @Override
                    public List<HistoryItem> apply(ReactiveResult<ShortTranslationEntity> shortTranslationEntities) throws Exception {
                        Log.d(TAG, "apply: Called");
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
        }
        Log.d(TAG, "addOrUpdateHistoryItem: called");
        itemEntity.setTranslation(item.getShortTranslation());
        itemEntity.setOriginal(item.getOriginal());
        itemEntity.setSaved(true);
        itemEntity.setFavored(item.isFavored());

        if (itemEntity.getId() == 0) {
            reactiveEntityStore.insert(itemEntity);
        } else {
            reactiveEntityStore.update(itemEntity);
        }
    }

    @Override
    public void clearFavoritesData() {
        return;
    }

    @Override
    public void clearHistoryData() {
        return;
    }
}
