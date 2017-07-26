package com.omegaspocktari.bakersdelight;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.omegaspocktari.bakersdelight.ui.MainActivity;
import com.omegaspocktari.bakersdelight.ui.RecipeListFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by ${Michael} on 7/25/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ActivityMainBasicTest {
    private static final String INGREDIENT = "unsalted butter, melted";
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_main_tablet_recipe_list_holder, new RecipeListFragment())
                .commit();
        ;
    }

    @Test
    public void startsUpNextFragment() {
        //Find the adapter, a specific view, and perform action on it
        onData(withId(R.id.cv_recipe))
                .inAdapterView(allOf(withParent(withId(R.id.cl_recipe_list)), withId(R.id.rv_recipe_list)))
                .atPosition(0)
                .perform(click());

        //Check if the adapter for recipeListFragment at position 1 has the child view with appropriate
        //ingredient text.
//        onData(anything()).inAdapterView(withId(R.id.rv_recipe_details_list)).atPosition(1).
//                onChildView(withId(R.id.tv_ingredient_ingredient)).check(matches(withText(INGREDIENT)));
    }
}
