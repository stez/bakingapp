package it.and.stez78.bakingapp.app;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import it.and.stez78.bakingapp.R;
import it.and.stez78.bakingapp.app.adapter.ElementRecipeAdapter;
import it.and.stez78.bakingapp.app.adapter.OnRecipeClickListener;
import it.and.stez78.bakingapp.app.viewmodel.MainActivityViewModel;
import it.and.stez78.bakingapp.app.viewmodel.ViewModelFactory;
import it.and.stez78.bakingapp.model.Recipe;

public class MainActivity extends AppCompatActivity implements OnRecipeClickListener {

    @BindView(R.id.activity_main_rw)
    RecyclerView recyclerView;

    @BindView(R.id.activity_main_swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.activity_main_toolbar)
    Toolbar toolbar;

    @Inject
    ViewModelFactory viewModelFactory;
    MainActivityViewModel viewModel;

    @Inject
    Picasso p;

    private ElementRecipeAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel.class);
        int size = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        switch (size) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                layoutManager = new GridLayoutManager(this, 3);
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                layoutManager = new GridLayoutManager(this, 3);
                break;
            default:
                layoutManager = new LinearLayoutManager(this);
        }
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ElementRecipeAdapter(viewModel.getRecipes(), p, viewModel.getRecipesRepository(), this);
        recyclerView.setAdapter(adapter);
        viewModel.getRecipesLiveData().observe(this, recipes -> {
            if (recipes != null && recipes.size() > 0) {
                viewModel.setRecipes(recipes);
                adapter.notifyDataSetChanged();
            }
            swipeRefreshLayout.setRefreshing(false);
            if (recipes.isEmpty() && viewModel.isFirstRun()) {
                Toast.makeText(this, R.string.fetch_data_remote, Toast.LENGTH_LONG).show();
                viewModel.updateRecipes();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(() -> viewModel.updateRecipes());
    }

    @Override
    public void OnRecipeClick(Recipe recipe) {
        Intent recipeIntent = new Intent(this, RecipeActivity.class);
        recipeIntent.putExtra(RecipeActivity.RECIPE_KEY, recipe);
        startActivity(recipeIntent);
    }
}