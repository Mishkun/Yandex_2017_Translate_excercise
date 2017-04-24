package com.mishkun.yandextestexercise.presentation.presenters;

import com.mishkun.yandextestexercise.domain.entities.ShortTranslationModel;
import com.mishkun.yandextestexercise.domain.interactors.AddEditHistoryInteractor;
import com.mishkun.yandextestexercise.domain.interactors.ClearFavoritesInteractor;
import com.mishkun.yandextestexercise.domain.interactors.GetFavoritesInteractor;
import com.mishkun.yandextestexercise.presentation.Presenter;
import com.mishkun.yandextestexercise.presentation.views.BookmarksView;
import com.mishkun.yandextestexercise.presentation.views.HistoryView;

import java.util.List;

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
        getFavoritesInteractor.execute(new BookmarksObserver(attachedView));
    }

    @Override
    public void pause() {
        getFavoritesInteractor.dispose();
        addEditHistoryInteractor.dispose();
        clearFavoritesInteractor.dispose();
    }

    public void onFavored(ShortTranslationModel item, boolean favored) {
        item.setFavored(favored);
        addEditHistoryInteractor.execute(new MutedObserver<Void>(), item);
    }

    public void clearFavorites() {
        clearFavoritesInteractor.execute(new MutedObserver<Void>());
    }

    private class BookmarksObserver extends HistoryObserver {


        BookmarksObserver(HistoryView historyView) {
            super(historyView);}

        @Override
        public void onNext(List<ShortTranslationModel> value) {
            super.onNext(value);
            if (value.size() > 0){
                attachedView.hideEmptyState();
            }else {
                attachedView.showEmptyState();
            }
        }
    }
}
