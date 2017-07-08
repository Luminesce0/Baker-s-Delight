package com.omegaspocktari.bakersdelight.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omegaspocktari.bakersdelight.R;
import com.omegaspocktari.bakersdelight.data.RecipeBase;
import com.omegaspocktari.bakersdelight.data.RecipeIngredients;
import com.omegaspocktari.bakersdelight.data.RecipeSteps;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${Michael} on 6/26/2017.
 */

class RecipeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //ViewType Tags
    public static final int VIEW_TYPE_INGREDIENTS = 0;
    public static final int VIEW_TYPE_STEPS = 1;
    //Logging Tag
    private static final String LOG_TAG = RecipeDetailAdapter.class.getSimpleName();
    //Click Handler
    private final RecipeDetailListAdapterOnClickHandler mClickHandler;

    //Recipe Base List for detail step passing
    private List<RecipeBase> mRecipeBaseList;

    //Recipe Base object to obtain lists
    private RecipeBase mRecipeBase;

    //Ingredient & Steps lists
    private List<RecipeIngredients> mIngredientsList;
    private List<RecipeSteps> mStepsList;

    //Context
    private Context mContext;

    //Trackers for list sizes and the View
    private int mIngredientsSize;
    private int mStepsSize;

    public RecipeDetailAdapter(Context context, RecipeBase recipeBase, List<RecipeBase> recipeBaseList,
                               RecipeDetailListAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
        mRecipeBase = recipeBase;
        mRecipeBaseList = recipeBaseList;

        //Verify list isn't empty
        if (recipeBase.getRecipeSteps().size() > 0) {

            //Set new list
            mIngredientsList = mRecipeBase.getRecipeIngredients();

            //Set list tracker size to determine appropriate view holder
            mIngredientsSize = mIngredientsList.size();
        }

        //Verify list isn't empty
        if (recipeBase.getRecipeIngredients().size() > 0) {
            //Set new list
            mStepsList = mRecipeBase.getRecipeSteps();

            //Set list tracker size to determine appropriate view holder
            mStepsSize = mStepsList.size();
        }
    }

    //TODO: Pretty sure I can remove this

//    public void swapRecipeBase(RecipeBase newRecipeBase) {
//        if (newRecipeBase != null) {
//            mRecipeBase = newRecipeBase;
//            //Verify list exists and isn't empty
//            if (newRecipeBase.getRecipeSteps() != null &&
//                    newRecipeBase.getRecipeSteps().size() > 0) {
//
//                //Set new list
//                mIngredientsList = mRecipeBase.getRecipeIngredients();
//
//                //Set list tracker size to determine appropriate view holder
//                mIngredientsSize = mIngredientsList.size();
//            }
//            //Verify list exists and isn't empty
//            if (newRecipeBase.getRecipeIngredients() != null &&
//                    newRecipeBase.getRecipeIngredients().size() > 0) {
//                //Set new list
//                mStepsList = mRecipeBase.getRecipeSteps();
//
//                //Set list tracker size to determine appropriate view holder
//                mStepsSize = mStepsList.size();
//            }
//            notifyDataSetChanged();
//        }
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Obtain & return layout
        LayoutInflater inflater = LayoutInflater.from(mContext);

        switch (viewType) {
            case VIEW_TYPE_INGREDIENTS:
                View ingredientView = inflater.inflate(R.layout.recipe_ingredients_item, parent, false);
                return new RecipeIngredientViewHolder(ingredientView);
            case VIEW_TYPE_STEPS:
                View stepView = inflater.inflate(R.layout.recipe_step_description_item, parent, false);
                return new RecipeStepViewHolder(stepView);
            default:
                View stepViewDefault = inflater.inflate(R.layout.recipe_step_description_item, parent, false);
                return new RecipeStepViewHolder(stepViewDefault);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_INGREDIENTS:
                //Bind a recipe ingredient object
                RecipeIngredientViewHolder holderIngredient = (RecipeIngredientViewHolder) holder;
                RecipeIngredients recipeIngredient = mIngredientsList.get(position);
                holderIngredient.bind(recipeIngredient);
                break;
            case VIEW_TYPE_STEPS:
                //Bind a recipe step object
                RecipeStepViewHolder holderStep = (RecipeStepViewHolder) holder;
                RecipeSteps recipeStep = mStepsList.get(position - mIngredientsSize);
                holderStep.bind(recipeStep);
        }

    }

    @Override
    public int getItemCount() {
        return mStepsSize + mIngredientsSize;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(LOG_TAG, "mIngredientsSize: " + mIngredientsSize
                + "\nPosition: " + position);
        if (position < mIngredientsSize) {
            Log.d(LOG_TAG, "VIEW_TYPE_INGREDIENTS");
            return VIEW_TYPE_INGREDIENTS;
        } else {
            Log.d(LOG_TAG, "VIEW_TYPE_STEPS");
            return VIEW_TYPE_STEPS;
        }
    }

    public interface RecipeDetailListAdapterOnClickHandler {
        void onListItemClick(RecipeSteps recipeStep, boolean isRecipe, List<RecipeBase> mRecipeBaseList);
    }

    public class RecipeIngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredient_ingredient)
        TextView ingredient;
        @BindView(R.id.tv_ingredient_quantity_measure)
        TextView ingredientMeasureQuantity;

        public RecipeIngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(RecipeIngredients recipeIngredient) {
            //Ingredient Name
            ingredient.setText(recipeIngredient.getIngredient());

            //Ingredient Measure & Quantity
            String ingredientMeasureAndQuantity = recipeIngredient.getQuantity() + " "
                    + recipeIngredient.getMeasure();
            ingredientMeasureQuantity.setText(ingredientMeasureAndQuantity);

            Log.d(LOG_TAG, "Here is the current ingredient in [RecipeDetailAdapter]: " + recipeIngredient.getIngredient());
        }
    }

    public class RecipeStepViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        @BindView(R.id.tv_step_description_header)
        TextView stepHeader;

        public RecipeStepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(RecipeSteps recipeStep) {
            stepHeader.setText(recipeStep.getShortDescription());
        }

        @Override
        public void onClick(View v) {
            Log.d(LOG_TAG, "onClick of [RecipeDetailAdapter] " + getAdapterPosition());
            //Get current recipe step
            RecipeSteps recipeStep = mStepsList.get(getAdapterPosition());
            boolean isRecipeStep;

            //Identify clicked item
            if (getAdapterPosition() >= mIngredientsSize) {
                //Clicked item is a recipe step
                isRecipeStep = true;
                mClickHandler.onListItemClick(recipeStep, isRecipeStep, mRecipeBaseList);
            } else {
                //Clicked item is not a recipe step
                isRecipeStep = false;
                mClickHandler.onListItemClick(recipeStep, isRecipeStep, mRecipeBaseList);
            }

        }
    }
}
