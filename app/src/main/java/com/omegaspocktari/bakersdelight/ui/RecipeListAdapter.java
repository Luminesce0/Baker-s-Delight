package com.omegaspocktari.bakersdelight.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omegaspocktari.bakersdelight.R;
import com.omegaspocktari.bakersdelight.data.RecipeBase;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${Michael} on 6/26/2017.
 */

class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {
    // Logging Tag
    private static final String LOG_TAG = RecipeListAdapter.class.getSimpleName();

    // Click Handler
    private final RecipeListAdapterOnClickHandler mClickHandler;

    // List to move about the recipes
    private List<RecipeBase> mRecipeBaseList;

    // Context
    private Context mContext;

    public RecipeListAdapter(Context context, RecipeListAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    // TODO: Might be able to delete this & Might also be able to get rid of the if statement
    public void swapList(List<RecipeBase> newList) {
        if (newList != null && newList.size() > 0) {
            mRecipeBaseList = newList;
            notifyDataSetChanged();
        }
    }

    @Override
    public RecipeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Obtain & return layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recipe_card_view_item, parent, false);
        return new RecipeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeListViewHolder holder, int position) {
        //Obtain current RecipeBase object
        RecipeBase recipeBase = mRecipeBaseList.get(position);
        holder.bind(recipeBase);
    }


    @Override
    public int getItemCount() {
        if (mRecipeBaseList != null) {
            return mRecipeBaseList.size();
        } else {
            return 0;
        }
    }

    public interface RecipeListAdapterOnClickHandler {
        void onListItemClick(RecipeBase recipeBase);
    }

    // TODO: Set this up once there's enough data to create the card views
    public class RecipeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_recipe_picture)
        ImageView recipePicture;
        @BindView(R.id.tv_recipe_name)
        TextView recipeName;
        @BindView(R.id.tv_recipe_servings)
        TextView recipeServings;

        public RecipeListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(RecipeBase recipeBase) {
            //Handle Recipe Image
            if (!(recipeBase.getImage().isEmpty())) {
                Picasso.with(itemView.getContext())
                        .load(recipeBase.getImage())
                        .into(recipePicture);
            } else {
                //TODO: This may not function properly
                recipePicture.setVisibility(View.INVISIBLE);
            }

            //Recipe Name
            recipeName.setText(recipeBase.getName());

            //Recipe Servings
            //TODO: Learn more about string formatting and possibly add that to the string
            //https://developer.android.com/guide/topics/resources/string-resource.html
            String servingSizeString = mContext.getString(R.string.recipe_servings) + recipeBase.getServings();
            recipeServings.setText(servingSizeString);

            Log.d(LOG_TAG, "Here is the current recipe in [RecipeListAdapter]: " + recipeBase.getID());
        }

        @Override
        public void onClick(View v) {
            Log.d(LOG_TAG, "onClick of [RecipeListAdapter]");
            //Get recipeBase
            RecipeBase recipeBase = mRecipeBaseList.get(getAdapterPosition());

            //Send the relevant recipeBase
            mClickHandler.onListItemClick(recipeBase);
        }
    }
}
