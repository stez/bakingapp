package it.and.stez78.bakingapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import it.and.stez78.bakingapp.db.dao.IngredientsDao;
import it.and.stez78.bakingapp.db.dao.RecipesDao;
import it.and.stez78.bakingapp.db.dao.StepsDao;
import it.and.stez78.bakingapp.model.Ingredient;
import it.and.stez78.bakingapp.model.Recipe;
import it.and.stez78.bakingapp.model.Step;

/**
 * Created by Stefano Zanotti on 24/11/2017.
 */
@Database(entities = {
        Recipe.class,
        Ingredient.class,
        Step.class},
        version = 4)

public abstract class AppDatabase extends RoomDatabase {

    abstract public RecipesDao recipesDao();

    abstract public IngredientsDao ingredientsDao();

    abstract public StepsDao stepsDao();

}