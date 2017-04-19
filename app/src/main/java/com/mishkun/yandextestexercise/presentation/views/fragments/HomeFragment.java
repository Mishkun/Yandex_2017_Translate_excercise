package com.mishkun.yandextestexercise.presentation.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.ToggleButton;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mishkun.yandextestexercise.R;
import com.mishkun.yandextestexercise.di.components.MainActivityComponent;
import com.mishkun.yandextestexercise.domain.entities.Definition;
import com.mishkun.yandextestexercise.presentation.presenters.TranslatePresenter;
import com.mishkun.yandextestexercise.presentation.views.ExpandedTranslationAdapter;
import com.mishkun.yandextestexercise.presentation.views.TranslateView;
import com.mishkun.yandextestexercise.presentation.views.TranslationQueryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import io.reactivex.Observable;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.functions.Consumer;
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

    @BindView(R.id.to_translate_edit_text)
    public EditText sourceTextView;

    @BindView(R.id.translated_text_view)
    public TextView translationTextView;

    @BindView(R.id.extended_translated_text_view)
    public TextView expandedTranslationTextView;
    @Inject
    public TranslatePresenter translatePresenter;

    @BindView(R.id.translation_card)
    public CardView translationCard;

    @BindView(R.id.guess_toogle)
    public ToggleButton guessToogle;

    @BindView(R.id.transcription)
    public TextView transcriptionTextView;

    @BindView(R.id.expanded_translation_card)
    public CardView expandedTranslationCard;

    @BindView(R.id.expanded_translation_list)
    public RecyclerView expandedTranslationRecyclerView;

    private PublishSubject<TranslationQueryViewModel> translationQueryViewModelBehaviorSubject;
    private ExpandedTranslationAdapter expandedTranslationAdapter;


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

        expandedTranslationCard.setVisibility(View.GONE);
        translationCard.setVisibility(View.GONE);
        expandedTranslationAdapter = new ExpandedTranslationAdapter(new ArrayList<Definition.DefinitionItem>());
        expandedTranslationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        expandedTranslationRecyclerView.setAdapter(expandedTranslationAdapter);
        translatePresenter.attachView(this);
        initializeReverseButton();

        return view;
    }

    private void initializeTextObservable() {
        RxTextView.textChanges(sourceTextView).debounce(200, TimeUnit.MILLISECONDS).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                translationQueryViewModelBehaviorSubject.onNext(getTranslationViewModel());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        translatePresenter.resume();
        guessToogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translationQueryViewModelBehaviorSubject.onNext(getTranslationViewModel());
            }
        });
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
    public boolean getGuessLanguage() {
        return guessToogle.isChecked();
    }

    @Override
    public void reverseText() {
        sourceTextView.setText(translationTextView.getText());
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
        toTranslationSpinner.setOnItemSelectedListener(listener);
        toTranslationSpinner.setOnTouchListener(listener);
        fromTranslationSpinner.setOnItemSelectedListener(listener);
        fromTranslationSpinner.setOnTouchListener(listener);

        initializeTextObservable();
    }

    public void setTranslation(String translation) {
        if (!translation.equals("")) {
            translationCard.setVisibility(View.VISIBLE);
            translationTextView.setText(translation);
        } else {
            translationCard.setVisibility(View.GONE);
        }
    }

    @Override
    public void setExpandedTranslation(Definition expandedTranslation) {
        if (expandedTranslation.getText() != null) {
            expandedTranslationCard.setVisibility(View.VISIBLE);
            expandedTranslationTextView.setText(expandedTranslation.getText());
            expandedTranslationAdapter.update(expandedTranslation.getDefinitionItems());
            expandedTranslationAdapter.notifyDataSetChanged();
            transcriptionTextView.setText(expandedTranslation.getTranscription() == null ? null : "[" + expandedTranslation.getTranscription() + "]");
        } else {
            expandedTranslationCard.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void hideError() {

    }

    @NonNull
    private TranslationQueryViewModel getTranslationViewModel() {
        return new TranslationQueryViewModel(toTranslationSpinner.getSelectedItemPosition(),
                                             fromTranslationSpinner.getSelectedItemPosition(),
                                             sourceTextView.getText().toString());
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
                        getTranslationViewModel());
                userSelected = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
