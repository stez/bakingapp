package it.and.stez78.bakingapp.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import it.and.stez78.bakingapp.model.Recipe;
import it.and.stez78.bakingapp.repository.RecipesRepository;

/**
 * Created by stefano on 23/02/18.
 */

public class MainActivityViewModel extends ViewModel {

    private List<Recipe> recipes = new ArrayList<>();
    private RecipesRepository recipesRepository;
    private LiveData<List<Recipe>> recipesLiveData;
    private boolean firstRun = true;

    @Inject
    public MainActivityViewModel(RecipesRepository recipesRepository) {
        this.recipesRepository = recipesRepository;
        this.recipesLiveData = recipesRepository.getRecipiesFromDb();
    }

    public LiveData<List<Recipe>> getRecipesLiveData() {
        return recipesLiveData;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes.clear();
        this.recipes.addAll(recipes);
    }

    public void updateRecipes() {
        recipesRepository.updateRecipies();
        firstRun = false;
    }

    public RecipesRepository getRecipesRepository() {
        return recipesRepository;
    }

    public boolean isFirstRun() {
        return firstRun;
    }
}
