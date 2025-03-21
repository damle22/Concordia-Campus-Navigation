package minicap.concordia.campusnav;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBackUnconditionally;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.Manifest;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.espresso.intent.Intents;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import minicap.concordia.campusnav.screens.MainActivity;
import minicap.concordia.campusnav.screens.MapsActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ToggleCampusE2ETest {


    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private ActivityScenario<MainActivity> scenario;

    @Before
    public void setUp() {
        Intents.init();
        scenario = activityScenarioRule.getScenario();
    }

    @After
    public void tearDown() {
        Intents.release();
        if (scenario != null) {
            scenario.close();
        }
    }

    @Test
    public void testToggleCampusFunctionality() {
        onView(isRoot()).perform(waitFor(500));

        onView(withId(R.id.viewSGWCampusButton))
                .perform(click());

        intended(hasComponent(MapsActivity.class.getName()));

        onView(isRoot()).perform(waitFor(500));

        onView(withId(R.id.ToCampus))
                .check(matches(withText("LOY")));

        onView(withText("LOY"))
                .perform(click());

        onView(withId(R.id.ToCampus))
                .check(matches(withText("SGW")));

        onView(withText("SGW"))
                .perform(click());
        
        onView(withId(R.id.ToCampus))
                .check(matches(withText("LOY")));

        pressBackUnconditionally();
    }

    /**
     * Simple ViewAction to wait on the main thread for 'ms' milliseconds.
     */
    private ViewAction waitFor(long ms) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }
            @Override
            public String getDescription() {
                return "Wait for " + ms + "ms";
            }
            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(ms);
            }
        };
    }
}
