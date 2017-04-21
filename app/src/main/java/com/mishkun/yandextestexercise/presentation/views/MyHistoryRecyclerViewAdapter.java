package com.mishkun.yandextestexercise.presentation.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mishkun.yandextestexercise.R;
import com.mishkun.yandextestexercise.domain.entities.HistoryItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter}
 * TODO: Replace the implementation with code for your data type.
 */
public class MyHistoryRecyclerViewAdapter extends RecyclerView.Adapter<MyHistoryRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = MyHistoryRecyclerViewAdapter.class.getSimpleName();
    private final List<HistoryItem> values;

    private final FavButtonListener listener;

    public MyHistoryRecyclerViewAdapter(List<HistoryItem> values, FavButtonListener listener) {
        this.values = values;
        this.listener = listener;
    }

    public void update(List<HistoryItem> data) {
        values.clear();
        values.addAll(data);
    }

    @Override
    public MyHistoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_history, parent, false);
        return new MyHistoryRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.definitionItem = values.get(position);
        holder.text.setText(values.get(position).getOriginal());
        holder.translation.setText(values.get(position).getShortTranslation());
        holder.favButton.setChecked(values.get(position).isFavored());
        holder.favButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.favButtonChecked(holder.definitionItem, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        @BindView(R.id.translation_text_history)
        public TextView text;
        @BindView(R.id.original_text_history)
        public TextView translation;
        @BindView(R.id.favorite_button_history)
        public ToggleButton favButton;

        public HistoryItem definitionItem;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
