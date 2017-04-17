package com.mishkun.yandextestexercise.presentation.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mishkun.yandextestexercise.R;
import com.mishkun.yandextestexercise.di.components.MainActivityComponent;
import com.mishkun.yandextestexercise.domain.entities.Definition;
import com.mishkun.yandextestexercise.presentation.presenters.TranslatePresenter;
import com.mishkun.yandextestexercise.presentation.views.TranslateView;

import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.Observable;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.functions.Function;


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
        initializeReverseButton();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        translatePresenter.attachView(this);
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
        toTranslationSpinner.setSelection(To);
    }

    @Override
    public int getTranslationFrom() {
        return fromTranslationSpinner.getSelectedItemPosition();
    }

    @Override
    public void setTranslationFrom(int From) {
        fromTranslationSpinner.setSelection(From);
    }

    @Override
    public String getTextToTranslate() {
        return sourceTextView.getText().toString();
    }

    @Override
    public void setSupportedLanguages(List<String> supportedLanguages) {
        ArrayAdapter<String> spinnersAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, supportedLanguages);

        spinnersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toTranslationSpinner.setAdapter(spinnersAdapter);
        fromTranslationSpinner.setAdapter(spinnersAdapter);

        toTranslationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                translatePresenter.onQueryChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fromTranslationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                translatePresenter.onQueryChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void setTranslation(String translation) {
        translationTextView.setText(translation);
    }

    @Override
    public void setExpandedTranslation(Definition expandedTranslation) {
        expandedTranslationTextView.setText(expandedTranslation.getText());
    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void hideError() {

    }
}
