package minicap.concordia.campusnav.buildingmanager.entities;

public class Location
{
    private final double latitude;
    private final double longitude;

    protected Location(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns the latitude and longitude of the location
     * @return 2 value float array with latitude in index 0 and longitude in index 1
     */
    public double[] getLocation() {
        return new double[] {latitude, longitude};
    }
}
