package it.and.stez78.bakingapp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import it.and.stez78.bakingapp.db.dao.IngredientsDao;
import it.and.stez78.bakingapp.db.dao.RecipesDao;
import it.and.stez78.bakingapp.db.dao.StepsDao;
import it.and.stez78.bakingapp.model.Ingredient;
import it.and.stez78.bakingapp.model.Recipe;
import it.and.stez78.bakingapp.model.Step;
import it.and.stez78.bakingapp.network.RecipesApiService;
import timber.log.Timber;

/**
 * Created by stefano on 19/03/18.
 * Repository that holds all the methods to access and manipulates Movies, Videos and Reviews
 * Mostly produces LiveData for the getters part
 */

public class RecipesRepository {

    private Context context;
    private RecipesApiService recipesApiService;
    private RecipesDao recipesDao;
    private IngredientsDao ingredientsDao;
    private StepsDao stepsDao;

    @Inject
    public RecipesRepository(Application context, RecipesApiService recipesApiService,
                             RecipesDao recipesDao, IngredientsDao ingredientsDao, StepsDao stepsDao) {
        this.context = context;
        this.recipesApiService = recipesApiService;
        this.recipesDao = recipesDao;
        this.ingredientsDao = ingredientsDao;
        this.stepsDao = stepsDao;
    }

    public void updateRecipies() {
        recipesApiService.getRecipies().subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(result -> {
                    recipesDao.deleteAll();
                    ingredientsDao.deleteAll();
                    stepsDao.deleteAll();
                    List<Recipe> recipes = result;
                    for (Recipe r : recipes) {
                        List<Ingredient> ingredients = r.getIngredients();
                        List<Step> steps = r.getSteps();
                        for (Ingredient i : ingredients) {
                            i.setRecipeId(r.getId());
                            ingredientsDao.insertSingle(i);
                        }
                        for (Step s : steps) {
                            s.setRecipeId(r.getId());
                            stepsDao.insertSingle(s);
                        }
                        recipesDao.insertSingle(r);
                    }
                }, error -> {
                    Timber.d("ERROR: " + error.getMessage());
                });
    }

    public LiveData<List<Recipe>> getRecipiesFromDb() {
        return recipesDao.getAllRecipies();
    }

    public LiveData<List<Ingredient>> getIngredientsFromDb(Long recipeId) {
        return ingredientsDao.getAllIngredientsForRecipe(recipeId);
    }

    public LiveData<List<Step>> getStepsFromDb(Long recipeId) {
        return stepsDao.getAllStepsForRecipe(recipeId);
    }

    public Observable<String> getThumbUrlFromRecipeId(Long recipeId) {
        return Observable.create(emitter -> {
            String thumbUrl = stepsDao.getAllStepsForRecipeSync(recipeId)
                    .stream()
                    .sorted((o1, o2) -> o2.getPosition() - o1.getPosition())
                    .filter(step -> step.getThumbnailURL().isEmpty())
                    .map(Step::getThumbnailURL)
                    .findFirst().orElse("");
            emitter.onNext(thumbUrl);
            emitter.onComplete();
        });
    }
}
