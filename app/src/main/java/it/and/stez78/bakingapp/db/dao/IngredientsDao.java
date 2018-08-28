package it.and.stez78.bakingapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import it.and.stez78.bakingapp.model.Ingredient;

@Dao
public interface IngredientsDao {

    @Query("SELECT * FROM ingredients WHERE recipeId = :id")
    LiveData<List<Ingredient>> getAllIngredientsForRecipe(Long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<Ingredient> ingredients);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertSingle(Ingredient ingredient);

    @Query("DELETE from ingredients")
    public void deleteAll();
}
