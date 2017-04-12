package com.mishkun.yandextestexercise.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mishkun.yandextestexercise.R;
import com.mishkun.yandextestexercise.di.components.MainActivityComponent;
import com.mishkun.yandextestexercise.presenters.TranslatePresenter;
import com.mishkun.yandextestexercise.views.TranslateView;

import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.Observable;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;


/**
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment implements TranslateView {

    private static final String TAG = HomeFragment.class.getSimpleName();

    @BindView(R.id.toTranslateEditText)
    public EditText sourceTextView;

    @BindView(R.id.translatedTextView)
    public TextView translationTextView;

    @BindView(R.id.extendedTranslatedTextView)
    public TextView expandedTranslationTextView;

    @Inject
    public TranslatePresenter translatePresenter;

    //private static final String ARG_TEXT_CONTENT = "text-content";

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
        this.getComponent(MainActivityComponent.class).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        translatePresenter.attachView(this);
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

    @Override
    public void setTranslationTo(String To) {

    }

    @Override
    public void setTranslationFrom(String From) {

    }

    @Override
    public void setSupportedLanguages(List<String> supportedLanguages) {

    }

    @Override
    public void setTranslation(String translation) {
        translationTextView.setText(translation);
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
    public Observable<String> getTextToTranslateStream() {
        return RxTextView.textChanges(sourceTextView).map(new Function<CharSequence, String>() {
            @Override
            public String apply(CharSequence text) throws Exception {
                return text.toString();
            }
        });
    }
}
