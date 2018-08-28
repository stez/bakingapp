package it.and.stez78.bakingapp.app.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import it.and.stez78.bakingapp.R;
import it.and.stez78.bakingapp.model.Step;

public class ElementStepAdapter extends RecyclerView.Adapter<ElementStepAdapter.ViewHolder> {

    private static final String TAG = "ElStepAdapter";

    private Context context;
    private List<Step> dataSet;
    private OnStepClickListener stepClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardContainer;
        private TextView title;

        public ViewHolder(View v) {
            super(v);
            cardContainer = v.findViewById(R.id.list_element_step_card);
            title = v.findViewById(R.id.list_element_step_title);
        }

        public CardView getCardContainer() {
            return cardContainer;
        }

        public TextView getTitle() {
            return title;
        }

    }

    public ElementStepAdapter(Context context, List<Step> dataSet, OnStepClickListener stepClickListener) {
        this.context = context;
        this.dataSet = dataSet;
        this.stepClickListener = stepClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_element_step, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Step el = dataSet.get(position);
        int index = position + 1;
        Resources res = context.getResources();
        String text = String.format(res.getString(R.string.list_element_step), index, el.getShortDescription());
        viewHolder.getTitle().setText(text);

        if (el.isActive()) {
            viewHolder.getCardContainer().setBackgroundColor(Color.LTGRAY);
        } else {
            viewHolder.getCardContainer().setBackgroundColor(Color.WHITE);
        }

        viewHolder.getCardContainer().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepClickListener.OnStepClick(el);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
