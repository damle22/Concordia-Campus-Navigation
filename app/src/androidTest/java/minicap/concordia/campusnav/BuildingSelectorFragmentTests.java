package minicap.concordia.campusnav;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.RecyclerViewActions;

import org.junit.Test;
import org.junit.runner.RunWith;

import minicap.concordia.campusnav.screens.MapsActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented tests for the BuildingSelectorFragment via the MapsActivity.
 *
 * 1. Launches MapsActivity.
 * 2. Clicks the "buildingView" MaterialButton to open BuildingSelectorFragment.
 * 3. Checks that SGW and Loyola RecyclerViews are displayed.
 * 4. Taps an item in SGW list and verifies an AlertDialog titled "Building Details" appears.
 */
@RunWith(AndroidJUnit4.class)
public class BuildingSelectorFragmentTests {

    @Test
    public void testBuildingSelectorFragmentLaunchesAndDisplaysList() {
        // 1) Launch the MapsActivity
        ActivityScenario<MapsActivity> scenario = ActivityScenario.launch(MapsActivity.class);

        // 2) Click the "buildingView" button to show the BuildingSelectorFragment
        onView(withId(R.id.buildingView)).perform(click());

        // 3) Check if the SGW RecyclerView is displayed
        onView(withId(R.id.sgwRecyclerView))
                .check(matches(isDisplayed()));

        // 4) Check if the Loyola RecyclerView is also displayed (optional but good to verify)
        onView(withId(R.id.loyRecyclerView))
                .check(matches(isDisplayed()));

        // 5) Perform a click on the first item in the SGW RecyclerView
        onView(withId(R.id.sgwRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // 6) Verify that an AlertDialog with the title "Building Details" is displayed
        onView(withText("Building Details"))
                .check(matches(isDisplayed()));
    }
}
