package it.and.stez78.bakingapp.app;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import it.and.stez78.bakingapp.R;
import it.and.stez78.bakingapp.app.viewmodel.ViewModelFactory;
import it.and.stez78.bakingapp.app.viewmodel.WidgetConfigurationViewModel;
import it.and.stez78.bakingapp.model.Recipe;
import it.and.stez78.bakingapp.widget.BakingAppWidgetProvider;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;

public class WidgetConfigurationActivity extends AppCompatActivity {

    @BindView(R.id.activity_widget_conf_spinner)
    Spinner recipesSpinner;

    @Inject
    ViewModelFactory viewModelFactory;
    WidgetConfigurationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_configuration);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WidgetConfigurationViewModel.class);

        Bundle extras = getIntent().getExtras();
        int appWidgetId = extras.getInt(EXTRA_APPWIDGET_ID);

        viewModel.init(appWidgetId);
        viewModel.getRecipesLiveData().observe(this, recipes -> {
            viewModel.setRecipes(recipes);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, viewModel.getRecipeNames());
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            recipesSpinner.setAdapter(dataAdapter);
            recipesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    viewModel.setSelectedRecipe(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        });
    }

    @OnClick(R.id.activity_widget_conf_go_button)
    public void recipeSelectedClick() {
        viewModel.getIngredientsForSelectedRecipe().observe(this, ingredients -> {
            SharedPreferences sharedPref = getSharedPreferences(
                    getString(R.string.widget_pref_name), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            Recipe r = viewModel.getSelectedRecipe();
            r.setIngredients(ingredients);
            editor.putString("w-" + viewModel.getWidgetId(), r.toJson());
            editor.apply();
            Intent startWidget = new Intent();
            startWidget.putExtra(EXTRA_APPWIDGET_ID, viewModel.getWidgetId());
            setResult(RESULT_OK, startWidget);
            BakingAppWidgetProvider.updateAppWidget(getApplicationContext(), AppWidgetManager.getInstance(this), viewModel.getWidgetId());
            finish();
        });

    }
}
