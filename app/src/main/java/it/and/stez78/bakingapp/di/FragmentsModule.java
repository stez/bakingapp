package it.and.stez78.bakingapp.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import it.and.stez78.bakingapp.app.StepFragment;

@Module
public abstract class FragmentsModule {

    @ContributesAndroidInjector
    abstract StepFragment contributeStepFragment();
}
