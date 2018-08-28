package it.and.stez78.bakingapp.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import it.and.stez78.bakingapp.model.Ingredient;
import it.and.stez78.bakingapp.model.Recipe;
import it.and.stez78.bakingapp.repository.RecipesRepository;

public class WidgetConfigurationViewModel extends ViewModel {

    private List<Recipe> recipes = new ArrayList<>();
    private List<String> recipeNames = new ArrayList<>();
    private Recipe selectedRecipe;
    private RecipesRepository recipesRepository;
    private LiveData<List<Recipe>> recipesLiveData;
    private int widgetId;

    @Inject
    public WidgetConfigurationViewModel(RecipesRepository recipesRepository) {
        this.recipesRepository = recipesRepository;
        this.recipesLiveData = recipesRepository.getRecipiesFromDb();
    }

    public void init(int widgetId) {
        this.widgetId = widgetId;
    }

    public LiveData<List<Recipe>> getRecipesLiveData() {
        return recipesLiveData;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes.clear();
        this.recipeNames.clear();
        this.recipes.addAll(recipes);
        for (Recipe r : recipes) {
            this.recipeNames.add(r.getName());
        }
    }

    public List<String> getRecipeNames() {
        return recipeNames;
    }

    public void setSelectedRecipe(int pos) {
        selectedRecipe = recipes.get(pos);
    }

    public Recipe getSelectedRecipe() {
        return selectedRecipe;
    }

    public LiveData<List<Ingredient>> getIngredientsForSelectedRecipe() {
        return recipesRepository.getIngredientsFromDb(selectedRecipe.getId());
    }

    public int getWidgetId() {
        return widgetId;
    }
}