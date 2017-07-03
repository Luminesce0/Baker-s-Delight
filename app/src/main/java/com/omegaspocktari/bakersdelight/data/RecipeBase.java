package com.omegaspocktari.bakersdelight.data;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by ${Michael} on 6/29/2017.
 */

@Parcel(Parcel.Serialization.BEAN)
public class RecipeBase {

    // Variable Holders for Recipe Steps
    public String mID;
    public String mName;
    public List<RecipeIngredients> mRecipeIngredients;
    public List<RecipeSteps> mRecipeSteps;
    public String mServings;
    public String mImage;

    public RecipeBase() {}

    public RecipeBase(String mID, String mName, List<RecipeIngredients> mRecipeIngredients, List<RecipeSteps> mRecipeSteps, String mServings, String mImage) {
        this.mID = mID;
        this.mName = mName;
        this.mRecipeIngredients = mRecipeIngredients;
        this.mRecipeSteps = mRecipeSteps;
        this.mServings = mServings;
        this.mImage = mImage;
    }

    public String getID() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public List<RecipeIngredients> getRecipeIngredients() {
        return mRecipeIngredients;
    }

    public List<RecipeSteps> getRecipeSteps() {
        return mRecipeSteps;
    }

    public String getServings() {
        return mServings;
    }

    public String getImage() {
        return mImage;
    }
}
