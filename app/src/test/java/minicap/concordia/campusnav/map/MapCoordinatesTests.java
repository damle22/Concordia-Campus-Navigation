package minicap.concordia.campusnav.map;

import com.google.android.gms.maps.model.LatLng;
import com.mappedin.sdk.models.MPIMap;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class MapCoordinatesTests {

    @Test
    public void testGetLatitude_ReturnsCorrectLatitude() {
        double expectedLat = 12;
        double expectedLng = 0;

        MapCoordinates coords = new MapCoordinates(expectedLat, expectedLng);

        Assert.assertEquals(expectedLat, coords.getLat(), 0.0001);
    }

    @Test
    public void testGetLongitude_ReturnsCorrectLongitude() {
        double expectedLat = 0;
        double expectedLng = 12;

        MapCoordinates coordinates = new MapCoordinates(expectedLat, expectedLng);

        Assert.assertEquals(expectedLng, coordinates.getLng(), 0.0001);
    }

    @Test
    public void testGetX_ReturnsCorrectX() {
        double expectedLat = 0;
        double expectedLng = 0;
        double expectedX = 12;
        double expectedY = 0;

        MapCoordinates coordinates = new MapCoordinates(expectedLat, expectedLng, expectedX, expectedY);

        Assert.assertEquals(expectedX, coordinates.getX(), 0.0001);
    }

    @Test
    public void testGetY_ReturnsCorrectY() {
        double expectedLat = 0;
        double expectedLng = 0;
        double expectedX = 0;
        double expectedY = 12;

        MapCoordinates coordinates = new MapCoordinates(expectedLat, expectedLng, expectedX, expectedY);

        Assert.assertEquals(expectedY, coordinates.getY(), 0.0001);
    }

    @Test
    public void testCreateFromLatLng_ReturnsCorrectMapCoordinates() {
        double expectedLat = 12;
        double expectedLng = 15;

        LatLng expectedLatLng = new LatLng(expectedLat, expectedLng);

        MapCoordinates coordinates = MapCoordinates.fromGoogleMapsLatLng(expectedLatLng);

        Assert.assertEquals(expectedLat, coordinates.getLat(), 0.0001);
        Assert.assertEquals(expectedLng, coordinates.getLng(), 0.0001);
    }

    @Test
    public void testCreateFromMPICoordinate_ReturnsCorrectMapCoordinates() {
        MPIMap mapMock = Mockito.mock(MPIMap.class);
        double expectedLat = 12;
        double expectedLng = 17;
        double expectedX = 20;
        double expectedY = 25;

        MPIMap.MPICoordinate coordinate = new MPIMap.MPICoordinate(expectedX, expectedY, expectedLat, expectedLng, mapMock);

        MapCoordinates coordinates = MapCoordinates.fromMappedInCoordinate(coordinate);

        Assert.assertEquals(expectedLat, coordinates.getLat(), 0.0001);
        Assert.assertEquals(expectedLng, coordinates.getLng(), 0.0001);
        Assert.assertEquals(expectedX, coordinates.getX(), 0.0001);
        Assert.assertEquals(expectedY, coordinates.getY(), 0.0001);
    }

    @Test
    public void testCreateLatLngFromCoordinates_ReturnsCorrectLatLng() {
        double expectedLat = 12;
        double expectedLng = 17;

        MapCoordinates coords = new MapCoordinates(expectedLat, expectedLng);

        LatLng actual = coords.toGoogleMapsLatLng();

        Assert.assertEquals(expectedLat, actual.latitude, 0.0001);
        Assert.assertEquals(expectedLng, actual.longitude, 0.0001);
    }

    @Test
    public void testCreateMPICoordinateFromCoordinates_ReturnsCorrectMPICoordinate() {
        MPIMap mapMock = Mockito.mock(MPIMap.class);
        double expectedLat = 12;
        double expectedLng = 20;
        double expectedX = 15;
        double expectedY = 27;

        MapCoordinates coords = new MapCoordinates(expectedLat, expectedLng, expectedX, expectedY);

        MPIMap.MPICoordinate actual = coords.toMappedInCoordinate(mapMock);

        Assert.assertEquals(expectedLat, actual.getLatitude(), 0.0001);
        Assert.assertEquals(expectedLng, actual.getLongitude(), 0.0001);
        Assert.assertEquals(expectedX, actual.getX(), 0.0001);
        Assert.assertEquals(expectedY, actual.getY(), 0.0001);
    }
}
