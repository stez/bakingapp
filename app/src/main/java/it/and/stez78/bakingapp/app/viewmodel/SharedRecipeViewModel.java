package it.and.stez78.bakingapp.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.android.exoplayer2.C;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import it.and.stez78.bakingapp.model.Ingredient;
import it.and.stez78.bakingapp.model.Step;
import it.and.stez78.bakingapp.repository.RecipesRepository;

public class SharedRecipeViewModel extends ViewModel {

    private RecipesRepository recipesRepository;
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Step> steps = new ArrayList<>();
    private List<Integer> playerWindows = new ArrayList<>();
    private List<Long> playerPositions = new ArrayList<>();
    private MutableLiveData<Integer> currentStep = new MutableLiveData<>();
    private Integer prevStepIndex;

    @Inject
    public SharedRecipeViewModel(RecipesRepository recipesRepository) {
        this.recipesRepository = recipesRepository;
    }

    public LiveData<List<Ingredient>> getIngredients(Long recipeId) {
        return recipesRepository.getIngredientsFromDb(recipeId);
    }

    public LiveData<List<Step>> getStepsLiveData(Long recipeId) {
        return recipesRepository.getStepsFromDb(recipeId);
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
        for (Step s : steps) {
            playerWindows.add(C.INDEX_UNSET);
            playerPositions.add(C.TIME_UNSET);
        }
        currentStep.setValue(0);
        prevStepIndex = null;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public int getPlayerWindow() {
        return playerWindows.get(currentStep.getValue());
    }

    public void setPlayerWindow(int playerWindow) {
        this.playerWindows.set(currentStep.getValue(), playerWindow);
    }

    public void setPlayerWindow(int stepIndex, int playerWindow) {
        this.playerWindows.set(stepIndex, playerWindow);
    }

    public Long getPlayerPosition() {
        return playerPositions.get(currentStep.getValue());
    }

    public void setPlayerPosition(Long playerPosition) {
        this.playerPositions.set(currentStep.getValue(), playerPosition);
    }

    public void setPlayerPosition(int stepIndex, Long playerPosition) {
        this.playerPositions.set(stepIndex, playerPosition);
    }

    public MutableLiveData<Integer> getCurrentStepLiveData() {
        return currentStep;
    }

    public Step getCurrentStep() {
        int current = currentStep.getValue();
        return steps.get(current);
    }

    public int getCurrentStepIndex() {
        return steps.indexOf(getCurrentStep());
    }

    public void setCurrentStep(Step step) {
        prevStepIndex = currentStep.getValue();
        currentStep.setValue(steps.indexOf(step));
    }

    public void setCurrentStep(int index) {
        prevStepIndex = currentStep.getValue();
        currentStep.setValue(index);
    }

    public Integer getPrevStepIndex() {
        return prevStepIndex;
    }
}
