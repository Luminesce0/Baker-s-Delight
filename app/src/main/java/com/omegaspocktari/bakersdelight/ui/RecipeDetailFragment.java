package com.omegaspocktari.bakersdelight.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omegaspocktari.bakersdelight.R;
import com.omegaspocktari.bakersdelight.data.RecipeBase;
import com.omegaspocktari.bakersdelight.data.RecipeSteps;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${Michael} on 7/3/2017.
 */

public class RecipeDetailFragment extends Fragment implements
        RecipeDetailAdapter.RecipeDetailListAdapterOnClickHandler {
    //Logging Tag
    private static final String LOG_TAG = RecipeDetailFragment.class.getSimpleName();

    //Views for layout
    @BindView(R.id.rv_recipe_details_list)
    RecyclerView mRecyclerView;

    //Layout Managers
    private GridLayoutManager gridLayoutManager;

    //Adapters
    private RecipeDetailAdapter mRecipeDetailAdapter;

    //RecipeBase Object
    RecipeBase mRecipeBase;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Acquire the layout we wish to use and bind views
        View rootView = inflater.inflate(R.layout.recipe_detail_fragment, container, false);
        ButterKnife.bind(this, rootView);

        //Acquire bundled information
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mRecipeBase = Parcels.unwrap(bundle.getParcelable(getString(R.string.recipe_base_key)));
        }

        //Grid Layout that will offer a grid for ingredients and a single row for steps
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mRecipeDetailAdapter.getItemViewType(position)) {
                    case RecipeDetailAdapter.VIEW_TYPE_INGREDIENTS:
                        Log.d(LOG_TAG, "VIEW_TYPE_INGREDIENTS");
                        return 1;
                    case RecipeDetailAdapter.VIEW_TYPE_STEPS:
                        Log.d(LOG_TAG, "VIEW_TYPE_STEPS");
                        return 2;
                    default:
                        Log.d(LOG_TAG, "It should never come here!");
                        return 2;
                }
            }
        });

        //Setup the adapter
        mRecipeDetailAdapter = new RecipeDetailAdapter(getContext(), mRecipeBase, this);

        //Setup Recycler View
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mRecipeDetailAdapter);

        return rootView;
    }

    @Override
    public void onListItemClick(RecipeSteps recipeStep, boolean isRecipe) {
        if (isRecipe == true) {
            Log.d(LOG_TAG, "onClick of [RecipeDetailFragment]");
            //Create the fragment to be added to the stack
            Fragment fragment = new RecipeDetailFragment();

            //Create bundle to attach to and send with the fragment
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.recipe_base_key), Parcels.wrap(mRecipeBase));
            fragment.setArguments(bundle);

            //Create the transaction to interact with the stack and add previous fragment to the backstack
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.activity_main_fragment_holder, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
