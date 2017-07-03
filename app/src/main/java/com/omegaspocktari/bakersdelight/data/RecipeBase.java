package com.omegaspocktari.bakersdelight.data;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by ${Michael} on 6/29/2017.
 */

@Parcel
public class RecipeBase {

    // Variable Holders for Recipe Steps
    private String mID;
    private String mName;
    private List<RecipeIngredients> mRecipeIngredients;
    private List<RecipeSteps> mRecipeSteps;
    private String mServings;
    private String mImage;

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
