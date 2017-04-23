package com.mishkun.yandextestexercise.presentation.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mishkun.yandextestexercise.AndroidApplication;
import com.mishkun.yandextestexercise.R;
import com.mishkun.yandextestexercise.domain.entities.Definition;
import com.mishkun.yandextestexercise.domain.entities.HistoryItem;
import com.mishkun.yandextestexercise.domain.entities.Language;
import com.mishkun.yandextestexercise.presentation.presenters.TranslatePresenter;
import com.mishkun.yandextestexercise.presentation.views.ExpandedTranslationAdapter;
import com.mishkun.yandextestexercise.presentation.views.FavButtonListener;
import com.mishkun.yandextestexercise.presentation.views.HistoryRecyclerViewAdapter;
import com.mishkun.yandextestexercise.presentation.views.TranslateView;
import com.mishkun.yandextestexercise.presentation.views.TranslationQueryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;


/**
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment implements TranslateView, FavButtonListener {

    private static final String KEY_SOURCE_TEXT = "SOURCE_TEXT";
    private static final String KEY_FROM_DIRECTION = "FROM_DIRECTION";
    private static final String KEY_TO_DIRECTION = "TO_DIRECTION";


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
    @BindView(R.id.clear_button)
    public Button clearButton;
    @BindView(R.id.history_card)
    public CardView historyCard;
    @BindView(R.id.history_list)
    public RecyclerView historyRecyclerView;
    @BindView(R.id.favorite_btn)
    public ToggleButton favoriteToggle;
    @BindView(R.id.loadingBar)
    public ProgressBar loadingBar;
    @Inject
    public TranslatePresenter translatePresenter;
    private PublishSubject<TranslationQueryViewModel> translationQueryViewModelBehaviorSubject;
    private ExpandedTranslationAdapter expandedTranslationAdapter;
    private HistoryRecyclerViewAdapter historyRecyclerViewAdapter;
    private ArrayAdapter<Language> spinnersAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */

    public static HomeFragment newInstance(Bundle args) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static HomeFragment newInstance(String sourceText, String translationFrom, String translationTo) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(KEY_SOURCE_TEXT, sourceText);
        args.putString(KEY_FROM_DIRECTION, translationFrom);
        args.putString(KEY_TO_DIRECTION, translationTo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: " + savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AndroidApplication) getActivity().getApplication()).getApplicationComponent().inject(this);
        translationQueryViewModelBehaviorSubject = PublishSubject.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            sourceTextView.setText(getArguments().getString(KEY_SOURCE_TEXT));
        }

        favoriteToggle.setOnClickListener(new CompoundButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                translatePresenter.onFavored(new HistoryItem(sourceTextView.getText().toString(), translationTextView.getText().toString(), false),
                                             favoriteToggle.isChecked());
            }
        });

        expandedTranslationCard.setVisibility(View.GONE);
        translationCard.setVisibility(View.GONE);
        historyCard.setVisibility(View.VISIBLE);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(getContext(),
                                                                               DividerItemDecoration.VERTICAL);
        horizontalDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));

        expandedTranslationAdapter = new ExpandedTranslationAdapter(new ArrayList<Definition.DefinitionItem>());
        expandedTranslationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        expandedTranslationRecyclerView.setAdapter(expandedTranslationAdapter);
        expandedTranslationRecyclerView.addItemDecoration(horizontalDecoration);

        List<HistoryItem> historyItemsDummy = new ArrayList<>();


        historyRecyclerViewAdapter = new HistoryRecyclerViewAdapter(historyItemsDummy, this);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        historyRecyclerView.setAdapter(historyRecyclerViewAdapter);
        historyRecyclerView.addItemDecoration(horizontalDecoration);
        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        // callback for drag-n-drop, false to skip this feature
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        translatePresenter.onHistoryDismissed(historyRecyclerViewAdapter.getItemAt(viewHolder.getAdapterPosition()));
                    }
                });
        swipeToDismissTouchHelper.attachToRecyclerView(historyRecyclerView);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sourceTextView.setText("");
            }
        });
        initializeReverseButton();


        translatePresenter.attachView(this);
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
    public Language getTranslationTo() {
        return (Language) toTranslationSpinner.getSelectedItem();
    }

    @Override
    public void setTranslationTo(Language To) {
        toTranslationSpinner.setSelection(spinnersAdapter.getPosition(To));
    }

    @Override
    public Language getTranslationFrom() {
        return (Language) fromTranslationSpinner.getSelectedItem();
    }

    @Override
    public void setTranslationFrom(Language From) {
        fromTranslationSpinner.setSelection(spinnersAdapter.getPosition(From));
    }

    @Override
    public boolean getGuessLanguage() {
        return guessToogle.isChecked();
    }

    @Override
    public void showProgressBar() {
        loadingBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        loadingBar.setVisibility(View.GONE);
    }

    @Override
    public void reverseText() {
        sourceTextView.setText(translationTextView.getText());
    }

    @Override
    public Observable<TranslationQueryViewModel> getQueries() {
        return translationQueryViewModelBehaviorSubject.observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void setSupportedLanguages(List<Language> supportedLanguages) {
        Log.d(TAG, "SetSupportedLanguages called");
        spinnersAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, supportedLanguages);

        spinnersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toTranslationSpinner.setAdapter(spinnersAdapter);
        fromTranslationSpinner.setAdapter(spinnersAdapter);

        if (getArguments() == null) {

            fromTranslationSpinner.setSelection(spinnersAdapter.getPosition(new Language("ru", null)));
            toTranslationSpinner.setSelection(spinnersAdapter.getPosition(new Language("en", null)));
        } else {
            fromTranslationSpinner.setSelection(spinnersAdapter.getPosition(new Language(getArguments().getString(KEY_FROM_DIRECTION), null)));
            toTranslationSpinner.setSelection(spinnersAdapter.getPosition(new Language(getArguments().getString(KEY_TO_DIRECTION), null)));
        }
        SpinnerInteractionListener listener = new SpinnerInteractionListener();
        toTranslationSpinner.setOnItemSelectedListener(listener);
        toTranslationSpinner.setOnTouchListener(listener);
        fromTranslationSpinner.setOnItemSelectedListener(listener);
        fromTranslationSpinner.setOnTouchListener(listener);

        initializeTextObservable();
    }

    public void setTranslation(String translation) {
        if (translation != null && !translation.equals("")) {
            historyCard.setVisibility(View.GONE);
            translationCard.setVisibility(View.VISIBLE);
            translationTextView.setText(translation);
        } else {
            translatePresenter.getHistory();
            historyCard.setVisibility(View.VISIBLE);
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
    public void setIsCurrentTranslationFavored(boolean favored) {
        favoriteToggle.setChecked(favored);
    }


    @NonNull
    private TranslationQueryViewModel getTranslationViewModel() {
        return new TranslationQueryViewModel((Language) fromTranslationSpinner.getSelectedItem(),
                                             (Language) toTranslationSpinner.getSelectedItem(),
                                             sourceTextView.getText().toString());
    }

    @Override
    public void favButtonChecked(HistoryItem item, boolean favored) {
        translatePresenter.onFavored(item, favored);
    }

    @Override
    public void setData(List<HistoryItem> historyItems) {
        historyRecyclerViewAdapter.update(historyItems);
        historyRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: ");
        outState.putString(KEY_SOURCE_TEXT, sourceTextView.getText().toString());
        outState.putString(KEY_FROM_DIRECTION, ((Language) fromTranslationSpinner.getSelectedItem()).getCode());
        outState.putString(KEY_TO_DIRECTION, ((Language) toTranslationSpinner.getSelectedItem()).getCode());
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
