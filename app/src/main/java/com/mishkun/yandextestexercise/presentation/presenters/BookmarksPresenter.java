package com.mishkun.yandextestexercise.presentation.presenters;

import com.mishkun.yandextestexercise.domain.entities.HistoryItem;
import com.mishkun.yandextestexercise.domain.interactors.AddEditHistoryInteractor;
import com.mishkun.yandextestexercise.domain.interactors.ClearFavoritesInteractor;
import com.mishkun.yandextestexercise.domain.interactors.GetFavoritesInteractor;
import com.mishkun.yandextestexercise.presentation.MutedObserver;
import com.mishkun.yandextestexercise.presentation.views.BookmarksView;

import javax.inject.Inject;

/**
 * Created by Mishkun on 21.04.2017.
 */

public class BookmarksPresenter extends Presenter<BookmarksView> {

    private final GetFavoritesInteractor getFavoritesInteractor;
    private final AddEditHistoryInteractor addEditHistoryInteractor;
    private ClearFavoritesInteractor clearFavoritesInteractor;

    @Inject
    public BookmarksPresenter(GetFavoritesInteractor getFavoritesInteractor, AddEditHistoryInteractor addEditHistoryInteractor,
                              ClearFavoritesInteractor clearFavoritesInteractor) {
        this.getFavoritesInteractor = getFavoritesInteractor;
        this.addEditHistoryInteractor = addEditHistoryInteractor;
        this.clearFavoritesInteractor = clearFavoritesInteractor;
    }

    @Override
    public void resume() {
        getFavoritesInteractor.execute(new HistoryObserver(attachedView));
    }

    @Override
    public void pause() {

    }

    public void onFavored(HistoryItem item, boolean favored) {
        item.setFavored(favored);
        addEditHistoryInteractor.execute(new MutedObserver<Void>(), item);
    }

    public void clearFavorites() {
        clearFavoritesInteractor.execute(new MutedObserver<Void>());
    }
}
