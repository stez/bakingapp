package it.and.stez78.bakingapp.app;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import it.and.stez78.bakingapp.R;
import it.and.stez78.bakingapp.app.viewmodel.SharedRecipeViewModel;
import it.and.stez78.bakingapp.app.viewmodel.ViewModelFactory;
import it.and.stez78.bakingapp.model.Recipe;

public class StepActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    public static final String EXTRA_RECIPE_LABEL = "recipe";
    public static final String EXTRA_CURRENT_STEP_INDEX_LABEL = "currentStepIndex";

    @BindView(R.id.activity_step_next_button)
    Button nextButton;

    @BindView(R.id.activity_step_prev_button)
    Button prevButton;

    @BindView(R.id.activity_step_toolbar)
    Toolbar toolbar;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    ViewModelFactory viewModelFactory;
    SharedRecipeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SharedRecipeViewModel.class);
        Recipe recipe = getIntent().getParcelableExtra(EXTRA_RECIPE_LABEL);
        int currentStepIndex = getIntent().getIntExtra(EXTRA_CURRENT_STEP_INDEX_LABEL, 0);
        if (recipe != null && viewModel.getSteps().isEmpty()) {
            viewModel.getStepsLiveData(recipe.getId()).observe(this, steps -> {
                viewModel.setSteps(steps);
                viewModel.setCurrentStep(currentStepIndex);
                setupUI(recipe);
            });
        } else {
            setupUI(recipe);
        }
    }

    private void setupUI(Recipe recipe) {
        if (isLargeDisplayLandscape()) {
            Intent recipeIntent = new Intent(this, RecipeActivity.class);
            recipeIntent.putExtra(RecipeActivity.RECIPE_KEY, recipe);
            recipeIntent.putExtra(RecipeActivity.CURRENT_STEP_KEY, viewModel.getCurrentStepIndex());
            startActivity(recipeIntent);
        } else {
            setupBottomNavigation();
            fillActionBarTitle();
            StepFragment stepFragment = new StepFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_step_container, stepFragment)
                    .commit();
        }
    }

    private void setupBottomNavigation() {
        int currentPosition = viewModel.getCurrentStepIndex();
        prevButton.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);
        if (currentPosition == 0) {
            prevButton.setVisibility(View.INVISIBLE);
        }
        if (currentPosition == viewModel.getSteps().size() - 1) {
            nextButton.setVisibility(View.INVISIBLE);
        }
    }

    private void fillActionBarTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            int currentPosition = viewModel.getCurrentStep().getPosition();
            String title = viewModel.getCurrentStep().getShortDescription();
            if (currentPosition != 0) {
                title = viewModel.getCurrentStep().getPosition() + "/" + (viewModel.getSteps().size() - 1) + " " + viewModel.getCurrentStep().getShortDescription();
            }
            actionBar.setTitle(title);
        }
    }

    private boolean isLargeDisplayLandscape() {
        return (findViewById(R.id.activity_step_landscape_large) != null);
    }

    @OnClick(R.id.activity_step_next_button)
    public void nextStep() {
        int nextStepIndex = viewModel.getCurrentStepIndex() + 1;
        viewModel.setCurrentStep(nextStepIndex);
        setupBottomNavigation();
        fillActionBarTitle();
    }

    @OnClick(R.id.activity_step_prev_button)
    public void prevStep() {
        int prevStepIndex = viewModel.getCurrentStepIndex() - 1;
        viewModel.setCurrentStep(prevStepIndex);
        setupBottomNavigation();
        fillActionBarTitle();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
