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

import java.util.List;

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
    private RecipeBase mRecipeBase;

    //Current RecipeStep Object
    private int mStepSelected = 0;

    //Tablet Layout Tracker
    private boolean mTwoPane;

    //DetailStepFragment Initialized Tracker
    private boolean mInitialized = false;

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
            mTwoPane = bundle.getBoolean(getString(R.string.tablet_layout_key));
        }

        //If tablet layout load and show other fragment
        if (mTwoPane && !mInitialized) {
            createDetailStepFragment();
            getActivity().findViewById(R.id.activity_main_tablet_recipe_step_detail_holder).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.activity_main_v_fragment_divider).setVisibility(View.VISIBLE);
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

    /**
     * Store user's current step location
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.current_step_key), mStepSelected);
        outState.putBoolean(getString(R.string.detail_step_initialized), mInitialized);
    }

    /**
     * Restore user's instance state and the current step the user is on if fragment is reloaded
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mStepSelected = savedInstanceState.getInt(getString(R.string.current_step_key));
            mInitialized = savedInstanceState.getBoolean(getString(R.string.detail_step_initialized));
        }
    }

    public void createDetailStepFragment () {
        Log.d(LOG_TAG, "TWO PANE LAYOUT");
        Fragment fragment = new RecipeDetailStepFragment();

        RecipeSteps recipeStep = mRecipeBase.getRecipeSteps().get(0);
        List<RecipeSteps> stepsList = mRecipeBase.getRecipeSteps();

        Bundle initialStepBundle = new Bundle();
        initialStepBundle.putParcelable(getString(R.string.recipe_step_key), Parcels.wrap(recipeStep));
        initialStepBundle.putParcelable(getString(R.string.recipe_step_list_key), Parcels.wrap(stepsList));
        initialStepBundle.putBoolean(getString(R.string.tablet_layout_key), mTwoPane);
        fragment.setArguments(initialStepBundle);

        //Create the transaction to interact with the stack and add previous fragment to the backstack
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_main_tablet_recipe_step_detail_holder, fragment);
        transaction.commit();
    }

    @Override
    public void onListItemClick(RecipeSteps recipeStep, List<RecipeSteps> mStepsList, boolean isRecipeStep) {
        if (isRecipeStep == true) {
            Log.d(LOG_TAG, "onClick of [RecipeDetailFragment]");
            //Store the selected item

            //Create the fragment to be added to the stack
            Fragment fragment = new RecipeDetailStepFragment();

            //Create bundle to attach to and send with the fragment
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.recipe_step_key), Parcels.wrap(recipeStep));
            bundle.putParcelable(getString(R.string.recipe_step_list_key), Parcels.wrap(mStepsList));
            bundle.putBoolean(getString(R.string.tablet_layout_key), mTwoPane);
            fragment.setArguments(bundle);

            //Create the transaction to interact with the stack and add previous fragment to the backstack
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            if (mTwoPane) {
                transaction.replace(R.id.activity_main_tablet_recipe_step_detail_holder, fragment);
                transaction.commit();
            } else {
                transaction.replace(R.id.activity_main_tablet_recipe_list_holder, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

        }
    }
}
