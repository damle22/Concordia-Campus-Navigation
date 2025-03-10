package minicap.concordia.campusnav.map;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InternalGoogleMaps extends AbstractMap{
    private final float defaultZoom = 18;
    private final GoogleMap mMap;
    private List<Polyline> polylines = new ArrayList<>();

    public InternalGoogleMaps(GoogleMap map){
        mMap = map;
    }

    @Override
    public void centerOnCoordinates(float latitude, float longitude) {
        centerOnCoordinates((double)latitude, (double)longitude);
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

    public void addPolygons(List<PolygonOptions> options) {
        for (PolygonOptions polygonOptions : options){
            mMap.addPolygon(polygonOptions);
        }
    }

    /**
     * Adds marker to the googleMaps
     * @param markerOptions MarkerOptions
     */
    public void addMarker(MarkerOptions markerOptions){
        mMap.addMarker(markerOptions);
    }
    /**
     * Adds PolyLine to the googleMaps
     * @param options PolylineOptions
     */
    public void addPolyline(PolylineOptions options){
        polylines.add(mMap.addPolyline(options));
    }

    /**
     * Removes All polylines
     */
    public void clearPolyLines(){
        for (Iterator<Polyline> it = polylines.iterator(); it.hasNext();) {
            Polyline polyline = it.next();
            polyline.remove();
            it.remove();
        }
    }
    public void animateCameraToLocation(LatLng location, float zoom) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoom));
    }

    public Marker updateCampusMarker(Marker marker, LatLng campus, boolean showSGW) {
        String campusTitle = showSGW ? "Loyola Campus" : "SGW Campus";
        if (marker != null) {
            marker.setPosition(campus);
            marker.setTitle(campusTitle);
            return marker;
        } else {
            return mMap.addMarker(new MarkerOptions().position(campus).title(campusTitle));
        }
    }

    @Override
    public boolean toggleLocationTracking(boolean isEnabled) {
        try {
            mMap.setMyLocationEnabled(isEnabled);
            return true;
        } catch (SecurityException e) {
            Log.d("InternalGoogleMaps", "Permissions not granted for location");
            return false;
        }
    }
}
