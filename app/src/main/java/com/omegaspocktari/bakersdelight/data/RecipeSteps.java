package com.omegaspocktari.bakersdelight.data;

import org.parceler.Parcel;

/**
 * Created by ${Michael} on 6/29/2017.
 */

@Parcel
public class RecipeSteps {

    // Variable Holders for Recipe Steps
    public String mID;
    public String mShortDescription;
    public String mDescription;
    public String mVideoURL;
    public String mThumbnailURL;

    public RecipeSteps () {}

    public RecipeSteps(String mID, String mShortDescription, String mDescription, String mVideoURL, String mThumbnailURL) {
        this.mID = mID;
        this.mShortDescription = mShortDescription;
        this.mDescription = mDescription;
        this.mVideoURL = mVideoURL;
        this.mThumbnailURL = mThumbnailURL;
    }

    public int getID() {
        return Integer.getInteger(mID);
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getVideoURL() {
        return mVideoURL;
    }

    public String getThumbnailURL() {
        return mThumbnailURL;
    }
}
