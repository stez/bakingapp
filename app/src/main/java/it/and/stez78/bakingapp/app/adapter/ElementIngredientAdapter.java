package it.and.stez78.bakingapp.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import it.and.stez78.bakingapp.R;
import it.and.stez78.bakingapp.model.Ingredient;

public class ElementIngredientAdapter extends RecyclerView.Adapter<ElementIngredientAdapter.ViewHolder> {


    private static final String TAG = "ElIngredientAdapter";

    private List<Ingredient> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView description;
        private TextView quantity;
        private TextView mesure;

        public ViewHolder(View v) {
            super(v);
            description = v.findViewById(R.id.list_element_ingredient_description);
            quantity = v.findViewById(R.id.list_element_ingredient_quantity);
            mesure = v.findViewById(R.id.list_element_ingredient_measure);
        }

        public TextView getDescription() {
            return description;
        }

        public TextView getQuantity() {
            return quantity;
        }

        public TextView getMesure() {
            return mesure;
        }
    }

    public ElementIngredientAdapter(List<Ingredient> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_element_ingredient, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Ingredient el = dataSet.get(position);
        viewHolder.getDescription().setText(el.getIngredient());
        viewHolder.getQuantity().setText(el.getQuantity().toString());
        viewHolder.getMesure().setText(el.getMeasure());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
