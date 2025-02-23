package minicap.concordia.campusnav.map;

public abstract class AbstractMap {

    public abstract void centerOnCoordinates(float latitude, float longitude);

    public abstract void centerOnCoordinates(double latitude, double longitude);

    //TODO: Uncomment when Route/RouteCreator is made
    //public abstract Route getDirections(float[] start, float[] destination);

    public abstract void switchToFloor(String floorName);

    public abstract boolean enableLocationTracking(boolean isEnabled);

    public abstract void displayRoute();
}
