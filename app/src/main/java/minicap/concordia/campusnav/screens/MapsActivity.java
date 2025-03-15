package minicap.concordia.campusnav.screens;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import android.widget.EditText;


import com.google.android.material.bottomsheet.BottomSheetBehavior;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.map.AbstractMap;
import minicap.concordia.campusnav.map.InternalGoogleMaps;
import minicap.concordia.campusnav.map.InternalMappedIn;

public class MapsActivity extends FragmentActivity implements AbstractMap.MapUpdateListener {

    private final String MAPS_ACTIVITY_TAG = "MapsActivity";

    public static final String KEY_STARTING_LAT = "starting_lat";
    public static final String KEY_STARTING_LNG = "starting_lng";
    public static final String KEY_CAMPUS_NOT_SELECTED = "campus_not_selected";

    public static final String KEY_SHOW_SGW = "show_sgw";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private AbstractMap map;

    private ConcordiaBuildingManager buildingManager;

    private double startingLat;

    private double startingLng;

    private boolean isDestinationSet;

    private boolean showSGW;

    private boolean hasUserLocationBeenSet;

    private Button campusSwitchBtn;

    private TextView campusTextView;

    private String campusNotSelected;

    private EditText yourLocationEditText;

    private EditText destinationEditText;

    TextInputEditText searchText;

    private ImageButton walkButton;
    private ImageButton wheelchairButton;
    private ImageButton carButton;
    private ImageButton transitButton;

    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;

    private FusedLocationProviderClient fusedLocationClient;

    private String travelMode = "DRIVE";

    private double destinationLat;
    private double destinationLng;

    private double originLat;
    private double originLng;

    // We use this to launch and capture the results of the search location activity
    private ActivityResultLauncher<Intent> searchLocationLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        isDestinationSet = false;
        hasUserLocationBeenSet = false;
        buildingManager = ConcordiaBuildingManager.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            startingLat = bundle.getDouble(KEY_STARTING_LAT);
            startingLng = bundle.getDouble(KEY_STARTING_LNG);
            campusNotSelected = bundle.getString(KEY_CAMPUS_NOT_SELECTED);
            showSGW = bundle.getBoolean(KEY_SHOW_SGW);
        }

        LinearLayout bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

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

        walkButton = findViewById(R.id.walkButton);
        wheelchairButton = findViewById(R.id.wheelchairButton);
        carButton = findViewById(R.id.carButton);
        transitButton = findViewById(R.id.transitButton);

        //Default mode
        carButton.setSelected(true);

        walkButton.setOnClickListener(v -> changeSelectedTravelMethod(walkButton, "WALK"));

        //google maps does not support "wheelchair" for now, travelMode is the same as walking
        wheelchairButton.setOnClickListener(v -> changeSelectedTravelMethod(wheelchairButton, "WALK"));

        carButton.setOnClickListener(v -> changeSelectedTravelMethod(carButton, "DRIVE"));

        transitButton.setOnClickListener(v -> changeSelectedTravelMethod(transitButton, "TRANSIT"));

        yourLocationEditText = findViewById(R.id.yourLocationEditText);

        searchText = findViewById(R.id.genericSearchField);

        //Add main menu functionality to page
        View slidingMenu = findViewById(R.id.sliding_menu);
        ImageButton openMenuButton = findViewById(R.id.menuButton);
        ImageButton closeMenuButton = findViewById(R.id.closeMenu);
        ImageButton classScheduleRedirect = findViewById(R.id.classScheduleRedirect);
        ImageButton directionsRedirect = findViewById(R.id.directionsRedirect);
        ImageButton campusMapRedirect = findViewById(R.id.campusMapRedirect);
        MainMenuController menu = new MainMenuController(slidingMenu, openMenuButton, closeMenuButton, classScheduleRedirect, directionsRedirect, campusMapRedirect);


        searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    launchSearchActivity("", false);
                }
            }
        });

        yourLocationEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    launchSearchActivity(yourLocationEditText.getText().toString(), true);
                }
            }
        });

        destinationEditText = findViewById(R.id.destinationText);

        destinationEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    launchSearchActivity(destinationEditText.getText().toString(), false);
                }
            }
        });

        searchLocationLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        HandleSearchLocationResult(result);
                    }
                });

        getUserLocationPath();
    }

    /**
     * Handles the result of the SearchLocationActivity
     * @param result The result received from the activity
     */
    private void HandleSearchLocationResult(ActivityResult result) {
        runOnUiThread(() -> {
            searchText.clearFocus();
            destinationEditText.clearFocus();
            yourLocationEditText.clearFocus();
        });

        if(result.getResultCode() != LocationSearchActivity.RESULT_OK) {
            Log.d(MAPS_ACTIVITY_TAG, "Result code for search was not 'ok', discarding...");
            return;
        }

        Intent intent = result.getData();
        if(intent == null) {
            Log.e(MAPS_ACTIVITY_TAG, "Error while reading results from search, no intent returned");
            return;
        }
        Bundle returnData = intent.getExtras();
        if(returnData == null) {
            Log.e(MAPS_ACTIVITY_TAG, "Error while reading results from search, no data was returned");
            return;
        }

        boolean isDestination = returnData.getBoolean(LocationSearchActivity.KEY_RETURN_BOOL_IS_DESTINATION);
        String returnedLocation = returnData.getString(LocationSearchActivity.KEY_RETURN_CHOSEN_LOCATION);
        double lat = returnData.getDouble(LocationSearchActivity.KEY_RETURN_CHOSEN_LAT);
        double lng = returnData.getDouble(LocationSearchActivity.KEY_RETURN_CHOSEN_LNG);
        if(isDestination) {
            setDestination(returnedLocation, lat, lng);
        }
        else {
            boolean useCurrentLocation = returnData.getBoolean(LocationSearchActivity.KEY_RETURN_BOOL_CURRENT_LOCATION);
            setStartingPoint(useCurrentLocation, returnedLocation, lat, lng);
        }
    }

    /**
     * Launches the search activity and sets the extra parameters
     * @param previousInput The previous location that was selected if there was one
     * @param isStartingLocation Flag to indicate if this is a search for starting location or destination
     */
    private void launchSearchActivity(String previousInput, boolean isStartingLocation) {
        Intent i = new Intent(MapsActivity.this, LocationSearchActivity.class);
        i.putExtra(LocationSearchActivity.KEY_IS_STARTING_LOCATION, isStartingLocation);
        i.putExtra(LocationSearchActivity.KEY_PREVIOUS_INPUT_STRING, previousInput);

        searchLocationLauncher.launch(i);
    }

    /**
     * Sets the starting point used for navigation
     * @param useCurrentLocation Flag indicating whether to use the current location of the user
     * @param locationString The name of the location
     * @param lat The latitude of the location
     * @param lng The longitude of the location
     */
    private void setStartingPoint(boolean useCurrentLocation, String locationString, double lat, double lng) {
        Log.d(MAPS_ACTIVITY_TAG, "Set starting location to: " + locationString + " with coords: (" + lat + ", " + lng + "), is current location: " + useCurrentLocation);

        if(useCurrentLocation && !hasUserLocationBeenSet) {
            //getUserLocationPath will set the starting point and then call this again
            getUserLocationPath();
            return;
        }

        if(useCurrentLocation) {
            yourLocationEditText.setText(R.string.your_location);
        }
        else {
            //Reset this flag in case the user wants to use their own location in the future
            hasUserLocationBeenSet = false;
            originLat = lat;
            originLng = lng;
            yourLocationEditText.setText(locationString);
        }

        drawPath();
    }

    /**
     * Sets the destination used for navigation
     * @param locationString The name of the location
     * @param lat The location's latitude
     * @param lng The location's longitude
     */
    private void setDestination(String locationString, double lat, double lng) {
        Log.d(MAPS_ACTIVITY_TAG, "Set starting location to: " + locationString + " with coords: (" + lat + ", " + lng + ")");

        destinationLat = lat;
        destinationLng = lng;
        destinationEditText.setText(locationString);
        isDestinationSet = true;

        drawPath();
    }

    /**
     * Toggles the map to either the SGW campus or the Loyola campus based on which was already focused
     */
    private void toggleCampus(){
        //flipping the state
        showSGW = !showSGW;

        // getting the new campus location
        CampusName wantedCampus = showSGW ? CampusName.SGW : CampusName.LOYOLA;
        Campus curCampus = buildingManager.getCampus(wantedCampus);
        double[] campusCoords = curCampus.getLocation();

        //moving the existing marker to the new campus location
        map.addMarker(campusCoords[0], campusCoords[1], curCampus.getCampusName(), true);

        //moving the camera smoothly to the new campus location
        map.centerOnCoordinates(campusCoords[0], campusCoords[1]);

        //updating the button text
        campusTextView.setText(showSGW ? "SGW" : "LOY");
    }

    /**
     * Switches the desired travel method and re-draws the route line
     * @param selectedButton The travel method button that was clicked
     * @param newTravelMode The new travel mode to be used
     */
    private void changeSelectedTravelMethod(ImageButton selectedButton, String newTravelMode) {
        walkButton.setSelected(false);
        wheelchairButton.setSelected(false);
        carButton.setSelected(false);
        transitButton.setSelected(false);

        selectedButton.setSelected(true);
        travelMode = newTravelMode;

        drawPath();
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
     * Initializes the desired map (for now, google only)
     */
    private void initializeMap() {
        map = new InternalMappedIn(this);

//        map = new InternalGoogleMaps(this);
        Fragment mapFragment = map.initialize();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.map, mapFragment)
                .commit();
    }

    /**
     * Enables location tracking on the map
     */
    private void enableMyLocation() {
        if (!map.toggleLocationTracking(true)) {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Gets the User Location and invokes drawPath
     */
    private void getUserLocationPath() {
        if(fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            //If you are emulating and need to change your current location, use the emulator controls.
                            originLat = location.getLatitude();
                            originLng = location.getLongitude();
                            hasUserLocationBeenSet = true;
                            setStartingPoint(true, "", 0, 0);
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Gets the address as a String given coordinates
     * @param latitude double
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
                return address.getAddressLine(0);  // Return the first address line
            } else {
                return "Address not found";
            }
        } catch (IOException e) {
            Log.e("getAddressFromLocation()", e.getMessage());
            return "Unable to fetch address";
        }
    }

    /**
     * This handles the calls to the map to create the route
     */
    private void drawPath() {
        //In case someone changes their starting location before their destination
        if(!isDestinationSet) {
            return;
        }

        map.clearPathFromMap();
        map.clearAllMarkers();
        map.addMarker(originLat, originLng, "Starting location", BitmapDescriptorFactory.HUE_AZURE);

        map.addMarker(destinationLat, destinationLng, "Destination");

        map.centerOnCoordinates(originLat, originLng);

        map.displayRoute(originLat, originLng, destinationLat, destinationLng, travelMode);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onMapReady() {
        map.centerOnCoordinates(startingLat, startingLng);

        //By default, origin is user location
        enableMyLocation();
        getUserLocationPath();
    }

    @Override
    public void onEstimatedTimeUpdated(String newTime) {
        TextView estimatedTime = findViewById(R.id.estimatedTime);

        estimatedTime.setText(getString(R.string.estimated_time, newTime));
    }

    @Override
    public void onMapError(String errorString) {
        Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapClicked(double latitude, double longitude) {
        String address = getAddressFromLocation(latitude, longitude);
        map.addMarker(latitude, longitude, "Clicked location", true);
        setDestination(address, latitude, longitude);
    }

}



