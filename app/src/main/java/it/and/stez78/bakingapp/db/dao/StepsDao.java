package it.and.stez78.bakingapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import it.and.stez78.bakingapp.model.Step;

@Dao
public interface StepsDao {

    @Query("SELECT * FROM steps WHERE recipeId = :id")
    LiveData<List<Step>> getAllStepsForRecipe(Long id);

    @Query("SELECT * FROM steps WHERE recipeId = :id")
    List<Step> getAllStepsForRecipeSync(Long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<Step> steps);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertSingle(Step step);

    @Query("DELETE from steps")
    public void deleteAll();
}
