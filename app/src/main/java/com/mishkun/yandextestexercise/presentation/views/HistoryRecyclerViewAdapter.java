package com.mishkun.yandextestexercise.presentation.views;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mishkun.yandextestexercise.R;
import com.mishkun.yandextestexercise.domain.entities.ShortTranslationModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter}
 * TODO: Replace the implementation with code for your data type.
 */
public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = HistoryRecyclerViewAdapter.class.getSimpleName();
    private final List<ShortTranslationModel> values;

    private final FavButtonListener favButtonListener;
    private final ItemClickListener<ShortTranslationModel> clicksListener;

    public HistoryRecyclerViewAdapter(List<ShortTranslationModel> values, FavButtonListener favButtonListener, ItemClickListener<ShortTranslationModel> clicksListener) {
        this.values = values;
        this.favButtonListener = favButtonListener;
        this.clicksListener = clicksListener;
    }

    public void update(List<ShortTranslationModel> data) {
        values.clear();
        values.addAll(data);
        Log.d(TAG, "update: " + data.size());
    }

    @Override
    public HistoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_history, parent, false);
        return new HistoryRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.definitionItem = values.get(position);
        holder.text.setText(values.get(position).getOriginal());
        Log.d(TAG, "onBindViewHolder: " + holder.text.getText().toString() + " " + position);
        holder.translation.setText(values.get(position).getTranslation());
        holder.favButton.setOnCheckedChangeListener(null);
        holder.favButton.setChecked(values.get(position).isFavored());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicksListener.onClicked(holder.definitionItem);
            }
        });
        holder.favButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                favButtonListener.favButtonChecked(holder.definitionItem, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public ShortTranslationModel getItemAt(int adapterPosition) {
        return values.get(adapterPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        @BindView(R.id.translation_text_history)
        public TextView translation;
        @BindView(R.id.original_text_history)
        public TextView text;
        @BindView(R.id.favorite_button_history)
        public ToggleButton favButton;

        public ShortTranslationModel definitionItem;

        public ViewHolder(View itemView) {
            super(itemView);

            Log.d(TAG, "onCreateViewHolder: ");
            this.view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
