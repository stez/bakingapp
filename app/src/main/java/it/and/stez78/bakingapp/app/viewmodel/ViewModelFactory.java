package it.and.stez78.bakingapp.app.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import it.and.stez78.bakingapp.repository.RecipesRepository;

/**
 * Created by stefano on 23/02/18.
 */

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final RecipesRepository recipesRepository;

    @Inject
    public ViewModelFactory(RecipesRepository recipesRepository) {
        this.recipesRepository = recipesRepository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(recipesRepository);
        }
        if (modelClass.isAssignableFrom(SharedRecipeViewModel.class)) {
            return (T) new SharedRecipeViewModel(recipesRepository);
        }
        if (modelClass.isAssignableFrom(WidgetConfigurationViewModel.class)) {
            return (T) new WidgetConfigurationViewModel(recipesRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}