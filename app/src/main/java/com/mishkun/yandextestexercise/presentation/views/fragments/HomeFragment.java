package com.mishkun.yandextestexercise.presentation.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextSwitcher;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.mishkun.yandextestexercise.R;
import com.mishkun.yandextestexercise.di.components.MainActivityComponent;
import com.mishkun.yandextestexercise.domain.entities.Definition;
import com.mishkun.yandextestexercise.presentation.presenters.TranslatePresenter;
import com.mishkun.yandextestexercise.presentation.views.TranslateView;
import com.mishkun.yandextestexercise.presentation.views.TranslationQueryViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import io.reactivex.Observable;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;


/**
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment implements TranslateView {

    private static final String TAG = HomeFragment.class.getSimpleName();

    @BindView(R.id.from_translation_spinner)
    public Spinner fromTranslationSpinner;

    @BindView(R.id.reverse_translation_direction_btn)
    public ImageButton reverseTranslationButton;

    @BindView(R.id.to_translation_spinner)
    public Spinner toTranslationSpinner;

    @BindView(R.id.toTranslateEditText)
    public EditText sourceTextView;

    @BindView(R.id.translatedTextView)
    public TextView translationTextView;

    @BindView(R.id.extendedTranslatedTextView)
    public TextView expandedTranslationTextView;

    @Inject
    public TranslatePresenter translatePresenter;

    private PublishSubject<TranslationQueryViewModel> translationQueryViewModelBehaviorSubject;

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
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        translationQueryViewModelBehaviorSubject = PublishSubject.create();
        this.getComponent(MainActivityComponent.class).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        translatePresenter.attachView(this);
        initializeReverseButton();
        return view;
    }

    private void initializeTextObservable() {
        RxTextView.textChanges(sourceTextView).debounce(200, TimeUnit.MILLISECONDS).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                translationQueryViewModelBehaviorSubject.onNext(new TranslationQueryViewModel(toTranslationSpinner.getSelectedItemPosition(),
                                                                                              fromTranslationSpinner.getSelectedItemPosition(),
                                                                                              charSequence.toString()));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        translatePresenter.resume();
    }

    private void initializeReverseButton() {
        reverseTranslationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translatePresenter.OnReverseTranslationButtonClicked();
            }
        });
    }

    @Override
    public int getTranslationTo() {
        return toTranslationSpinner.getSelectedItemPosition();
    }

    @Override
    public void setTranslationTo(int To) {
        toTranslationSpinner.setSelection(To, false);
    }

    @Override
    public int getTranslationFrom() {
        return fromTranslationSpinner.getSelectedItemPosition();
    }

    @Override
    public void setTranslationFrom(int From) {
        fromTranslationSpinner.setSelection(From, false);
    }


    @Override
    public Observable<TranslationQueryViewModel> getQueries() {
        return translationQueryViewModelBehaviorSubject;
    }

    @Override
    public void setSupportedLanguages(List<String> supportedLanguages) {
        Log.d(TAG, "SetSupportedLanguages called");
        ArrayAdapter<String> spinnersAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, supportedLanguages);

        spinnersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toTranslationSpinner.setAdapter(spinnersAdapter);
        fromTranslationSpinner.setAdapter(spinnersAdapter);

        fromTranslationSpinner.setSelection(0);
        toTranslationSpinner.setSelection(1);
        SpinnerInteractionListener listener = new SpinnerInteractionListener();
        toTranslationSpinner.setOnItemSelectedListener(listener );
        toTranslationSpinner.setOnTouchListener(listener );
        fromTranslationSpinner.setOnItemSelectedListener(listener);
        fromTranslationSpinner.setOnTouchListener(listener);


        initializeTextObservable();
    }

    public void setTranslation(String translation) {
        translationTextView.setText(translation);
    }

    @Override
    public void setExpandedTranslation(Definition expandedTranslation) {
        if (expandedTranslation != null) {
            expandedTranslationTextView.setVisibility(View.VISIBLE);
            expandedTranslationTextView.setText(expandedTranslation.getText());
        } else {
            expandedTranslationTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void hideError() {

    }

    /**
     * Simple class to tell whether user clicked on spinner or it was set programmatically
     */
    private class SpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

        private boolean userSelected = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelected = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userSelected) {
                translationQueryViewModelBehaviorSubject.onNext(
                        new TranslationQueryViewModel(toTranslationSpinner.getSelectedItemPosition(),
                                                      fromTranslationSpinner.getSelectedItemPosition(),
                                                      sourceTextView.getText().toString()));
                userSelected = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
