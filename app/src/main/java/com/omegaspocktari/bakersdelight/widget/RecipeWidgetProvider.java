package com.omegaspocktari.bakersdelight.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.omegaspocktari.bakersdelight.R;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = RecipeWidgetProvider.class.getSimpleName();

    public static final String ACTION_OPEN_RECIPE_DETAILS = "com.omegaspocktari.bakersdelight.action.recipe_details";
    public static final String RECIPE_ITEM = "com.omegaspocktari.bakersdelight.action.recipe_item";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(LOG_TAG, "onUpdate() - RecipeWidgetProvider");

        //Update widgets with remote adapter
        for (int i = 0; i < appWidgetIds.length; ++i) {

            Log.d(LOG_TAG, "onUpdate() - RecipeWidgetProvider" + i);

            //Point to StackViewsService which will provide views for this collection
            Intent intent = new Intent(context, RecipeStackWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

//            //TODO: What
            // When intents are compared, the extras are ignored, so we need to embed the extras
            // into the data so that the extras will not be ignored.
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            //TODO: My brain
            //Set the RemoteViews object for the app widget layout.
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

            //Setup RemoteViews object to use the appropriate adapter to populate the data.
            //The adapter connects to the RemoteViewsService through the given intent.
            rv.setRemoteAdapter(R.id.stack_view, intent);

            //Displayed when the collection has no items
            rv.setEmptyView(R.id.stack_view, R.id.empty_view);

//          appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
            appWidgetManager.updateAppWidget(appWidgetIds, rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /**
     * Called in response to the {@link AppWidgetManager#ACTION_APPWIDGET_OPTIONS_CHANGED}
     * broadcast when this widget has been layed out at a new size.
     * <p>
     * {@more}
     *
     * @param context          The {@link Context Context} in which this receiver is
     *                         running.
     * @param appWidgetManager A {@link AppWidgetManager} object you can call {@link
     *                         AppWidgetManager#updateAppWidget} on.
     * @param appWidgetId      The appWidgetId of the widget whose size changed.
     * @param newOptions       The appWidgetId of the widget whose size changed.
     * @see AppWidgetManager#ACTION_APPWIDGET_OPTIONS_CHANGED
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stack_view);
    }
}


