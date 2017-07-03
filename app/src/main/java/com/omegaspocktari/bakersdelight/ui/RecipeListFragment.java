package com.omegaspocktari.bakersdelight.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omegaspocktari.bakersdelight.R;
import com.omegaspocktari.bakersdelight.data.RecipeBase;
import com.omegaspocktari.bakersdelight.utilities.RecipeUtils;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;

/**
 * Created by ${Michael} on 6/26/2017.
 */

public class RecipeListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<RecipeBase>>,
        RecipeListAdapter.RecipeListAdapterOnClickHandler {

    //Logging Tag
    private static final String LOG_TAG = RecipeListFragment.class.getSimpleName();

    //Loader ID
    private static final int RECIPE_RESULTS_LOADER = 1;

    //Bundle key for Url
    private static final String RECIPE_INFO_URL = "recipeUrl";

    //Views for layout
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;

    //Adapter
    private RecipeListAdapter mAdapter;

    //NetworkInfo to check network connectivity
    private NetworkInfo mNetworkInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Get the layout we want to use for this fragment
        View rootView = inflater.inflate(R.layout.recipe_list_fragment, container, false);

        // Acquire a connectivity manager to see if the network is connected.
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get the current active network's info.
        mNetworkInfo = connectivityManager.getActiveNetworkInfo();

        //Bind RecyclerView to the layout
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_recipe_list);

        //Improve performance
        mRecyclerView.setHasFixedSize(true);

        //Create layout manager
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        //Bind layout manager
        mRecyclerView.setLayoutManager(layoutManager);

        //Default adapter
        mAdapter = new RecipeListAdapter(getContext(), this);

        //Bind the adapter
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();

        fetchRecipes();
    }

    private void fetchRecipes() {
        if (mNetworkInfo != null && mNetworkInfo.isConnected()) {

            Bundle recipeInfoBundle = new Bundle();
            recipeInfoBundle.putString(RECIPE_INFO_URL, getString(R.string.json_recipe_url));

            LoaderManager loaderManager = getActivity().getSupportLoaderManager();
            Loader<String> recipeLoader = loaderManager.getLoader(RECIPE_RESULTS_LOADER);
            if (recipeLoader == null) {
                loaderManager.initLoader(RECIPE_RESULTS_LOADER, recipeInfoBundle, this).forceLoad();
            } else {
                loaderManager.restartLoader(RECIPE_RESULTS_LOADER, recipeInfoBundle, this).forceLoad();
            }
        }
    }

    //TODO: Create RecipeDetailFragment
    @Override
    public void onListItemClick(RecipeBase recipeBase) {
        Log.d(LOG_TAG, "onClick of [RecipeListAdapter]");
        Intent recipeDetailName = new Intent(getContext(), RecipeDetailFragment.class);
        recipeDetailName.putExtra(getString(R.string.recipe_base_key), Parcels.wrap(recipeBase));
        startActivity(recipeDetailName);
    }

    @Override
    public Loader<List<RecipeBase>> onCreateLoader(int id, final Bundle args) {
        //Generate an AsyncTask that will obtain the recipe information within the lodaer
        return new AsyncTaskLoader<List<RecipeBase>>(getContext()) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
            }

            @Override
            public List<RecipeBase> loadInBackground() {
                //Grab URL from bundle argument
                String jsonUrl = args.getString(RECIPE_INFO_URL);

                //Fetch recipe information & return results
                List<RecipeBase> recipeBaseList = RecipeUtils.fetchRecipeData(jsonUrl);
                return recipeBaseList;
            }
        };
    }

    //TODO: Set up views for loading/Errors/Empty Results
    @Override
    public void onLoadFinished(Loader<List<RecipeBase>> loader, List<RecipeBase> recipeBaseList) {
        //If results aren't null, swap the list
        if (recipeBaseList != null) {
            mAdapter.swapList(recipeBaseList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<RecipeBase>> loader) {
        mAdapter.swapList(null);
    }

}
