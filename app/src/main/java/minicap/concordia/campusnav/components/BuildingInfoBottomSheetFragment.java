package minicap.concordia.campusnav.components;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    // Now takes a String identifier instead of BuildingName
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

        TextView buildingNameText = view.findViewById(R.id.building_name);
        TextView buildingAddress = view.findViewById(R.id.building_address);
        TextView buildingDetails = view.findViewById(R.id.building_details);
        ImageView buildingImage = view.findViewById(R.id.building_image);

        if (getArguments() != null) {
            String buildingIdentifier = getArguments().getString(ARG_BUILDING_IDENTIFIER);
            Log.d(TAG, "Fetching Building Info for: " + buildingIdentifier);

            if (buildingIdentifier != null && !buildingIdentifier.isEmpty()) {
                try {
                    ResourceBundle bundle = ResourceBundle.getBundle(
                            "minicap.concordia.campusnav.buildingmanager.resources.BuildingResource_en_CA",
                            Locale.CANADA
                    );

                    Object obj = bundle.getObject(buildingIdentifier);
                    if (obj instanceof Building) {
                        Building building = (Building) obj;

                        buildingNameText.setText(building.getBuildingName());
                        buildingAddress.setText(building.getBuildingAddress());
                        buildingDetails.setText(building.getDescription());

                        if (building.getBuildingImageRes() != 0) {
                            buildingImage.setImageResource(building.getBuildingImageRes());
                        }
                    } else {
                        Log.e(TAG, "Building not found in resource bundle!");
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Error loading building from bundle", e);
                }
            } else {
                Log.e(TAG, "Invalid building identifier provided.");
            }
        }

        return view;
    }
}
