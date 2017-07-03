package com.omegaspocktari.bakersdelight.data;

import org.parceler.Parcel;

/**
 * Created by ${Michael} on 6/29/2017.
 */

@Parcel
public class RecipeSteps {

    // Variable Holders for Recipe Steps
    private String mID;
    private String mShortDescription;
    private String mDescription;
    private String mVideoURL;
    private String mThumbnailURL;


    public RecipeSteps(String mID, String mShortDescription, String mDescription, String mVideoURL, String mThumbnailURL) {
        this.mID = mID;
        this.mShortDescription = mShortDescription;
        this.mDescription = mDescription;
        this.mVideoURL = mVideoURL;
        this.mThumbnailURL = mThumbnailURL;
    }

    public String getmID() {
        return mID;
    }

    public String getmShortDescription() {
        return mShortDescription;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmVideoURL() {
        return mVideoURL;
    }

    public String getmThumbnailURL() {
        return mThumbnailURL;
    }
}
