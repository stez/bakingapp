package it.and.stez78.bakingapp.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import it.and.stez78.bakingapp.app.MainActivity;
import it.and.stez78.bakingapp.app.RecipeActivity;
import it.and.stez78.bakingapp.app.StepActivity;
import it.and.stez78.bakingapp.app.WidgetConfigurationActivity;

/**
 * Created by stefano on 19/02/18.
 */

@Module
public abstract class ActivitiesModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivityInjector();

    @ContributesAndroidInjector
    abstract RecipeActivity contributeRecipeActivityInjector();

    @ContributesAndroidInjector
    abstract StepActivity contributeStepctivityInjector();

    @ContributesAndroidInjector
    abstract WidgetConfigurationActivity contributeWidgetConfigurationInjector();

}