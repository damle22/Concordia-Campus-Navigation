package minicap.concordia.campusnav.map;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.List;

import minicap.concordia.campusnav.map.enums.MapColors;

public abstract class AbstractMap {

    protected MapUpdateListener listener;

    /**
     * Constructor
     * @param listener Listener for map events
     */
    public AbstractMap(MapUpdateListener listener) {
        this.listener = listener;
    }

    /**
     * Starts the initialization process for the map and returns the fragment to display
     * @return The fragment used to display the map
     */
    public abstract Fragment initialize();

    /**
     * Adds a marker to the map with the specified parameters
     * @param position The position of the marker
     * @param title The title used to label the marker
     * @param color The color of the marker
     * @param clearOtherMarkers Flag to indicate whether to clear other markers
     */
    public abstract void addMarker(MapCoordinates position, String title, MapColors color, boolean clearOtherMarkers);

    /**
     * Adds a marker to the map of the specified color at the given position with the given title
     * @param position The position of the marker
     * @param title The title used to label the marker
     * @param color The color of the marker
     */
    public abstract void addMarker(MapCoordinates position, String title, MapColors color);

    /**
     * Adds a marker to the map at the given position with given title. Clears other markers if requested
     * @param position The position of the marker on the map
     * @param title The title used to label the marker
     * @param clearOtherMarkers Flag indicating whether to clear other markers
     */
    public abstract void addMarker(MapCoordinates position, String title, boolean clearOtherMarkers);

    /**
     * Adds a marker to the map at the given position with the given title
     * @param position The position of the marker on the map
     * @param title The title used to label the marker
     */
    public abstract void addMarker(MapCoordinates position, String title);

    /**
     * Clears all the markers from the map
     */
    public abstract void clearAllMarkers();

    /**
     * Clears the current path from the map
     */
    public abstract void clearPathFromMap();

    /**
     * Display a route from origin to destination on the map
     * @param origin The coordinates of the origin
     * @param destination The coordinates of the destination
     * @param travelMode The mode of travel to be used
     */
    public abstract void displayRoute(MapCoordinates origin, MapCoordinates destination, String travelMode);

    /**
     * Centers the map on the given coordinates
     * @param coordinates The coordinates used for centering
     */
    public abstract void centerOnCoordinates(MapCoordinates coordinates);

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

    //NEW ABSTRACTS METHODS DURING REFACTOR ============================================
    public void setStyle(Context context, int resourceID){
    }

    public void zoomCamera(MapCoordinates center, float routeZoom) {
    }

    public void moveCameraToPosition(int padding, MapCoordinates position, float zoom, float bearing){
    }

    /**
     * Zooms the camera to show the entire route smoothly
     * @param allPoints List of coordinates that make up the route
     */
    public void zoomToRouteSmoothly(List<MapCoordinates> allPoints) {
    }

    /**
     * Creates and adds a user location marker
     * @param position The position of the marker
     * @param iconResId The resource ID for the marker icon
     */
    public void createUserMarker(MapCoordinates position, int iconResId, Context context){
    }

    /**
     * Updates an existing user marker's position
     * @param position The new position
     * @param context The current context
     */
    public void updateUserMarkerPosition(MapCoordinates position, Context context){

    }

    public boolean isUserMarkerNull() {
        return true;
    }

    public void rotateUserMarker(float markerRotation) {

    }

    public MapCoordinates getMapCoordinateFromMarker() {
        return null;
    }

    /**
     * Clears all polylines from the map
     */
    public void clearAllPolylines(){
    }

    /**
     * Adds a polyline to the map using MapCoordinates
     * @param coordinates List of coordinates for the polyline
     * @param width Width of the polyline
     * @param color Color of the polyline
     * @param geodesic Whether the polyline should be geodesic
     */
    public void addPolyline(List<MapCoordinates> coordinates, int width, int color, boolean geodesic){
    }

    /**
     * Decodes an encoded polyline string into MapCoordinates
     * @param encodedPolyline The encoded polyline string
     * @return List of MapCoordinates
     */
    public List<MapCoordinates> decodePolyline(String encodedPolyline) {
        return null;
    }


    public float calculateRemainingDistance(MapCoordinates location) {
        return 0;
    }

    public float calculatePathBearing(MapCoordinates currentPosition) {
        return 0;
    }



    public interface MapUpdateListener {
        /**
         * Method called when the map is ready for use
         */
        void onMapReady();

        /**
         * Method called when a new estimated time is given
         * @param newTime String representation of the new estimate
         */
        void onEstimatedTimeUpdated(String newTime);

        /**
         * Method called when there is an error with the map
         * @param errorString The error that happened as a string
         */
        void onMapError(String errorString);

        /**
         * Method called when the map is clicked
         * @param coordinates The coordinates of where the map was clicked
         */
        void onMapClicked(MapCoordinates coordinates);
    }
}
