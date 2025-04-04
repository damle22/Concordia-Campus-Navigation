package minicap.concordia.campusnav.map;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class MapCoordinates implements Parcelable {
    private double lat;
    private double lng;

    public MapCoordinates(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    protected MapCoordinates(Parcel in) {
        this.lat = in.readDouble();
        this.lng = in.readDouble();
    }

    /**
     * Creates a MapCoordinates object from GoogleMaps LatLng
     * @param coordinate The LatLng to be translated
     * @return new MapCoordinates object with the properties of the LatLng
     */
    public static MapCoordinates fromGoogleMapsLatLng(LatLng coordinate) {
        return new MapCoordinates(coordinate.latitude, coordinate.longitude);
    }

    public static MapCoordinates fromAndroidLocation(Location loc) {
        return new MapCoordinates(loc.getLatitude(), loc.getLongitude());
    }

    /**
     * Creates a LatLng based on the current MapCoordinates
     * @return new LatLng
     */
    public LatLng toGoogleMapsLatLng() {
        return new LatLng(this.lat, this.lng);
    }

    /**
     * Gets the latitude
     * @return latitude
     */
    public double getLat() {
        return lat;
    }

    /**
     * Gets the longitude
     * @return longitude
     */
    public double getLng() {
        return lng;
    }

    public static final Parcelable.Creator<MapCoordinates> CREATOR = new Creator<MapCoordinates>() {
        @Override
        public MapCoordinates createFromParcel(Parcel source) {
            return new MapCoordinates(source);
        }

        @Override
        public MapCoordinates[] newArray(int size) {
            return new MapCoordinates[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
    }
}
