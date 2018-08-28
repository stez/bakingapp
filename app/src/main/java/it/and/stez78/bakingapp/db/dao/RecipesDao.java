package it.and.stez78.bakingapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import it.and.stez78.bakingapp.model.Recipe;

@Dao
public interface RecipesDao {

    @Query("SELECT COUNT(*) FROM recipes")
    Integer getRecepicesCount();

    @Query("SELECT * FROM recipes")
    LiveData<List<Recipe>> getAllRecipies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<Recipe> recipes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertSingle(Recipe recipe);

    @Query("DELETE from recipes")
    public void deleteAll();
}
