package minicap.concordia.campusnav.map;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(concordia, defaultZoom));
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

    public void addMarker(MarkerOptions markerOptions, boolean clearOtherMarkers) {
        if(clearOtherMarkers) {
            mMap.clear();
        }

        addMarker(markerOptions);
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

    public void parseRoutePolylineAndDisplay(JSONArray steps) throws JSONException {
        // Looping trough steps to display on UI
        for (int i = 0; i < steps.length(); i++) {

            JSONObject step = steps.getJSONObject(i);
            String travelMode = step.getString("travelMode");
            JSONObject polylineObject = step.getJSONObject("polyline");
            String encodedPolyline = polylineObject.getString("encodedPolyline");
            List<LatLng> stepPoints = PolyUtil.decode(encodedPolyline);

            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(stepPoints)
                    .width(10);

            //Case where Walking
            if ("WALK".equals(travelMode)) {
                polylineOptions.color(Color.BLUE)
                        .pattern(Arrays.asList(new Dot(), new Gap(10)));
                //Case where Transit
            } else if ("TRANSIT".equals(travelMode)) {
                JSONObject transitDetails = step.getJSONObject("transitDetails");
                String vehicleType = transitDetails.getJSONObject("transitLine")
                        .getJSONObject("vehicle")
                        .getString("type");
                String transitLineName = transitDetails.getJSONObject("transitLine").getString("name");

                // Set color based on vehicle type
                if ("BUS".equalsIgnoreCase(vehicleType)) {
                    polylineOptions.color(Color.parseColor("#000000"));
                } else if ("SUBWAY".equalsIgnoreCase(vehicleType) || "METRO".equalsIgnoreCase(vehicleType)) {
                    switch (transitLineName) {
                        case "Ligne Verte":
                            polylineOptions.color(Color.parseColor("#008000"));
                            break;
                        case "Ligne Orange":
                            polylineOptions.color(Color.parseColor("#FFA500"));
                            break;
                        case "Ligne Jaune":
                            polylineOptions.color(Color.parseColor("#FFD700"));
                            break;
                        case "Ligne Bleu":
                            polylineOptions.color(Color.parseColor("#0000FF"));
                        default:
                            polylineOptions.color(Color.GRAY); // Default in case the name changes
                            break;
                    }
                }
            }

            addPolyline(polylineOptions);
        }
    }
}
