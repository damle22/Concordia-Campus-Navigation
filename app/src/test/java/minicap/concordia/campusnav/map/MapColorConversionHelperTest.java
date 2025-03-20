package minicap.concordia.campusnav.map;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.junit.Assert;
import org.junit.Test;

import minicap.concordia.campusnav.map.enums.MapColors;
import minicap.concordia.campusnav.map.helpers.MapColorConversionHelper;

public class MapColorConversionHelperTest {

    @Test
    public void testGetGoogleMapsColor_DefaultColor() {
        MapColors color = MapColors.DEFAULT;
        float expectedColor = BitmapDescriptorFactory.HUE_RED;

        float actual = MapColorConversionHelper.getGoogleMapsColor(color);

        Assert.assertEquals(expectedColor, actual, 0.0001);
    }

    @Test
    public void testGetGoogleMapsColor_NonDefaultColor() {
        MapColors color = MapColors.BLUE;
        float expectedColor = BitmapDescriptorFactory.HUE_AZURE;

        float actual = MapColorConversionHelper.getGoogleMapsColor(color);

        Assert.assertEquals(expectedColor, actual, 0.0001);
    }

    @Test
    public void testGetMappedInMarkerHTML_DefaultColor() {
        String expectedTitle = "test";
        String expectedHTML = "<div style=\"background-color:white; border: 2px solid red; padding: 0.4rem; border-radius: 0.4rem;\">" + expectedTitle + "</div>";

        String actual = MapColorConversionHelper.getMappedInMarkerHTML(MapColors.DEFAULT, expectedTitle);

        Assert.assertEquals(expectedHTML, actual);
    }

    @Test
    public void testGetMappedInMarkerHTML_NonDefaultColor() {
        String expectedTitle = "test";
        String expectedHTML = "<div style=\"background-color:white; border: 2px solid blue; padding: 0.4rem; border-radius: 0.4rem;\">" + expectedTitle + "</div>";

        String actual = MapColorConversionHelper.getMappedInMarkerHTML(MapColors.BLUE, expectedTitle);

        Assert.assertEquals(expectedHTML, actual);
    }

}
