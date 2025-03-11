package minicap.concordia.campusnav.screens;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import minicap.concordia.campusnav.BuildConfig;
import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.buildingshape.CampusBuildingShapes;
import minicap.concordia.campusnav.databinding.ActivityMapsBinding;
import minicap.concordia.campusnav.helpers.CoordinateResHelper;
import minicap.concordia.campusnav.map.FetchPathTask;
import minicap.concordia.campusnav.map.InternalGoogleMaps;
// Added from branch: use the BuildingSelectorFragment from your branch package.
import minicap.concordia.ca.BuildingSelectorFragment;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, FetchPathTask.OnRouteFetchedListener {

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

    private EditText yourLocationEditText;
    private FusedLocationProviderClient fusedLocationClient;
    private String travelMode = "DRIVE";
    private Spinner buildingSpinner;
    private LatLng destination;

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

        // Fill buildings list based on campus
        CampusName campusName;
        if (!showSGW) {
            campusName = CampusName.SGW;
        } else {
            campusName = CampusName.LOYOLA;
        }
        populateBuildingsList(campusName);

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

        ImageButton walkButton = findViewById(R.id.walkButton);
        ImageButton wheelchairButton = findViewById(R.id.wheelchairButton);
        ImageButton carButton = findViewById(R.id.carButton);
        ImageButton transitButton = findViewById(R.id.transitButton);

        // Default mode
        carButton.setSelected(true);

        walkButton.setOnClickListener(v -> {
            walkButton.setSelected(true);
            wheelchairButton.setSelected(false);
            carButton.setSelected(false);
            transitButton.setSelected(false);
            travelMode = "WALK";
            getUserLocationPath();
        });

        wheelchairButton.setOnClickListener(v -> {
            wheelchairButton.setSelected(true);
            walkButton.setSelected(false);
            carButton.setSelected(false);
            transitButton.setSelected(false);
            // TODO: Google Maps does not support wheelchair mode, so using WALK for now.
            travelMode = "WALK";
            getUserLocationPath();
        });

        carButton.setOnClickListener(v -> {
            carButton.setSelected(true);
            walkButton.setSelected(false);
            wheelchairButton.setSelected(false);
            transitButton.setSelected(false);
            travelMode = "DRIVE";
            getUserLocationPath();
        });

        transitButton.setOnClickListener(v -> {
            transitButton.setSelected(true);
            walkButton.setSelected(false);
            wheelchairButton.setSelected(false);
            carButton.setSelected(false);
            travelMode = "TRANSIT";
            getUserLocationPath();
        });

        yourLocationEditText = findViewById(R.id.yourLocationEditText);
    }

    private void toggleCampus() {
        // Flip the state
        showSGW = !showSGW;
        LatLng campus = showSGW ? LOY_LOCATION : SGW_LOCATION;
        campusMarker = gMapController.updateCampusMarker(campusMarker, campus, showSGW);
        float defaultZoom = CoordinateResHelper.getFloat(this, R.dimen.default_map_zoom);
        gMapController.animateCameraToLocation(campus, defaultZoom);
        campusTextView.setText(showSGW ? "SGW" : "LOY");

        CampusName campusName;
        if (!showSGW) {
            campusName = CampusName.SGW;
        } else {
            campusName = CampusName.LOYOLA;
        }
        populateBuildingsList(campusName);
    }

    /**
     * Populates the list of buildings from the ConcordiaBuildingManager.
     *
     * @param campusName CampusName
     */
    private void populateBuildingsList(CampusName campusName) {
        ArrayList<Building> buildingsForCampus = ConcordiaBuildingManager.getInstance().getBuildingsForCampus(campusName);

        buildingSpinner = findViewById(R.id.building_spinner);
        if (buildingSpinner == null) {
            Log.e("Error", "Spinner is not initialized properly.");
        }

        ArrayAdapter<Building> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, buildingsForCampus);
        buildingSpinner.setAdapter(adapter);

        buildingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Building selectedBuilding = (Building) parentView.getItemAtPosition(position);
                if (selectedBuilding != null) {
                    destination = new LatLng(selectedBuilding.getLocation()[0], selectedBuilding.getLocation()[1]);
                    getUserLocationPath();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Callback for when Google Maps has loaded.
     *
     * @param googleMap The loaded Google Map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMapController = new InternalGoogleMaps(googleMap);
        gMapController.centerOnCoordinates(startingLat, startingLng);
        gMapController.addPolygons(CampusBuildingShapes.getSgwBuildingCoordinates());
        gMapController.addPolygons(CampusBuildingShapes.getLoyolaBuildingCoordinates());
        LatLng campusLocation = new LatLng(startingLat, startingLng);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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
     * Gets the user location and invokes drawPath.
     */
    private void getUserLocationPath() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            // TODO: Uncomment the following line to reflect current user location
                            // LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            String address = getAddressFromLocation(45.489682435037835, -73.58808030276997);
                            yourLocationEditText.setText(address);
                            LatLng userLocation = new LatLng(45.489682435037835, -73.58808030276997);
                            drawPath(userLocation, destination);
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Gets the address as a String given coordinates.
     *
     * @param latitude  double
     * @param longitude double
     * @return String
     */
    private String getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            } else {
                return "Address not found";
            }
        } catch (IOException e) {
            Log.e("getAddressFromLocation()", e.getMessage());
            return "Unable to fetch address";
        }
    }

    /**
     * This method generates the Google API URL and invokes FetchPathTask.
     *
     * @param origin      LatLng
     * @param destination LatLng
     */
    private void drawPath(LatLng origin, LatLng destination) {
        gMapController.clearPolyLines();
        gMapController.addMarker(new MarkerOptions()
                .position(origin)
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        gMapController.addMarker(new MarkerOptions()
                .position(destination)
                .title("Destination"));
        gMapController.centerOnCoordinates(origin.latitude, origin.longitude);
        new FetchPathTask(this).fetchRoute(origin, destination, travelMode);
    }

    /**
     * Invoked when the route is fetched by the Google API. This method adds the route to the map.
     *
     * @param info JSONArray
     */
    @Override
    public void onRouteFetched(JSONArray info) {
        try {
            JSONArray steps = info.getJSONArray(0);
            TextView estimatedTime = findViewById(R.id.estimatedTime);
            String estimatedTimeValue = info.getString(1);
            estimatedTime.setText(getString(R.string.estimated_time, estimatedTimeValue));

            if (steps == null) {
                Toast.makeText(this, "Failed to fetch route", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < steps.length(); i++) {
                JSONObject step = steps.getJSONObject(i);
                String travelMode = step.getString("travelMode");
                JSONObject polylineObject = step.getJSONObject("polyline");
                String encodedPolyline = polylineObject.getString("encodedPolyline");
                List<LatLng> stepPoints = PolyUtil.decode(encodedPolyline);

                PolylineOptions polylineOptions = new PolylineOptions()
                        .addAll(stepPoints)
                        .width(10);

                if ("WALK".equals(travelMode)) {
                    polylineOptions.color(Color.BLUE)
                            .pattern(Arrays.asList(new Dot(), new Gap(10)));
                } else if ("TRANSIT".equals(travelMode)) {
                    JSONObject transitDetails = step.getJSONObject("transitDetails");
                    String vehicleType = transitDetails.getJSONObject("transitLine")
                            .getJSONObject("vehicle")
                            .getString("type");
                    String transitLineName = transitDetails.getJSONObject("transitLine").getString("name");

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
                                polylineOptions.color(Color.GRAY);
                                break;
                        }
                    }
                }
                gMapController.addPolyline(polylineOptions);
            }
        } catch (JSONException e) {
            Log.e("Route Parsing Error", e.toString());
        }
    }

    /**
     * Displays the BuildingSelectorFragment.
     * This method makes the fragment container visible (if hidden) and replaces its contents with a new BuildingSelectorFragment.
     */
    private void showBuildingSelectorFragment() {
        // Ensure that the container (with id buildingSelectorContainer) is visible.
        binding.buildingSelectorContainer.setVisibility(View.VISIBLE);
        // Begin a fragment transaction to display the BuildingSelectorFragment.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.buildingSelectorContainer, new BuildingSelectorFragment())
                .addToBackStack(null)
                .commit();
    }
}
