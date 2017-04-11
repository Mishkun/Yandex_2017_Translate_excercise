package com.mishkun.yandextestexercise.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mishkun.yandextestexercise.R;
import com.mishkun.yandextestexercise.model.entities.TranslationDirection;
import com.mishkun.yandextestexercise.views.TranslateView;

import butterknife.BindView;
import io.reactivex.Flowable;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements TranslateView {

    @BindView(R.id.toTranslateEditText)
    public EditText mSourceTextView;

    @BindView(R.id.translatedTextView)
    public TextView mTranslationTextView;

    @BindView(R.id.extendedTranslatedTextView)
    public TextView mExpandedTranslationTextView;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
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
    public void setTranslationDirection(TranslationDirection direction) {
        
    }

    @Override
    public void setTranslation(String translation) {

    }

    @Override
    public void setExpandedTranslation(String expandedTranslation) {

    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void hideError() {

    }

    @Override
    public Flowable<String> getTextToTranslateStream() {
        return null;
    }
}
