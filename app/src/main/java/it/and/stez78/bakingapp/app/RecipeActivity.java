package it.and.stez78.bakingapp.app;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import it.and.stez78.bakingapp.R;
import it.and.stez78.bakingapp.app.adapter.OnStepClickListener;
import it.and.stez78.bakingapp.app.viewmodel.SharedRecipeViewModel;
import it.and.stez78.bakingapp.app.viewmodel.ViewModelFactory;
import it.and.stez78.bakingapp.model.Recipe;
import it.and.stez78.bakingapp.model.Step;

public class RecipeActivity extends AppCompatActivity implements OnStepClickListener, HasSupportFragmentInjector {

    public static final String RECIPE_KEY = "recipeParcel";
    public static final String CURRENT_STEP_KEY = "currentStep";

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @BindView(R.id.activity_recipe_ingredients_container)
    FrameLayout ingredientsContainer;

    @BindView(R.id.activity_recipe_steps_container)
    FrameLayout stepsContainer;

    @BindView(R.id.activity_recipe_toolbar)
    Toolbar toolbar;

    @Inject
    ViewModelFactory viewModelFactory;
    SharedRecipeViewModel viewModel;

    private RecipeIngredientsFragment recipeIngredientsFragment = new RecipeIngredientsFragment();
    RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
    private StepFragment stepFragment = new StepFragment();
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SharedRecipeViewModel.class);
        if (getIntent().hasExtra(RECIPE_KEY)) {
            recipe = getIntent().getParcelableExtra(RECIPE_KEY);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(recipe.getName());
            }
            FragmentManager fragmentManager = getSupportFragmentManager();

            viewModel.getIngredients(recipe.getId()).observe(this, ingredients -> {
                viewModel.setIngredients(ingredients);
                recipeIngredientsFragment.setIngredients(ingredients);
                fragmentManager.beginTransaction()
                        .replace(R.id.activity_recipe_ingredients_container, recipeIngredientsFragment)
                        .commit();
            });

            viewModel.getStepsLiveData(recipe.getId()).observe(this, steps -> {
                viewModel.setSteps(steps);
                recipeStepsFragment.setSteps(steps);
                fragmentManager.beginTransaction()
                        .replace(R.id.activity_recipe_steps_container, recipeStepsFragment)
                        .commit();
                if (getIntent().hasExtra(CURRENT_STEP_KEY)) {
                    viewModel.setCurrentStep(getIntent().getIntExtra(CURRENT_STEP_KEY, 0));
                }
            });
        }
    }

    @Override
    public void OnStepClick(Step step) {
        viewModel.setCurrentStep(step);
        if (isLargeDisplayLandscape()) {
            recipeStepsFragment.setStepActive(step);
        } else {
            Intent stepIntent = new Intent(this, StepActivity.class);
            stepIntent.putExtra(StepActivity.EXTRA_RECIPE_LABEL, recipe);
            stepIntent.putExtra(StepActivity.EXTRA_CURRENT_STEP_INDEX_LABEL, viewModel.getCurrentStepIndex());
            startActivity(stepIntent);
        }
    }

    private boolean isLargeDisplayLandscape() {
        return (findViewById(R.id.activity_recipe_step_fragment) != null);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
