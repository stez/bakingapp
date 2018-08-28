package it.and.stez78.bakingapp;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.and.stez78.bakingapp.app.RecipeActivity;
import it.and.stez78.bakingapp.model.Recipe;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    private static final Long RECIPE_ID = 1L;
    private static final String RECIPE_NAME = "TestRecipe";

    @Rule
    public ActivityTestRule<RecipeActivity> activityRule =
            new ActivityTestRule(RecipeActivity.class, true, false);

    @Before
    public void createRecipe() {

        Recipe testRecipe = new Recipe();
        testRecipe.setName(RECIPE_NAME);
        testRecipe.setId(RECIPE_ID);

        Intent recipeIntent = new Intent();
        recipeIntent.putExtra(RecipeActivity.RECIPE_KEY, testRecipe);
        activityRule.launchActivity(recipeIntent);
    }

    @Test
    public void recipeActivityOpensAndToolbarTitleIsSet() {
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.activity_recipe_toolbar))))
                .check(matches(withText(RECIPE_NAME)));
    }
}
