package minicap.concordia.campusnav.screens;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import minicap.concordia.campusnav.BuildConfig;
import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingshape.CampusBuildingShapes;
import minicap.concordia.campusnav.databinding.ActivityMapsBinding;
import minicap.concordia.campusnav.helpers.CoordinateResHelper;
import minicap.concordia.campusnav.map.FetchPathTask;
import minicap.concordia.campusnav.map.InternalGoogleMaps;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, FetchPathTask.OnRouteFetchedListener{

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

    private FusedLocationProviderClient fusedLocationClient;

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

        LinearLayout bottomSheet = findViewById(R.id.bottom_sheet);
        BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        int peekHeightPx = (int) (32 * getResources().getDisplayMetrics().density);
        bottomSheetBehavior.setPeekHeight(peekHeightPx); // Set peek height
        bottomSheetBehavior.setHideable(false); // Prevent complete hiding
        bottomSheetBehavior.setFitToContents(true); // Lock to collapsed or expanded state
        bottomSheetBehavior.setHalfExpandedRatio(0.01f); // Disable half-expanded state
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        campusTextView = findViewById(R.id.ToCampus);
        campusTextView.setText(campusNotSelected);
        campusSwitchBtn = findViewById(R.id.campusSwitch);

        campusSwitchBtn.setOnClickListener(v -> toggleCampus());

        // check location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // request perm
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // start map
            initializeMap();
        }

        ImageButton walkButton = findViewById(R.id.walkButton);
        ImageButton wheelchairButton = findViewById(R.id.wheelchairButton);
        ImageButton carButton = findViewById(R.id.carButton);
        ImageButton transitButton = findViewById(R.id.transitButton);

        walkButton.setOnClickListener(v -> {
            toggleButtonState(walkButton);
            wheelchairButton.setSelected(false);
            carButton.setSelected(false);
            transitButton.setSelected(false);
        });

        wheelchairButton.setOnClickListener(v -> {
            toggleButtonState(wheelchairButton);
            walkButton.setSelected(false);
            carButton.setSelected(false);
            transitButton.setSelected(false);
        });

        carButton.setOnClickListener(v -> {
            toggleButtonState(carButton);
            walkButton.setSelected(false);
            wheelchairButton.setSelected(false);
            transitButton.setSelected(false);
        });

        transitButton.setOnClickListener(v -> {
            toggleButtonState(transitButton);
            walkButton.setSelected(false);
            wheelchairButton.setSelected(false);
            carButton.setSelected(false);
        });
    }

    private void toggleButtonState(ImageButton button) {
        button.setSelected(!button.isSelected());
    }

    private void toggleCampus(){
        //flipping the state
        showSGW = !showSGW;

        // getting the new campus location
        LatLng campus =  showSGW ? LOY_LOCATION : SGW_LOCATION;

        //moving the existing marker to the new campus location
        campusMarker = gMapController.updateCampusMarker(campusMarker, campus, showSGW);

        //moving the camera smoothly to the new campus location
        float defaultZoom = CoordinateResHelper.getFloat(this, R.dimen.default_map_zoom);
        gMapController.animateCameraToLocation(campus, defaultZoom);

        //updating the button text
        campusTextView.setText(showSGW ? "SGW" : "LOY");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // start map
                initializeMap();
            } else {
                // error if no perm
                Toast.makeText(this, "Location permission is required to show your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Initializes google maps
     */
    private void initializeMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Callback for when google maps has loaded
     * @param googleMap The loaded google map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMapController = new InternalGoogleMaps(googleMap);

        gMapController.centerOnCoordinates(startingLat, startingLng);

        // create building shapes
        gMapController.addPolygons(CampusBuildingShapes.getSgwBuildingCoordinates());
        gMapController.addPolygons(CampusBuildingShapes.getLoyolaBuildingCoordinates());

        LatLng campusLocation = new LatLng(startingLat, startingLng);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //TODO This call routes the building to the User Location. It should be removed later and made OnClick when building is selected
        getUserLocation();

        // track location layer
        enableMyLocation();
    }

    /**
     * Enables location tracking on the map
     */
    private void enableMyLocation() {
        if (!gMapController.toggleLocationTracking(true)) {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Gets the User Location and invokes drawPath
     */
    private void getUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            //TODO userLocation is currently hardcoded for VM purposes. Uncomment this line to reflect current user location
                            //LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            LatLng userLocation= new LatLng(45.489682435037835, -73.58808030276997);
                            //TODO Destination Temporarily hardcoded until UI allows to choose building
                            drawPath(userLocation, SGW_LOCATION);
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * This Method generates the GoogleAPI URL and invokes FetchPathTask
     * @param origin LatLng
     * @param destination LatLng
     */
    private void drawPath(LatLng origin, LatLng destination) {
        new FetchPathTask(this).fetchRoute(origin, destination);
    }

    /**
     * Will add the route to the Map
     * Invoked when the route is fetched by the Google API
     * @param route List<LatLng>
     */
    @Override
    public void onRouteFetched(List<LatLng> route) {
        //Handles Route not fetched
        if (route == null || route.isEmpty()) {
            Toast.makeText(this, "Failed to fetch route", Toast.LENGTH_SHORT).show();
            return;
        }

        // Draw polyline on the map
        gMapController.addPolyline(new PolylineOptions()
                .addAll(route)
                .width(10)
                .color(Color.BLUE));
    }

}
