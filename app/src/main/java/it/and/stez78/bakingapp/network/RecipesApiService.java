package it.and.stez78.bakingapp.network;

import java.util.List;

import io.reactivex.Observable;
import it.and.stez78.bakingapp.model.Recipe;
import retrofit2.http.GET;

public interface RecipesApiService {

    // http://go.udacity.com/android-baking-app-json
    @GET("android-baking-app-json")
    Observable<List<Recipe>> getRecipies();
}
