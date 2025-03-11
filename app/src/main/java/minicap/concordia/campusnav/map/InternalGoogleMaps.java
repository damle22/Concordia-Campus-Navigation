package minicap.concordia.campusnav.map;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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

    private List<Marker> markers = new ArrayList<>();

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

    /**
     * Add polygons to the map (used for building shapes)
     * @param options A list of PolygonOptions to be added to the map
     */
    public void addPolygons(List<PolygonOptions> options) {
        for (PolygonOptions polygonOptions : options){
            mMap.addPolygon(polygonOptions);
        }
    }

    /**
     * Adds a marker to the map based on position with the title and color given, with the option to clear other markers
     * @param position The position of the marker
     * @param title The title used for the marker
     * @param color The color of the marker (use BitmapDescriptorFactory to get the color float)
     * @param clearOtherMarkers Flag to indicate whether to clear other markers on the map
     */
    public void addMarker(LatLng position, String title, float color, boolean clearOtherMarkers){
        if(clearOtherMarkers) {
            clearAllMarkers();
        }

        MarkerOptions newMarker = new MarkerOptions()
                                        .icon(BitmapDescriptorFactory.defaultMarker(color))
                                        .position(position)
                                        .title(title);
        markers.add(mMap.addMarker(newMarker));
    }

    /**
     * Adds a marker to the map using the default color, does not clear other markers
     * @param position The position of the marker
     * @param title The title of the marker
     */
    public void addMarker(LatLng position, String title) {
        addMarker(position, title, BitmapDescriptorFactory.HUE_RED, false);
    }

    /**
     * Adds a marker to the map, does not clear other markers
     * @param position The position of the marker
     * @param title The title of the marker
     * @param color The color of the marker (use BitmapDescriptorFactory to get the color float)
     */
    public void addMarker(LatLng position, String title, float color) {
        addMarker(position, title, color, false);
    }

    /**
     * Adds a marker to the map, clearing other markers if desired
     * @param position The position of the marker
     * @param title The title of the marker
     * @param clearOtherMarkers Flag indicating whether to clear other markers
     */
    public void addMarker(LatLng position, String title, boolean clearOtherMarkers) {
        addMarker(position, title, BitmapDescriptorFactory.HUE_RED, clearOtherMarkers);
    }

    /**
     * Clears all the current markers from the map
     */
    public void clearAllMarkers() {
        for (Iterator<Marker> allMarkers = markers.iterator(); allMarkers.hasNext();) {
            Marker cur = allMarkers.next();
            cur.remove();
            allMarkers.remove();
        }
    }

    /**
     * Adds PolyLine to the googleMaps
     * @param options PolylineOptions
     */
    public void addPolyline(PolylineOptions options){
        polylines.add(mMap.addPolyline(options));
    }

    /**
     * Removes all polylines from the map (used for route)
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

    /**
     * Sets an onClick listener for the map
     * @param listener The listener to be set
     */
    public void setOnMapClickListener(GoogleMap.OnMapClickListener listener) {
        mMap.setOnMapClickListener(listener);
    }

    /**
     * Parses the JSONArray given by the Routes API to create a polyline
     * @param steps The array of steps in the line
     * @throws JSONException Thrown when there is an error parsing the JSON
     */
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
                        case "Verte":
                            polylineOptions.color(Color.parseColor("#008000"));
                            break;
                        case "Ligne Orange":
                        case "Orange":
                            polylineOptions.color(Color.parseColor("#FFA500"));
                            break;
                        case "Ligne Jaune":
                        case "Jaune":
                            polylineOptions.color(Color.parseColor("#FFD700"));
                            break;
                        case "Ligne Bleu":
                        case "Blue":
                            polylineOptions.color(Color.parseColor("#0000FF"));
                            break;
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
