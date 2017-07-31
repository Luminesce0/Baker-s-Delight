package com.omegaspocktari.bakersdelight.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.omegaspocktari.bakersdelight.R;
import com.omegaspocktari.bakersdelight.widget.RecipeWidgetProvider;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    //Logging Tag
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    //Loader ID
    private static final int RECIPE_RESULTS_LOADER = 1;
    //Bundle key for Url
    private static final String RECIPE_INFO_URL = "recipeUrl";
    //Context
    Context mContext;
    //Preference Writer
    private boolean mTwoPane;
    //String value for our intent
    private String mIntentIdentifier;
    //Integer value for current recipe
    private int mRecipeSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //Verify the intent
        verifyIntent();

        //Check which fragment should be started
        if (mIntentIdentifier != null && mIntentIdentifier.equals(getString(R.string.recipe_widget_key))) {

            if (this.findViewById(R.id.activity_main_tablet_recipe_step_detail_holder) != null) {
                mTwoPane = true;
            } else {
                mTwoPane = false;
            }

            Fragment fragment = new RecipeDetailFragment();
            Fragment fragmentRemove = new RecipeListFragment();

            //Create bundle to attach to and send with the fragment
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.recipe_widget_key), getString(R.string.recipe_widget_key));
            bundle.putBoolean(getString(R.string.tablet_layout_key), mTwoPane);
            bundle.putInt(getString(R.string.recipe_widget_preference_key), mRecipeSelected);
            fragment.setArguments(bundle);

            //Create the transaction to interact with the stack and add previous fragment to the backstack
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(fragmentRemove);
            transaction.replace(R.id.activity_main_tablet_recipe_list_holder, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (savedInstanceState == null) {
            Log.d(LOG_TAG, "Starting up the other thing /!\\ WARNING /!\\");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main_tablet_recipe_list_holder, new RecipeListFragment())
                    .commit();
        }

        mContext = this;

        //Update widget
        RecipeWidgetProvider.sendRefreshBroadcast(mContext);
    }

    public void verifyIntent() {
        Intent intent = getIntent();
        mIntentIdentifier = intent.getStringExtra(getString(R.string.recipe_widget_key));
        mRecipeSelected = intent.getIntExtra(getString(R.string.recipe_widget_preference_key), 0);
    }

    @Override
    protected void onStart() {

        //Start up RecipeDetailFragment
        if (mIntentIdentifier != null && mIntentIdentifier.equals(getString(R.string.recipe_widget_key))) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main_tablet_recipe_list_holder, new RecipeDetailFragment())
                    .commit();
        }

        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
