package minicap.concordia.campusnav;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import com.google.android.gms.maps.model.LatLng;
import minicap.concordia.campusnav.screens.MapsActivity;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class SelectCoordinatesUnitTest {

    //Test if coordinates retrieved matches coordinates clicked
    @Test
    public void isValidCoordinatesRouted() {

        MapsActivity activity = new MapsActivity();
        LatLng testLatLng = new LatLng(1,1);
        activity.setLatLng(testLatLng);
        assertEquals(testLatLng,activity.getLatLng());

    }

}
