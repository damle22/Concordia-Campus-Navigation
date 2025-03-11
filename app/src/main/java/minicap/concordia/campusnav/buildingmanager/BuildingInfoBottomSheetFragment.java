package minicap.concordia.campusnav.buildingmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import minicap.concordia.campusnav.R;

public class BuildingInfoBottomSheetFragment extends BottomSheetDialogFragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_ADDRESS = "address";
    private static final String ARG_DETAILS = "details";
    private static final String ARG_IMAGE_RES = "imageRes";

    public static BuildingInfoBottomSheetFragment newInstance(String name, String address, String details, int imageRes) {
        BuildingInfoBottomSheetFragment fragment = new BuildingInfoBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_ADDRESS, address);
        args.putString(ARG_DETAILS, details);
        args.putInt(ARG_IMAGE_RES, imageRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.building_information_bottomsheet, container, false);

        TextView buildingName = view.findViewById(R.id.building_name);
        TextView buildingAddress = view.findViewById(R.id.building_address);
        TextView buildingDetails = view.findViewById(R.id.building_details);
        ImageView buildingImage = view.findViewById(R.id.building_image);

        if (getArguments() != null) {
            buildingName.setText(getArguments().getString(ARG_NAME));
            buildingAddress.setText(getArguments().getString(ARG_ADDRESS));
            buildingDetails.setText(getArguments().getString(ARG_DETAILS));
            buildingImage.setImageResource(getArguments().getInt(ARG_IMAGE_RES));
        }

        return view;
    }
}


