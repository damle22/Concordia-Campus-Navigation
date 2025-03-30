package minicap.concordia.campusnav.screens;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;


import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.components.MainMenuDialog;
import minicap.concordia.campusnav.map.AbstractMap;
import minicap.concordia.campusnav.map.FetchPathTask;
import minicap.concordia.campusnav.map.MapCoordinates;
import minicap.concordia.campusnav.map.NavigationGoogleMaps;
import minicap.concordia.campusnav.map.enums.MapColors;

public class NavigationActivity extends AppCompatActivity implements FetchPathTask.OnRouteFetchedListener, AbstractMap.MapUpdateListener, MainMenuDialog.MainMenuListener {

    private static final int LOCATION_REQUEST_CODE = 101;
    private static final float DEFAULT_ZOOM = 18f;
    private static final float ROUTE_ZOOM = 15f;
    private static final float ROUTE_UPDATE_DISTANCE_THRESHOLD = 50;

    private AbstractMap curMap;
    private Fragment curMapFragment;
    private FusedLocationProviderClient locationClient;
    private LocationCallback locationCallback;

    private MapCoordinates origin;
    private MapCoordinates destination;
    private Marker userMarker;
    private String travelMode;

    private Marker destinationMarker;
    private TextView etaText;
    private TextView statsText;
    private ImageButton exit;
    private ImageButton mainMenu;
    private JSONArray routeData;
    private boolean isNavigationActive = false;
    private LatLng lastRouteUpdatePosition;

    /**
     * On create method, runs when activity is created
     * @param savedInstanceState bundle data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set layout to activity navigation
        setContentView(R.layout.activity_navigation);

        //Run routine
        initializeViews();
        getRouteDataFromIntent();
        setupLocationClient();
        initializeMap();
    }

    /**
     * Initializing the UI components
     */
    private void initializeViews() {
        //Grabing UI elements from the layout
        etaText = findViewById(R.id.eta_text);
        statsText = findViewById(R.id.statsText);
        mainMenu = findViewById(R.id.mainMenu);
        exit = findViewById(R.id.exitButton);

        //Setting up necessary buttons
        mainMenu.setOnClickListener(v -> showMainMenuDialog());
        exit.setOnClickListener(v -> exitIntent());
    }

    private void getRouteDataFromIntent() {
        try {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                throw new IllegalStateException("No extras in intent");
            }

            origin = new MapCoordinates(extras.getDouble("origin_lat"), extras.getDouble("origin_lng"));
            destination = new MapCoordinates(extras.getDouble("destination_lat"), extras.getDouble("destination_lng"));
            travelMode = extras.getString("travel_mode", "WALK");

//            String routeJson = extras.getString("route_data");
//            if (routeJson != null) {
//                routeData = new JSONArray(routeJson);
//            }
        } catch (Exception e) {
            Log.e("Navigation", "Error parsing intent data", e);
            Toast.makeText(this, "Invalid navigation data", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupLocationClient() {
        locationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                for (Location location : locationResult.getLocations()) {

                    updateUserPosition(MapCoordinates.fromAndroidLocation(location));
                }
            }
        };
    }

    /**
     * Initializing the map through the activity
     */
    private void initializeMap() {
        //set the curMap variable to an Internal Google maps
        curMap = new NavigationGoogleMaps(this);

        //get fragment from initialized variable
        curMapFragment = curMap.initialize();

        getSupportFragmentManager().beginTransaction().add(R.id.navigation_map, curMapFragment)
                .commit();

    }

    @Override
    public void onMapReady() {
        try {
            //Set map style
            curMap.setStyle(this,R.raw.nav_map_style);
            //setup map markers
            setupMapMarkers();

            if (routeData != null) {
                onRouteFetched(routeData);
            } else {
                fetchAndDisplayRoute();
            }

            checkLocationPermissions();
        } catch (Exception e) {
            Log.e("Navigation", "Map ready error", e);
            Toast.makeText(this, "Map setup failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupMapMarkers() {
        curMap.addMarker(
                destination,
                "Destination",
                MapColors.DEFAULT,
                true
        );
    }

    private void fetchAndDisplayRoute() {
        if (/* curMap.getmMap() == null || */origin == null || destination == null) {
            Toast.makeText(this, "Location data not available", Toast.LENGTH_SHORT).show();
            return;
        }

        //curMap.displayRoute(origin, destination, travelMode);

        new FetchPathTask(this).fetchRoute(origin.toGoogleMapsLatLng(), destination.toGoogleMapsLatLng(), travelMode);

        curMap.zoomCamera(origin,ROUTE_ZOOM);

    }

    private void fetchAndDisplayRoute(LatLng origin, LatLng destination) {
        //if (curMap.getmMap() == null) return;

        new FetchPathTask(this).fetchRoute(origin, destination, travelMode);
        isNavigationActive = true;
    }

    private void updateUserPosition(MapCoordinates location) {
        //if (location == null || /*curMap.getmMap() == null ||*/ routePolylines.isEmpty()) return;

        LatLng userLatLng = location.toGoogleMapsLatLng();

        float remainingDistance = calculateRemainingDistance(userLatLng);
        updateStatsText((int) remainingDistance);

        float pathBearing = calculatePathBearing(userLatLng);
        updateUserMarker(location);
        updateCameraPosition(userLatLng, pathBearing);

        if (isNavigationActive && shouldUpdateRoute(userLatLng)) {
            lastRouteUpdatePosition = userLatLng;
            fetchAndDisplayRoute(userLatLng, destination.toGoogleMapsLatLng());
        }
    }

    private void updateUserMarker(MapCoordinates position) {
        if (userMarker == null) {
            userMarker = curMap.createUserMarker(position, R.drawable.token, this);
        } else {
            curMap.updateUserMarkerPosition(userMarker,position);
        }
    }

    private void updateCameraPosition(LatLng position, float bearing) {
        //if (curMap.getmMap() == null) return;

        float markerRotation = (bearing + 110) % 360;

        if (userMarker != null) {
            userMarker.setRotation(markerRotation);
        }

        CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(DEFAULT_ZOOM).bearing(bearing).tilt(45).build();

        int padding = (int)(getResources().getDisplayMetrics().heightPixels * 0.3);
        curMap.moveCameraToPosition(cameraPosition, padding);

    }

    private boolean shouldUpdateRoute(LatLng currentPosition) {
        if (lastRouteUpdatePosition == null) {
            return true;
        }

        float[] results = new float[1];
        Location.distanceBetween(currentPosition.latitude, currentPosition.longitude, lastRouteUpdatePosition.latitude, lastRouteUpdatePosition.longitude, results);
        return results[0] > ROUTE_UPDATE_DISTANCE_THRESHOLD;
    }

    public void onRouteFetched(JSONArray routeInfo) {
        runOnUiThread(() -> {
            try {
                if (routeInfo == null || routeInfo.length() < 2) {
                    throw new JSONException("Invalid route data");
                }

                clearRoute();

                JSONArray steps = routeInfo.getJSONArray(0);
                String eta = routeInfo.optString(1, "N/A");
                int totalDistance = routeInfo.optInt(2, 0);

                etaText.setText(getString(R.string.eta_format, eta));

                updateStatsText(totalDistance);

                displayRouteSteps(steps);

                if (userMarker != null) {
                    float bearing = calculatePathBearing(userMarker.getPosition());
                    updateCameraPosition(userMarker.getPosition(), bearing);
                }

            } catch (Exception e) {
                Log.e("Navigation", "Route display error", e);
            }
        });
    }

    private String formatArrivalTime(String etaDuration) {
        try {
            if (etaDuration.equals("N/A")) {
                return "";
            }

            int minutes = Integer.parseInt(etaDuration.replaceAll("[^0-9]", ""));
            long arrivalMillis = System.currentTimeMillis() + (minutes * 60 * 1000L);

            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
            return sdf.format(new Date(arrivalMillis));

        } catch (Exception e) {
            Log.e("Navigation", "Error formatting arrival time", e);
            return etaDuration;
        }
    }

    private void updateStatsText(int distanceMeters) {
        String etaDuration = etaText.getText().toString().replaceAll("[^0-9]", "");
        String arrivalTime = formatArrivalTime(etaDuration);

        String distanceText;
        if (distanceMeters >= 1000) {
            distanceText = String.format("%.1f km", distanceMeters / 1000.0);
        } else {
            distanceText = String.format("%d m", distanceMeters);
        }

        String stats = String.format("%s • %s", distanceText, arrivalTime);
        statsText.setText(stats);
    }


    private void displayRouteSteps(JSONArray steps) {
        curMap.clearAllPolylines();

        try {
            List<LatLng> allPoints = new ArrayList<>();
            for (int i = 0; i < steps.length(); i++) {
                JSONObject polyline = steps.getJSONObject(i).getJSONObject("polyline");
                allPoints.addAll(PolyUtil.decode(polyline.getString("encodedPolyline")));
            }

            if (!allPoints.isEmpty()) {
                PolylineOptions options = new PolylineOptions()
                        .addAll(allPoints)
                        .width(20)
                        .color(Color.parseColor("#4285F4"))
                        .geodesic(true);

                curMap.addPolyline(options);
                zoomToRouteSmoothly(allPoints);
            }
        } catch (JSONException e) {
            Log.e("RouteDisplay", "Error parsing route", e);
        }
    }

    private float calculateRemainingDistance(LatLng currentPosition) {
        NavigationGoogleMaps navMap = (NavigationGoogleMaps) curMap;
        List<LatLng> pathPoints = navMap.getFirstPolylinePoints();
        if (pathPoints.isEmpty()) return 0;

        float totalDistance = 0;
        boolean passedCurrentPos = false;

        for (int i = 0; i < pathPoints.size() - 1; i++) {
            LatLng start = pathPoints.get(i);
            LatLng end = pathPoints.get(i + 1);

            if (!passedCurrentPos) {
                if (SphericalUtil.computeDistanceBetween(currentPosition, end) <
                        SphericalUtil.computeDistanceBetween(currentPosition, start)) {
                    passedCurrentPos = true;
                }
            }

            if (passedCurrentPos) {
                totalDistance += SphericalUtil.computeDistanceBetween(start, end);
            }
        }

        return totalDistance;
    }

    private float calculatePathBearing(LatLng currentPosition) {
        NavigationGoogleMaps navMap = (NavigationGoogleMaps) curMap;
        List<LatLng> pathPoints = navMap.getFirstPolylinePoints();
        if (pathPoints.size() < 2) {
            return 0;
        }

        float minDistance = Float.MAX_VALUE;
        LatLng closestPoint = null;
        LatLng nextPoint = null;

        for (int i = 0; i < pathPoints.size() - 1; i++) {
            LatLng start = pathPoints.get(i);
            LatLng end = pathPoints.get(i + 1);

            double distance = SphericalUtil.computeDistanceBetween(currentPosition, start);
            if (distance < minDistance) {
                minDistance = (float) distance;
                closestPoint = start;
                nextPoint = end;
            }
        }

        if (closestPoint == null || nextPoint == null) {
            return 0;
        }

        return (float) SphericalUtil.computeHeading(closestPoint, nextPoint);
    }

    private void zoomToRouteSmoothly(List<LatLng> points) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : points) {
            builder.include(point);
        }


        curMap.moveCameraToBounds(builder);
    }

    private void clearRoute() {
        curMap.clearAllPolylines();
    }


    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }
    }

    private void startLocationUpdates() {
        try {
            LocationRequest locationRequest = new LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY, 1000).setMinUpdateIntervalMillis(500).build();

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            }
        } catch (Exception e) {
            Log.e("Navigation", "Location updates error", e);
        }
    }

    private void stopLocationUpdates() {
        try {
            locationClient.removeLocationUpdates(locationCallback);
        } catch (Exception e) {
            Log.e("Navigation", "Stop updates error", e);
        }
    }

    private void startNavigation() {
        if (origin == null || destination == null) {
            Toast.makeText(this, "Please set both origin and destination", Toast.LENGTH_SHORT).show();
            return;
        }

        fetchAndDisplayRoute(origin.toGoogleMapsLatLng(), destination.toGoogleMapsLatLng());
        isNavigationActive = true;
    }

    private void stopNavigation() {
        isNavigationActive = false;
        lastRouteUpdatePosition = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopNavigation();
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();

            if (origin != null && destination != null) {
                startNavigation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Location permission required for navigation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showMainMenuDialog() {
        MainMenuDialog dialog = new MainMenuDialog(this);
        dialog.show();
    }

    public void exitIntent(){
        stopNavigation();
        stopLocationUpdates();

        Intent i = new Intent();
        i.putExtra("OPEN_DIR", true);
        setResult(RESULT_OK, i);
        finish();
    }


    @Override
    public void onEstimatedTimeUpdated(String newTime) {

    }

    @Override
    public void onMapError(String errorString) {

    }

    @Override
    public void onMapClicked(MapCoordinates coordinates) {

    }
}