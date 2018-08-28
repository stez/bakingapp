package it.and.stez78.bakingapp.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.and.stez78.bakingapp.BuildConfig;
import it.and.stez78.bakingapp.db.AppDatabase;
import it.and.stez78.bakingapp.db.dao.IngredientsDao;
import it.and.stez78.bakingapp.db.dao.RecipesDao;
import it.and.stez78.bakingapp.db.dao.StepsDao;
import it.and.stez78.bakingapp.network.RecipesApiService;
import it.and.stez78.bakingapp.utils.RecipesApiUtils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by stefano on 18/02/18.
 */
@Module
public class AppModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG)
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        else interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    @Provides
    @Singleton
    Picasso providePicasso(Application ctx) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG)
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        else interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        return new Picasso.Builder(ctx)
                .downloader(new OkHttp3Downloader(client))
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(RecipesApiUtils.RECIPE_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    RecipesApiService provideRecipesApiService(Retrofit retrofit) {
        return retrofit.create(RecipesApiService.class);
    }

    @Singleton
    @Provides
    AppDatabase provideDb(Application app) {
        return Room
                .databaseBuilder(app, AppDatabase.class, "bakingApp.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    RecipesDao provideRecipesDao(AppDatabase db) {
        return db.recipesDao();
    }

    @Singleton
    @Provides
    IngredientsDao provideIngredientsDao(AppDatabase db) {
        return db.ingredientsDao();
    }

    @Singleton
    @Provides
    StepsDao provideStepsDao(AppDatabase db) {
        return db.stepsDao();
    }

}
