package minicap.concordia.campusnav.screens;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.map.FetchPathTask;
import minicap.concordia.campusnav.map.MapCoordinates;

public class NavigationActivity extends AppCompatActivity implements OnMapReadyCallback, FetchPathTask.OnRouteFetchedListener {

    private static final int LOCATION_REQUEST_CODE = 101;
    private static final float DEFAULT_ZOOM = 18f;
    private static final float ROUTE_ZOOM = 15f;

    private GoogleMap googleMap;
    private FusedLocationProviderClient locationClient;
    private LocationCallback locationCallback;

    private MapCoordinates origin;
    private MapCoordinates destination;
    private String travelMode;

    private Marker originMarker;
    private Marker destinationMarker;
    private List<Polyline> routePolylines = new ArrayList<>();
    private TextView etaText;
    private JSONArray routeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        initializeViews();
        getRouteDataFromIntent();
        setupLocationClient();
        initializeMap();
    }

    private void initializeViews() {
        etaText = findViewById(R.id.eta_text);
    }

    private void getRouteDataFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            origin = new MapCoordinates(extras.getDouble("origin_lat"), extras.getDouble("origin_lng"));
            destination = new MapCoordinates(extras.getDouble("destination_lat"), extras.getDouble("destination_lng"));
            travelMode = extras.getString("travel_mode", "WALK");

            try {
                String routeJson = extras.getString("route_data");
                if (routeJson != null) {
                    routeData = new JSONArray(routeJson);
                }
            } catch (JSONException e) {
                Log.e("Navigation", "Error parsing route data", e);
            }
        }
    }

    private void setupLocationClient() {
        locationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                for (Location location : locationResult.getLocations()) {
                    updateUserPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                }
            }
        };
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.navigation_map, mapFragment).commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        configureMap();
        setupMapMarkers();
        if (routeData != null) {
            onRouteFetched(routeData);
        } else {
            fetchAndDisplayRoute();
        }
        fetchAndDisplayRoute();
        checkLocationPermissions();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
    }

    private void configureMap() {
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setTrafficEnabled(true);

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.nav_map_style));
            if (!success) {
                Log.e("NavigationActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("NavigationActivity", "Can't find style.", e);
        }
    }

    private void setupMapMarkers() {
        if (originMarker != null) originMarker.remove();
        if (destinationMarker != null) destinationMarker.remove();

        originMarker = googleMap.addMarker(new MarkerOptions().position(origin.toGoogleMapsLatLng()).title("Start").icon(com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker(com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_BLUE)));
        destinationMarker = googleMap.addMarker(new MarkerOptions().position(destination.toGoogleMapsLatLng()).title("Destination").icon(com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker(com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED)));
    }

    private void fetchAndDisplayRoute() {
        new FetchPathTask(this).fetchRoute(origin.toGoogleMapsLatLng(), destination.toGoogleMapsLatLng(), travelMode);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin.toGoogleMapsLatLng(), ROUTE_ZOOM));
    }

    private void updateUserPosition(LatLng position) {
        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, DEFAULT_ZOOM));

            if (originMarker != null) {
                originMarker.setPosition(position);
            }
        }
    }

    @Override
    public void onRouteFetched(JSONArray routeInfo) {
        if (routeInfo == null) {
            Toast.makeText(this, "Failed to fetch route", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            for (Polyline polyline : routePolylines) {
                polyline.remove();
            }
            routePolylines.clear();

            JSONArray steps = routeInfo.getJSONArray(0);
            String eta = routeInfo.getString(1);

            etaText.setText(getString(R.string.eta_format, eta));

            displayRouteSteps(steps);

        } catch (JSONException e) {
            Toast.makeText(this, "Error parsing route data", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayRouteSteps(JSONArray steps) {
        clearRoute();

        if (steps == null) {
            Log.e("RouteDisplay", "Steps array is null");
            return;
        }

        List<LatLng> allPoints = new ArrayList<>();

        for (int i = 0; i < steps.length(); i++) {
            try {
                JSONObject step = steps.getJSONObject(i);
                JSONObject polyline = step.getJSONObject("polyline");
                String encodedPath = polyline.getString("encodedPolyline");
                allPoints.addAll(PolyUtil.decode(encodedPath));
            } catch (JSONException e) {
                Log.e("RouteDisplay", "Error parsing step " + i, e);
            }
        }

        if (!allPoints.isEmpty()) {
            PolylineOptions options = new PolylineOptions().addAll(allPoints).width(20).color(Color.parseColor("#4285F4")).geodesic(true).zIndex(10);
            routePolylines.add(googleMap.addPolyline(options));
            zoomToRoute();
        }
    }

    private void clearRoute() {
        for (Polyline polyline : routePolylines) {
            polyline.remove();
        }
        routePolylines.clear();
    }

    private void zoomToRoute() {
        if (routePolylines.isEmpty()) return;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Polyline polyline : routePolylines) {
            for (LatLng point : polyline.getPoints()) {
                builder.include(point);
            }
        }

        try {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                    builder.build(), 100));
        } catch (IllegalStateException e) {
            googleMap.setOnMapLoadedCallback(() ->
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                            builder.build(), 100)));
        }
    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).setMinUpdateIntervalMillis(500).build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        }
    }

    private void stopLocationUpdates() {
        locationClient.removeLocationUpdates(locationCallback);
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
}