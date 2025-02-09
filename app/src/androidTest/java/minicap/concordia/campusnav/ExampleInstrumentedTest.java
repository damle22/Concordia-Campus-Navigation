package minicap.concordia.campusnav;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.google.android.gms.maps.model.LatLng;

import minicap.concordia.campusnav.helpers.CoordinateResHelper;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("minicap.concordia.campusnav", appContext.getPackageName());
    }

    @Test
    public void coordResHelper_GetGoogleCoords_ShouldReturnCorrectLatLng() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        double expectedHallLat = 45.49701;
        double expectedHallLng = -73.57877;

        LatLng testCoords = CoordinateResHelper.getCoordsForGoogleMaps(appContext, CoordinateResHelper.BuildingNames.SGWHallBuilding);

        //It's possible when it's parsed into LatLng that it adjusts the coordinate slightly (or may have a higher precision than we provide)
        assertEquals(expectedHallLat, testCoords.latitude, 0.001);
        assertEquals(expectedHallLng, testCoords.longitude, 0.001);
    }

    @Test
    public void shouldFail() {
        assertEquals(0,1);
    }
}