package minicap.concordia.campusnav.map;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class InternalGoogleMaps extends AbstractMap{
    private final float defaultZoom = 18;
    private final GoogleMap mMap;

    public InternalGoogleMaps(GoogleMap map){
        mMap = map;
    }

    @Override
    public void centerOnCoordinates(float latitude, float longitude) {
        centerOnCoordinates(latitude, longitude);
    }

    @Override
    public void centerOnCoordinates(double latitude, double longitude){
        LatLng concordia = new LatLng(latitude, longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(concordia, defaultZoom));
    }

    @Override
    public void switchToFloor(String floorName) {
        //Google maps does not have floors, so do nothing
    }

    @Override
    public boolean enableLocationTracking(boolean isEnabled) {
        try{
            mMap.setMyLocationEnabled(isEnabled);
            return true;
        }
        catch(SecurityException e){
            Log.d("InternalGoogleMaps", "Permissions not granted for location");
            return false;
        }
    }

    @Override
    public void displayRoute() {
        //TODO: To be implemented
    }
}
