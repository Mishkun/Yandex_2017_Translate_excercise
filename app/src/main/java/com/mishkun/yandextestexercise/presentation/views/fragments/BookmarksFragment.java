package com.mishkun.yandextestexercise.presentation.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mishkun.yandextestexercise.R;
import com.mishkun.yandextestexercise.presentation.views.MyHistoryRecyclerViewAdapter;
import com.mishkun.yandextestexercise.presentation.views.TranslationResultViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class BookmarksFragment extends Fragment {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    @BindView(R.id.bookmarks_list)
    public RecyclerView bookmarksRecyclerView;
    private MyHistoryRecyclerViewAdapter bookmarksRecyclerViewAdapter;

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

        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        ButterKnife.bind(this, view);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(bookmarksRecyclerView.getContext(),
                                                                               DividerItemDecoration.VERTICAL);
        horizontalDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));

        List<TranslationResultViewModel> historyItemsDummy = new ArrayList<TranslationResultViewModel>();
        historyItemsDummy.add(new TranslationResultViewModel("hi", "hello", null, 1, 2, true));
        historyItemsDummy.add(new TranslationResultViewModel("hi", "WOW", null, 1, 2, false));
        historyItemsDummy.add(new TranslationResultViewModel("rozor", "pizdec", null, 1, 2, true));
        historyItemsDummy.add(new TranslationResultViewModel("sss", "quart", null, 1, 2, false));

        bookmarksRecyclerViewAdapter = new MyHistoryRecyclerViewAdapter(historyItemsDummy);
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
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
