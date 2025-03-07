package minicap.concordia.campusnav.map;

public abstract class AbstractMap {

    /**
     * Centers the maps on the given coordinates
     * @param latitude the latitude as a float
     * @param longitude the longitude as a float
     */
    public abstract void centerOnCoordinates(float latitude, float longitude);

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
}
