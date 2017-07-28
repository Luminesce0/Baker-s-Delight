package com.omegaspocktari.bakersdelight.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.omegaspocktari.bakersdelight.R;
import com.omegaspocktari.bakersdelight.data.RecipeBase;
import com.omegaspocktari.bakersdelight.data.RecipeIngredients;
import com.omegaspocktari.bakersdelight.ui.MainActivity;
import com.omegaspocktari.bakersdelight.utilities.RecipeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Michael} on 7/19/2017.
 */

public class RecipeStackWidgetService extends RemoteViewsService {

    public static final String LOG_TAG = RecipeStackWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        //Total amount of views
        private int mCount;

        //Recipe items lists
        private List<RecipeBase> mRecipeItems = new ArrayList<>();
        private List<RecipeIngredients> mRecipeIngredients = new ArrayList<>();

        //Application context
        private Context mContext;

        //Current Recipe
        private int mCurrentRecipe;

        //ID of widget
        private int mAppWidgetId;

        public StackRemoteViewsFactory(Context applicationContext, Intent intent) {
            mContext = applicationContext;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            Log.d(LOG_TAG, "onCreate() Beginning");
            //TODO: Do I need to do anything here?
            //Fetch recipe data
            mRecipeItems = RecipeUtils.fetchRecipeData(mContext.getString(R.string.json_recipe_url));

            //Obtain last used recipe or the default recipe from Shared Preferences
            SharedPreferences prefs = getSharedPreferences(getString(R.string.recipe_widget_preferences), MODE_PRIVATE);
            int defaultValue = getResources().getInteger(R.integer.default_recipe_value);
            mCurrentRecipe = prefs.getInt(getString(R.string.recipe_widget_key), defaultValue);
            RecipeBase recipeBase = mRecipeItems.get(prefs.getInt(getString(R.string.recipe_widget_key), defaultValue));

            //Obtain proper ingredient list from the last used recipe or default recipe
            mRecipeIngredients = recipeBase.getRecipeIngredients();

            //Reflect amount of ingredients
            mCount = mRecipeItems.size();

            Log.d(LOG_TAG, "onCreate() " + mRecipeIngredients.get(1).getIngredient());
        }

        public void onDataSetChanged() {

            //Fetch recipe data
            mRecipeItems = RecipeUtils.fetchRecipeData(mContext.getString(R.string.json_recipe_url));

            //Obtain last used recipe or the default recipe from Shared Preferences
            SharedPreferences prefs = getSharedPreferences(getString(R.string.recipe_widget_preferences), MODE_PRIVATE);
            int defaultValue = getResources().getInteger(R.integer.default_recipe_value);
            RecipeBase recipeBase = mRecipeItems.get(prefs.getInt(getString(R.string.recipe_widget_key), defaultValue));

            //Obtain proper ingredient list from the last used recipe or default recipe
            mRecipeIngredients = recipeBase.getRecipeIngredients();

            //Reflect amount of ingredients
            mCount = mRecipeItems.size();

        }

        @Override
        public void onDestroy() {
            mRecipeItems.clear();
        }

        @Override
        public int getCount() {
            return mRecipeIngredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Log.d(LOG_TAG, "getViewAt()");

            //Get appropriate recipe ingredient
            RecipeIngredients recipeIngredient = mRecipeIngredients.get(position);

            //Construct remote view from our widget xml file
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_item);

            //Recipe Ingredient Header
            String ingredientPosition = getString(R.string.widget_ingredient_header)
                    + position
                    + "/"
                    + mRecipeIngredients.size();

            //Recipe Ingredient Name
            String ingredientName = recipeIngredient.getIngredient();

            //Recipe Ingredient Measure and Quantity
            String ingredientMeasureAndQuantity = recipeIngredient.getQuantity()
                    + " "
                    + recipeIngredient.getMeasure();

            //Set remote view text fields
            rv.setTextViewText(R.id.tv_widget_ingredient_header, ingredientPosition);
            rv.setTextViewText(R.id.tv_widget_ingredient_ingredient, ingredientName);
            rv.setTextViewText(R.id.tv_widget_ingredient_quantity_measure, ingredientMeasureAndQuantity);

            //Fill-intent used to fill-in the pending intent template set on the collection view in RecipeWidgetProvider
            Bundle extras = new Bundle();
            extras.putInt(RecipeWidgetProvider.RECIPE_ITEM, mCurrentRecipe);
            Intent recipeIntent = new Intent(mContext, MainActivity.class);
            recipeIntent.putExtras(extras);
            PendingIntent pendingRecipeIntent = PendingIntent.getActivity(mContext, 0, recipeIntent, 0);
            rv.setOnClickPendingIntent(R.id.cv_widget_item, pendingRecipeIntent);

            Log.d(LOG_TAG, "getViewAt() End");
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
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
