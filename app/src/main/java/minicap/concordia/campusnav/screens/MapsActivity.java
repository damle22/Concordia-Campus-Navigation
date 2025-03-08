package minicap.concordia.campusnav.screens;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingshape.CampusBuildingShapes;
import minicap.concordia.campusnav.databinding.ActivityMapsBinding;
import minicap.concordia.campusnav.helpers.CoordinateResHelper;
import minicap.concordia.campusnav.map.InternalGoogleMaps;
// Note: Import BuildingSelectorFragment from its actual package:
import minicap.concordia.ca.BuildingSelectorFragment;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String KEY_STARTING_LAT = "starting_lat";
    public static final String KEY_STARTING_LNG = "starting_lng";
    public static final String KEY_CAMPUS_NOT_SELECTED = "campus_not_selected";
    private final LatLng SGW_LOCATION = new LatLng(45.49701, -73.57877);
    private final LatLng LOY_LOCATION = new LatLng(45.45863, -73.64188);
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private InternalGoogleMaps gMapController;
    private ActivityMapsBinding binding;

    private double startingLat;
    private double startingLng;
    private boolean showSGW;
    private Marker campusMarker;
    private Button campusSwitchBtn;
    private TextView campusTextView;
    private String campusNotSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            startingLat = bundle.getDouble(KEY_STARTING_LAT);
            startingLng = bundle.getDouble(KEY_STARTING_LNG);
            campusNotSelected = bundle.getString(KEY_CAMPUS_NOT_SELECTED);
            showSGW = bundle.getBoolean("SHOW_SGW");
        }

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        campusTextView = findViewById(R.id.ToCampus);
        campusTextView.setText(campusNotSelected);
        campusSwitchBtn = findViewById(R.id.campusSwitch);
        campusSwitchBtn.setOnClickListener(v -> toggleCampus());

        // Hook up the Buildings button to show the BuildingSelectorFragment.
        binding.buildingView.setOnClickListener(v -> showBuildingSelectorFragment());

        // Check location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            initializeMap();
        }
    }

    private void toggleCampus() {
        // Flip the state
        showSGW = !showSGW;
        LatLng campus = showSGW ? LOY_LOCATION : SGW_LOCATION;
        campusMarker = gMapController.updateCampusMarker(campusMarker, campus, showSGW);
        float defaultZoom = CoordinateResHelper.getFloat(this, R.dimen.default_map_zoom);
        gMapController.animateCameraToLocation(campus, defaultZoom);
        campusTextView.setText(showSGW ? "SGW" : "LOY");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeMap();
            } else {
                Toast.makeText(this, "Location permission is required to show your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Initializes Google Maps.
     */
    private void initializeMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Callback for when Google Maps has loaded.
     * @param googleMap The loaded Google Map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMapController = new InternalGoogleMaps(googleMap);
        gMapController.centerOnCoordinates(startingLat, startingLng);
        // Create building shapes
        gMapController.addPolygons(CampusBuildingShapes.getSgwBuildingCoordinates());
        gMapController.addPolygons(CampusBuildingShapes.getLoyolaBuildingCoordinates());
        enableMyLocation();
    }

    /**
     * Enables location tracking on the map.
     */
    private void enableMyLocation() {
        if (!gMapController.toggleLocationTracking(true)) {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Displays the BuildingSelectorFragment.
     * This method makes the fragment container visible (if hidden) and replaces its contents with BuildingSelectorFragment.
     */
    private void showBuildingSelectorFragment() {
        // Make sure the fragment container (with id buildingSelectorContainer) is visible.
        binding.buildingSelectorContainer.setVisibility(View.VISIBLE);
        // Replace the container with a new instance of BuildingSelectorFragment.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.buildingSelectorContainer, new BuildingSelectorFragment())
                .addToBackStack(null)
                .commit();
    }
}
