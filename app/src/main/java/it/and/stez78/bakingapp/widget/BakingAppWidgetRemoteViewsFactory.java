package it.and.stez78.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import it.and.stez78.bakingapp.R;
import it.and.stez78.bakingapp.model.Ingredient;
import it.and.stez78.bakingapp.model.Recipe;

public class BakingAppWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private List<Ingredient> ingredientList = new ArrayList<>();
    private int appWidgetId;

    public BakingAppWidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        context = applicationContext;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.widget_pref_name), Context.MODE_PRIVATE);
        Recipe recipe = Recipe.fromJson(sharedPref.getString("w-" + appWidgetId, ""));
        if (recipe != null) {
            ingredientList.clear();
            ingredientList.addAll(recipe.getIngredients());
        }
    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.widget_pref_name), Context.MODE_PRIVATE);
        Recipe recipe = Recipe.fromJson(sharedPref.getString("w-" + appWidgetId, ""));
        if (recipe != null) {
            ingredientList.clear();
            ingredientList.addAll(recipe.getIngredients());
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.list_element_widget);
        Ingredient ingredient = ingredientList.get(position);
        if (ingredient != null) {
            rv.setTextViewText(R.id.list_element_widget_ingredient_description, ingredient.getIngredient());
            rv.setTextViewText(R.id.list_element_widget_ingredient_mesure, ingredient.getMeasure());
            rv.setTextViewText(R.id.list_element_widget_ingredient_quantity, ingredient.getQuantity().toString());
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}