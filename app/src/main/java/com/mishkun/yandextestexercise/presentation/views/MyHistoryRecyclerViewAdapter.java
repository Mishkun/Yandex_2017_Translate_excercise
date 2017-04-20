package com.mishkun.yandextestexercise.presentation.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mishkun.yandextestexercise.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter}
 * TODO: Replace the implementation with code for your data type.
 */
public class MyHistoryRecyclerViewAdapter extends RecyclerView.Adapter<MyHistoryRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = MyHistoryRecyclerViewAdapter.class.getSimpleName();
    private final List<TranslationResultViewModel> values;

    public MyHistoryRecyclerViewAdapter(List<TranslationResultViewModel> values) {
        this.values = values;
    }

    public void update(List<TranslationResultViewModel> data) {
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
        holder.text.setText(values.get(position).input);
        holder.translation.setText(values.get(position).translation);
        holder.favButton.setChecked(values.get(position).favored);
       /* holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the enabled callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView text;
        public final TextView translation;
        public final ToggleButton favButton;
        public TranslationResultViewModel definitionItem;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.text = (TextView) view.findViewById(R.id.translation_text_history);
            this.translation = (TextView) view.findViewById(R.id.original_text_history);
            this.favButton = (ToggleButton) view.findViewById(R.id.favorite_button_history);
        }
    }
}
