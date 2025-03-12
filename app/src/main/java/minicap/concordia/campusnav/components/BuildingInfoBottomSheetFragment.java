package minicap.concordia.campusnav.components;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;
import java.util.ResourceBundle;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.entities.Building;

public class BuildingInfoBottomSheetFragment extends BottomSheetDialogFragment {

    private static final String ARG_BUILDING_IDENTIFIER = "building_identifier";
    private static final String TAG = "BuildingInfoBottomSheet";

    private TextView buildingNameText, buildingAddress, buildingDetails;
    private ImageView buildingImage;
    private ImageButton directionsButton;

    /**
     * Factory method to create a new instance of the Bottom Sheet with a given building identifier.
     */
    public static BuildingInfoBottomSheetFragment newInstance(String buildingIdentifier) {
        BuildingInfoBottomSheetFragment fragment = new BuildingInfoBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BUILDING_IDENTIFIER, buildingIdentifier);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.building_information_bottomsheet, container, false);

        initializeViews(view);
        loadBuildingInfo();

        return view;
    }

    /**
     * Initializes UI elements.
     */
    private void initializeViews(View view) {
        buildingNameText = view.findViewById(R.id.building_name);
        buildingAddress = view.findViewById(R.id.building_address);
        buildingDetails = view.findViewById(R.id.building_details);
        buildingImage = view.findViewById(R.id.building_image);
        directionsButton = view.findViewById(R.id.btn_directions);
    }

    /**
     * Loads building information using the identifier from arguments.
     */
    private void loadBuildingInfo() {
        if (getArguments() == null) {
            Log.e(TAG, "No arguments provided.");
            return;
        }

        String buildingIdentifier = getArguments().getString(ARG_BUILDING_IDENTIFIER);
        if (buildingIdentifier == null || buildingIdentifier.isEmpty()) {
            Log.e(TAG, "Invalid building identifier provided.");
            return;
        }

        Log.d(TAG, "Fetching Building Info for: " + buildingIdentifier);

        try {
            ResourceBundle bundle = ResourceBundle.getBundle(
                    "minicap.concordia.campusnav.buildingmanager.resources.BuildingResource_en_CA",
                    Locale.CANADA
            );

            Object obj = bundle.getObject(buildingIdentifier);
            if (obj instanceof Building) {
                populateBuildingData((Building) obj);
            } else {
                Log.e(TAG, "Building not found in resource bundle!");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading building from bundle", e);
        }
    }

    /**
     * Populates UI elements with building data.
     */
    private void populateBuildingData(Building building) {
        buildingNameText.setText(building.getBuildingName());
        buildingAddress.setText(building.getBuildingAddress());
        buildingDetails.setText(building.getDescription());

        if (building.getBuildingImageRes() != 0) {
            buildingImage.setImageResource(building.getBuildingImageRes());
        }

        // Set click listener for the Directions button
        directionsButton.setOnClickListener(v -> showLocationPopup(building));
    }

    /**
     * Displays a popup showing the building's latitude and longitude.
     */
    private void showLocationPopup(Building building) {
        float[] location = building.getLocation();
        if (location == null || location.length < 2) {
            Log.e(TAG, "Invalid location data for building.");
            return;
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Building Location")
                .setMessage("Latitude: " + location[0] + "\nLongitude: " + location[1])
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
