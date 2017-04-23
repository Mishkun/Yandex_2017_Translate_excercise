package com.mishkun.yandextestexercise.presentation.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mishkun.yandextestexercise.R;
import com.mishkun.yandextestexercise.domain.entities.Definition;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mishkun on 19.04.2017.
 */

public class ExpandedTranslationAdapter extends RecyclerView.Adapter<ExpandedTranslationAdapter.ViewHolder> {

    private static final String TAG = ExpandedTranslationAdapter.class.getSimpleName();
    private final List<Definition.DefinitionItem> values;

    public ExpandedTranslationAdapter(List<Definition.DefinitionItem> values) {
        this.values = values;
    }

    public void update(List<Definition.DefinitionItem> data) {
        values.clear();
        values.addAll(data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_expanded_translation, parent, false);
        return new ExpandedTranslationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.definitionItem = values.get(position);
        holder.synonyms.setText(join(values.get(position).getSynonyms()));
        holder.number.setText(String.format(Locale.ENGLISH, "%d.", position + 1));
        if (values.get(position).getMeanings() != null && values.get(position).getMeanings().size() > 0) {
            holder.meanings.setText(join(values.get(position).getMeanings()));
        } else {
            holder.meanings.setVisibility(View.GONE);
        }
    }

    private String join(List<String> strings) {
        if (strings == null) {
            return "";
        }
        String result = strings.get(0);

        // Not using stringbuilder, because strings.size() < 10
        for (String string : strings.subList(1, strings.size())) {
            result = result + ", " + string;
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        @BindView(R.id.meanings)
        public  TextView meanings;
        @BindView(R.id.synonyms)
        public  TextView synonyms;
        @BindView(R.id.translations_number)
        public  TextView number;
        public Definition.DefinitionItem definitionItem;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
