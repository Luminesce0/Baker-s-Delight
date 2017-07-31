package com.omegaspocktari.bakersdelight.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.omegaspocktari.bakersdelight.R;
import com.omegaspocktari.bakersdelight.data.RecipeBase;
import com.omegaspocktari.bakersdelight.data.RecipeIngredients;
import com.omegaspocktari.bakersdelight.utilities.RecipeUtils;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.defaultValue;

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

        //Increment position for readability
        private static final int READABILITY_INCREMENT = 1;

        //Total amount of views
        private int mCount;

        //Recipe items lists
        private List<RecipeBase> mRecipeItems = new ArrayList<>();
        private List<RecipeIngredients> mRecipeIngredients = new ArrayList<>();

        //Application context
        private Context mContext;

        //Current recipe
        private int mCurrentRecipe;

        //Name of current recipe
        private String mRecipeName;

        //ID of widget
        private int mAppWidgetId;
        private RecipeBase mRecipeBase;


        public StackRemoteViewsFactory(Context applicationContext, Intent intent) {
            mContext = applicationContext;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {

            //Obtain last used recipe or the default recipe from Shared Preferences
            SharedPreferences prefs = getSharedPreferences(getString(R.string.recipe_widget_preferences), MODE_PRIVATE);
            int defaultValue = getResources().getInteger(R.integer.default_recipe_value);
            mCurrentRecipe = prefs.getInt(getString(R.string.recipe_widget_preference_key), defaultValue);
        }

        public void onDataSetChanged() {

            //Fetch recipe data
            mRecipeItems = RecipeUtils.fetchRecipeData(mContext.getString(R.string.json_recipe_url));

            //Fetch current recipe upon update with preference information
            SharedPreferences prefs = getSharedPreferences(getString(R.string.recipe_widget_preferences), MODE_PRIVATE);
            mCurrentRecipe = prefs.getInt(getString(R.string.recipe_widget_preference_key), defaultValue);
            mRecipeBase = mRecipeItems.get(mCurrentRecipe);

            //Obtain Recipe Name
            mRecipeName = mRecipeBase.getName();

            //Obtain proper ingredient list from the last used recipe or default recipe
            mRecipeIngredients = mRecipeBase.getRecipeIngredients();

            //Reflect amount of ingredients
            mCount = mRecipeIngredients.size();

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

            //Get appropriate recipe ingredient
            RecipeIngredients recipeIngredient = mRecipeIngredients.get(position);

            //Construct remote view from our widget xml file
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_item);

            //Recipe Ingredient Header
            String ingredientPosition =
                    + position + READABILITY_INCREMENT
                    + "/" //Divider
                    + mCount;

            //Recipe Ingredient Name
            String ingredientName = recipeIngredient.getIngredient();

            //Recipe Ingredient Measure and Quantity
            String ingredientMeasureAndQuantity = recipeIngredient.getQuantity()
                    + " "
                    + recipeIngredient.getMeasure();

            //Set remote view text fields
            rv.setTextViewText(R.id.tv_widget_recipe_name, mRecipeName);
            rv.setTextViewText(R.id.tv_widget_ingredient_header, ingredientPosition);
            rv.setTextViewText(R.id.tv_widget_ingredient_ingredient, ingredientName);
            rv.setTextViewText(R.id.tv_widget_ingredient_quantity_measure, ingredientMeasureAndQuantity);

            //Fill-intent used to fill-in the pending intent template set on the collection view in RecipeWidgetProvider
            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(getString(R.string.recipe_widget_key), getString(R.string.recipe_widget_key));
            fillInIntent.putExtra(getString(R.string.recipe_widget_preference_key), mCurrentRecipe);
            rv.setOnClickFillInIntent(R.id.ll_widget_item, fillInIntent);

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
