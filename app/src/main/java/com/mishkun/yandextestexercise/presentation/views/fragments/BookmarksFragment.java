package com.mishkun.yandextestexercise.presentation.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.mishkun.yandextestexercise.R;
import com.mishkun.yandextestexercise.di.components.MainActivityComponent;
import com.mishkun.yandextestexercise.domain.entities.HistoryItem;
import com.mishkun.yandextestexercise.presentation.presenters.BookmarksPresenter;
import com.mishkun.yandextestexercise.presentation.views.BookmarksView;
import com.mishkun.yandextestexercise.presentation.views.FavButtonListener;
import com.mishkun.yandextestexercise.presentation.views.HistoryRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class BookmarksFragment extends BaseFragment implements FavButtonListener, BookmarksView {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    @BindView(R.id.bookmarks_list)
    public RecyclerView bookmarksRecyclerView;

    @BindView(R.id.clear_favs_button)
    public ImageButton clearFavsButton;

    @BindView(R.id.no_content_bookmarks)
    public RelativeLayout noContent;
    @Inject
    public BookmarksPresenter bookMarksPresenter;
    private HistoryRecyclerViewAdapter bookmarksRecyclerViewAdapter;

    public BookmarksFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BookmarksFragment newInstance(int columnCount) {
        BookmarksFragment fragment = new BookmarksFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        this.getComponent(MainActivityComponent.class).inject(this);
        bookMarksPresenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        ButterKnife.bind(this, view);

        clearFavsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookMarksPresenter.clearFavorites();
            }
        });

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(bookmarksRecyclerView.getContext(),
                                                                               DividerItemDecoration.VERTICAL);
        horizontalDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));

        List<HistoryItem> historyItemsDummy = new ArrayList<>();

        bookmarksRecyclerViewAdapter = new HistoryRecyclerViewAdapter(historyItemsDummy, this);
        bookmarksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        bookmarksRecyclerView.setAdapter(bookmarksRecyclerViewAdapter);
        bookmarksRecyclerView.addItemDecoration(horizontalDecoration);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        bookMarksPresenter.resume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void favButtonChecked(HistoryItem item, boolean favored) {
        bookMarksPresenter.onFavored(item, favored);
    }

    @Override
    public void setData(List<HistoryItem> historyItems) {
        bookmarksRecyclerViewAdapter.update(historyItems);
        bookmarksRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEmptyState() {
        noContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyState() {
        noContent.setVisibility(View.GONE);
    }
}
