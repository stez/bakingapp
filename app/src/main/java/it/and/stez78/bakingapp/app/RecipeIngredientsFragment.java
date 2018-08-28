package it.and.stez78.bakingapp.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.and.stez78.bakingapp.R;
import it.and.stez78.bakingapp.app.adapter.ElementIngredientAdapter;
import it.and.stez78.bakingapp.model.Ingredient;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeIngredientsFragment extends Fragment {

    @BindView(R.id.fragment_recipe_ingredients_rw)
    RecyclerView ingredientsRecyclerView;

    private List<Ingredient> ingredients = new ArrayList<>();

    private ElementIngredientAdapter ingredientAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public RecipeIngredientsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);
        ButterKnife.bind(this, rootView);
        layoutManager = new LinearLayoutManager(getActivity());
        ingredientsRecyclerView.setLayoutManager(layoutManager);
        ingredientAdapter = new ElementIngredientAdapter(ingredients);
        ingredientsRecyclerView.setAdapter(ingredientAdapter);
        return rootView;
    }

    public void setIngredients(List<Ingredient> ing) {
        this.ingredients = ing;
    }

}
