package com.omegaspocktari.bakersdelight.data;


import org.parceler.Parcel;

/**
 * Created by ${Michael} on 6/29/2017.
 */

@Parcel
public class RecipeIngredients {

    // Variable Holders for Recipe Ingredients
    public String mQuantity;
    public String mMeasure;
    public String mIngredient;

    public RecipeIngredients() {
    }

    public RecipeIngredients(String mQuantity, String mMeasure, String mIngredient) {
        this.mQuantity = mQuantity;
        this.mMeasure = mMeasure;
        this.mIngredient = mIngredient;
    }

    public String getQuantity() {
        return mQuantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public String getIngredient() {
        return mIngredient;
    }
}
