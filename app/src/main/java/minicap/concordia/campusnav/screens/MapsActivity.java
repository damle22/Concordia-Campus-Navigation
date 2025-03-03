package minicap.concordia.campusnav.screens;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.databinding.ActivityMapsBinding;
import minicap.concordia.campusnav.helpers.CoordinateResHelper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String KEY_STARTING_LAT = "starting_lat";
    public static final String KEY_STARTING_LNG = "starting_lng";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private double startingLat;
    private double startingLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            startingLat = bundle.getDouble(KEY_STARTING_LAT);
            startingLng = bundle.getDouble(KEY_STARTING_LNG);
        }

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // check location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // start map
            initializeMap();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // start map
                initializeMap();
            } else {
                // error if no permission
                Toast.makeText(this, "Location permission is required to show your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initializeMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // NEW: Add the building selector overlay on top of the map.
        addBuildingSelectorOverlay();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng concordia = new LatLng(startingLat, startingLng);
        mMap.addMarker(new MarkerOptions().position(concordia).title("Marker at Concordia"));

        float defaultZoom = CoordinateResHelper.getFloat(this, R.dimen.default_map_zoom);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(concordia, defaultZoom));

        // Enable the "my location" layer
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true); // layer
            mMap.getUiSettings().setMyLocationButtonEnabled(true); // button
        } else {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    // NEW METHOD: Add the building selector overlay to the map's root view.
    private void addBuildingSelectorOverlay() {
        // Create a new FrameLayout to serve as the overlay container.
        FrameLayout overlay = new FrameLayout(this);
        overlay.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        // Assign an ID if needed (ensure this ID is defined in your resources)
        overlay.setId(R.id.buildingSelectorOverlay);

        // Add the overlay container to the root view.
        ((ViewGroup) binding.getRoot()).addView(overlay);

        // Inflate your fragment_building_selector.xml into the overlay.
        getLayoutInflater().inflate(R.layout.fragment_building_selector, overlay, true);
    }
}
