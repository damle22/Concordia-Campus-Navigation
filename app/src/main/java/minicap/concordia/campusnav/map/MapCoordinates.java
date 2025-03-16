package minicap.concordia.campusnav.map;

import com.google.android.gms.maps.model.LatLng;
import com.mappedin.sdk.models.MPIMap;

public class MapCoordinates {
    private double lat;

    private double lng;

    private double x;

    private double y;

    public MapCoordinates(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
        this.x = 0;
        this.y = 0;
    }

    public MapCoordinates(double lat, double lng, double x, double y) {
        this(lat, lng);
        this.x = x;
        this.y = y;
    }

    public static MapCoordinates fromGoogleMapsLatLng(LatLng coordinate) {
        return new MapCoordinates(coordinate.latitude, coordinate.longitude);
    }

    public static MapCoordinates fromMappedInCoordinate(MPIMap.MPICoordinate coordinate) {
        return new MapCoordinates(coordinate.getLatitude(), coordinate.getLongitude(), coordinate.getX(), coordinate.getY());
    }

    public LatLng toGoogleMapsLatLng() {
        return new LatLng(this.lat, this.lng);
    }

    public MPIMap.MPICoordinate toMappedInCoordinate(MPIMap map) {
        return new MPIMap.MPICoordinate(this.x, this.y, this.lat, this.lng, map);
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
