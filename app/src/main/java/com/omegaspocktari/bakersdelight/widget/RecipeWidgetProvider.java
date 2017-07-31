package com.omegaspocktari.bakersdelight.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.omegaspocktari.bakersdelight.R;
import com.omegaspocktari.bakersdelight.ui.MainActivity;

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

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

            Intent titleIntent = new Intent(context, MainActivity.class);
            PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//            views.setOnClickPendingIntent(R.id.sv_widget, titlePendingIntent);

            Intent stackWidgetServiceIntent = new Intent(context, RecipeStackWidgetService.class);
            views.setRemoteAdapter(R.id.sv_widget, stackWidgetServiceIntent);

            //Setup onClick for StackView items
            Intent clickIntentTemplate = new Intent(context, MainActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.sv_widget, clickPendingIntentTemplate);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, RecipeWidgetProvider.class));
        context.sendBroadcast(intent);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            Log.d(LOG_TAG, "Do the thing?!?!?!?!?!?!?!?!?!?!?");
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, RecipeWidgetProvider.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(componentName), R.id.sv_widget);
        }
        super.onReceive(context, intent);
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
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.sv_widget);
    }
}


