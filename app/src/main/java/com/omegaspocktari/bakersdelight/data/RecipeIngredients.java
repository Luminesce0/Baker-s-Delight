package com.omegaspocktari.bakersdelight.data;

import org.parceler.Parcel;

/**
 * Created by ${Michael} on 6/29/2017.
 */

@Parcel
public class RecipeIngredients {

    // Variable Holders for Recipe Ingredients
    private String mQuantity;
    private String mMeasure;
    private String mIngredient;

    public RecipeIngredients() {
    }

    public RecipeIngredients(String mQuantity, String mMeasure, String mIngredient) {
        this.mQuantity = mQuantity;
        this.mMeasure = mMeasure;
        this.mIngredient = mIngredient;
    }
}
