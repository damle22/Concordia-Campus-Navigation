package minicap.concordia.campusnav.buildingmanager.entities;

public class Location
{
    private final float latitude;
    private final float longitude;

    protected Location(float latitude, float longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns the latitude and longitude of the location
     * @return 2 value float array with latitude in index 0 and longitude in index 1
     */
    public float[] getLocation() {
        return new float[] {latitude, longitude};
    }
}
