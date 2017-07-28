package com.omegaspocktari.bakersdelight.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.omegaspocktari.bakersdelight.R;
import com.omegaspocktari.bakersdelight.widget.RecipeWidgetProvider;

public class MainActivity extends AppCompatActivity {

    //Logging Tag
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    //Preference Writer
    private int mRecipeSelected;

    //Context
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main_tablet_recipe_list_holder, new RecipeListFragment())
                    .commit();
        }

        mContext = this;

        RecipeWidgetProvider.sendRefreshBroadcast(mContext);
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
