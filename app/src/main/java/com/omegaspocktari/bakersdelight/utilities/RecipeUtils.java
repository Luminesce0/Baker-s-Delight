package com.omegaspocktari.bakersdelight.utilities;

import android.text.TextUtils;
import android.util.Log;

import com.omegaspocktari.bakersdelight.data.RecipeBase;
import com.omegaspocktari.bakersdelight.data.RecipeIngredients;
import com.omegaspocktari.bakersdelight.data.RecipeSteps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.omegaspocktari.bakersdelight.utilities.NetworkUtils.createUrl;
import static com.omegaspocktari.bakersdelight.utilities.NetworkUtils.makeHTTPRequest;

/**
 * Created by ${Michael} on 7/1/2017.
 */

public class RecipeUtils {

    //Logging Tag
    private static final String LOG_TAG = RecipeUtils.class.getSimpleName();

    public static List<RecipeBase> fetchRecipeData(String stringUrl) {
        URL url = createUrl(stringUrl);

        //Perform HTTP Request to the URL and receive a JSON response
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP Request [RecipeUtils].", e);
        }

        //Return extracted recipe data
        return extractRecipeDataFromJson(jsonResponse);
    }

    public static List<RecipeBase> extractRecipeDataFromJson(String recipeJson) {
        //Recipe List
        List<RecipeBase> recipesList = new ArrayList<>();

        if (TextUtils.isEmpty(recipeJson)) {
            Log.e(LOG_TAG, "JSON data was empty [RecipeUtils]");
            return null;
        }

        try {
            //Recipe Base JSON Keys & Storage Variables
            //Array Lists are declared independently
            final String RECIPE_ID = "id";
            final String RECIPE_NAME = "name";
            final String RECIPE_INGREDIENTS = "ingredients";
            final String RECIPE_STEPS = "steps";
            final String RECIPE_SERVINGS = "servings";
            final String RECIPE_IMAGE = "image";

            String recipeID;
            String recipeName;
            String recipeServings;
            String recipeImage;

            //Recipe Ingredients JSON Keys & Storage Variables
            final String INGREDIENTS_QUANTITY = "quantity";
            final String INGREDIENTS_MEASURE = "measure";
            final String INGREDIENTS_INGREDIENT = "ingredient";

            String ingredientsQuantity;
            String ingredientsMeasure;
            String ingredientsIngredient;


            //Recipe Steps JSON Keys & Storage Variables
            final String STEPS_ID = "id";
            final String STEPS_SHORT_DESCRIPTION = "shortDescription";
            final String STEPS_DESCRIPTION = "description";
            final String STEPS_VIDEO_URL = "videoURL";
            final String STEPS_THUMBNAIL_URL = "thumbnailURL";

            String stepsID;
            String stepsShortDescription;
            String stepsDescription;
            String stepsVideoUrl;
            String stepsThumbnailUrl;

            //Begin acquiring JSON data
            JSONArray recipeJsonArray = new JSONArray(recipeJson);

            //Create a list of RecipeBase information
            for (int i = 0; i < recipeJsonArray.length(); i++) {
                //Get RecipeBase Information
                JSONObject currentRecipeBase = recipeJsonArray.getJSONObject(i);
                recipeID = currentRecipeBase.getString(RECIPE_ID);
                recipeName = currentRecipeBase.getString(RECIPE_NAME);
                recipeServings = currentRecipeBase.getString(RECIPE_SERVINGS);
                recipeImage = currentRecipeBase.getString(RECIPE_IMAGE);

                //Get RecipeIngredients & create a list to store the information
                JSONArray recipeIngredientsJsonArray = currentRecipeBase.getJSONArray(RECIPE_INGREDIENTS);
                List<RecipeIngredients> recipeIngredientsList = new ArrayList<>();
                for (int i1 = 0; i1 < recipeIngredientsJsonArray.length(); i1++) {
                    JSONObject currentRecipeIngredient = recipeIngredientsJsonArray.getJSONObject(i1);
                    ingredientsQuantity = currentRecipeIngredient.getString(INGREDIENTS_QUANTITY);
                    ingredientsMeasure = currentRecipeIngredient.getString(INGREDIENTS_MEASURE);
                    ingredientsIngredient = currentRecipeIngredient.getString(INGREDIENTS_INGREDIENT);

                    //Create a RecipeIngredients Object
                    RecipeIngredients recipeIngredient = new RecipeIngredients(ingredientsQuantity, ingredientsMeasure, ingredientsIngredient);

                    //Add RecipeIngredient Object to the list
                    recipeIngredientsList.add(recipeIngredient);
                }

                //Get RecipeSteps & create a list to store the information
                JSONArray recipeStepsJsonArray = currentRecipeBase.getJSONArray(RECIPE_STEPS);
                List<RecipeSteps> recipeStepsList = new ArrayList<>();
                for (int i2 = 0; i2 < recipeStepsJsonArray.length(); i2++) {
                    JSONObject currentRecipeStep = recipeStepsJsonArray.getJSONObject(i2);
                    stepsID = currentRecipeStep.getString(STEPS_ID);
                    stepsShortDescription = currentRecipeStep.getString(STEPS_SHORT_DESCRIPTION);
                    stepsDescription= currentRecipeStep.getString(STEPS_DESCRIPTION);
                    stepsVideoUrl = currentRecipeStep.getString(STEPS_VIDEO_URL);
                    stepsThumbnailUrl = currentRecipeStep.getString(STEPS_THUMBNAIL_URL);

                    //Create a RecipeSteps Object
                    RecipeSteps recipeStep = new RecipeSteps(stepsID, stepsShortDescription, stepsDescription, stepsVideoUrl, stepsThumbnailUrl);

                    //Add RecipeSteps Object to the list
                    recipeStepsList.add(recipeStep);
                }

                //Create a RecipeBase Object
                RecipeBase recipeBase = new RecipeBase(recipeID, recipeName, recipeIngredientsList, recipeStepsList, recipeServings, recipeImage);

                //Add RecipeBase Object to the list
                recipesList.add(recipeBase);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipesList;
    }
}
