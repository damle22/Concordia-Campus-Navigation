package minicap.concordia.campusnav.screens;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.databinding.ActivityMapsBinding;
import minicap.concordia.campusnav.map.InternalGoogleMaps;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String KEY_STARTING_LAT = "starting_lat";
    public static final String KEY_STARTING_LNG = "starting_lng";
    public static final String KEY_CAMPUS_NOT_SELECTED = "campus_not_selected";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private InternalGoogleMaps gMapController;

    private ActivityMapsBinding binding;

    private double startingLat;
    private double startingLng;

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
}
