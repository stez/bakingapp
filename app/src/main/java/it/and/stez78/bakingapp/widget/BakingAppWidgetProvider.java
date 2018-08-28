package it.and.stez78.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.util.ArrayList;

import it.and.stez78.bakingapp.R;
import it.and.stez78.bakingapp.model.Recipe;
import timber.log.Timber;

public class BakingAppWidgetProvider extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients_list);
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.widget_pref_name), Context.MODE_PRIVATE);
        Recipe recipe = Recipe.fromJson(sharedPref.getString("w-" + appWidgetId, ""));
        if (recipe != null) {
            views.setTextViewText(R.id.widget_ingredients_list_recipe_name, recipe.getName());
            Intent intent = new Intent(context, BakingAppWidgetRemoteViewsService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            views.setRemoteAdapter(R.id.widget_ingredients_list_listview, intent);
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

