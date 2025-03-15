package minicap.concordia.campusnav.map;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public abstract class AbstractMap {

    protected boolean isMapReady = false;

    protected MapUpdateListener listener;

    public AbstractMap(MapUpdateListener listener) {
        this.listener = listener;
    }

    /**
     * Starts the initialization process for the map and returns the fragment to display
     * @return The fragment used to display the map
     */
    public abstract Fragment initialize();

    public abstract void addMarker(double lat, double lng, String title, float color, boolean clearOtherMarkers);

    public abstract void addMarker(double lat, double lng, String title, float color);

    public abstract void addMarker(double lat, double lng, String title, boolean clearOtherMarkers);

    public abstract void addMarker(double lat, double lng, String title);

    public abstract void clearAllMarkers();

    public abstract void clearPathFromMap();

    public abstract void displayRoute(double originLat, double originLng, double destinationLat, double destinationLng, String travelMode);

    /**
     * Centers the map on the given coordinates
     * @param latitude the latitude as a double
     * @param longitude the longitude as a double
     */
    public abstract void centerOnCoordinates(double latitude, double longitude);

    //TODO: Uncomment when Route/RouteCreator is made
    //public abstract Route getDirections(float[] start, float[] destination);

    /**
     * Switches the current floor to another one
     * @param floorName the name of the floor that's being switched to
     */
    public abstract void switchToFloor(String floorName);

    /**
     * Enables current location tracking on the map
     * @param isEnabled To enable or disable location tracking
     * @return True if successful, false otherwise
     */
    public abstract boolean toggleLocationTracking(boolean isEnabled);

    //TODO: Uncomment when we can display routes
    //public abstract void displayRoute();

    public interface MapUpdateListener {
        void onMapReady();

        void onEstimatedTimeUpdated(String newTime);

        void onMapError(String errorString);

        void onMapClicked(double latitude, double longitude);
    }
}
