package minicap.concordia.campusnav;

import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import minicap.concordia.campusnav.screens.MainActivity;
import minicap.concordia.campusnav.screens.MapsActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MainActivityEndToEndTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();  // It initialize Espresso Intents
    }

    @After
    public void tearDown() {
        Intents.release();  // It releases Espresso Intents to avoid memory leaks
    }

    @Test
    public void testSgwCampusButtonLaunchesMapsActivity() {
        // User clicks the SGW Campus button
        onView(withId(R.id.viewSGWCampusButton)).perform(click());

        // Retrieve the actual Intent
        Intent actualIntent = Intents.getIntents().get(0);

        // Asserts that the component is MapsActivity
        assertEquals("minicap.concordia.campusnav.screens.MapsActivity", actualIntent.getComponent().getClassName());

        // Check that the extras contain the correct latitude and longitude using TOLERANCE AS PRECISION HERE
        double startingLat = actualIntent.getDoubleExtra(MapsActivity.KEY_STARTING_LAT, 0.0);
        double startingLng = actualIntent.getDoubleExtra(MapsActivity.KEY_STARTING_LNG, 0.0);

        assertEquals(45.49701, startingLat, 0.0001);
        assertEquals(-73.57877, startingLng, 0.0001);
    }

    @Test
    public void testLoyCampusButtonLaunchesMapsActivity() {
        // User clicks the Loyola Campus button
        onView(withId(R.id.viewLoyCampusButton)).perform(click());

        // Retrieve the actual Intent
        Intent actualIntent = Intents.getIntents().get(0);

        // Asserts that the component is MapsActivity
        assertEquals("minicap.concordia.campusnav.screens.MapsActivity", actualIntent.getComponent().getClassName());

        // Check that the extras contain the correct latitude and longitude USING TOLERANCE AS PRECISION HERE
        double startingLat = actualIntent.getDoubleExtra(MapsActivity.KEY_STARTING_LAT, 0.0);
        double startingLng = actualIntent.getDoubleExtra(MapsActivity.KEY_STARTING_LNG, 0.0);

        assertEquals(45.45863, startingLat, 0.0001);
        assertEquals(-73.64188, startingLng, 0.0001);
    }
}
