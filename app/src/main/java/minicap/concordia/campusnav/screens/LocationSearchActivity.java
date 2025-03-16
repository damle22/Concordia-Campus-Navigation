package minicap.concordia.campusnav.screens;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;


import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.map.MapCoordinates;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class LocationSearchActivity extends AppCompatActivity {

    public static final String KEY_PREVIOUS_INPUT_STRING = "previous_input";
    public static final String KEY_IS_STARTING_LOCATION = "is_starting_location";

    public static final String KEY_RETURN_CHOSEN_LOCATION = "return_chosen_location";
    public static final String KEY_RETURN_BOOL_CURRENT_LOCATION = "return_current_location";
    public static final String KEY_RETURN_BOOL_IS_DESTINATION = "return_is_destination";

    public static final String KEY_RETURN_CHOSEN_LAT = "return_chosen_latitude";
    public static final String KEY_RETURN_CHOSEN_LNG = "return_chosen_longitude";
    private EditText searchInput;
    private RecyclerView resultsRecyclerView;
    private LocationAdapter adapter;

    private boolean isStartLocation = false;

    private List<Building> locations = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);

        String previousLocation = "";
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            previousLocation = bundle.getString(KEY_PREVIOUS_INPUT_STRING);
            isStartLocation = bundle.getBoolean(KEY_IS_STARTING_LOCATION);
        }

        searchInput = findViewById(R.id.locationSearch);

        if(isStartLocation) {
            Button currentLocationButton = findViewById(R.id.useCurrentLocationButton);
            currentLocationButton.setVisibility(VISIBLE);
            currentLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent returnData = new Intent();
                    returnData.putExtra(KEY_RETURN_CHOSEN_LOCATION, "");
                    returnData.putExtra(KEY_RETURN_BOOL_CURRENT_LOCATION, true);
                    returnData.putExtra(KEY_RETURN_BOOL_IS_DESTINATION, false);
                    returnData.putExtra(KEY_RETURN_CHOSEN_LAT, 0f);
                    returnData.putExtra(KEY_RETURN_CHOSEN_LNG, 0f);
                    setResult(RESULT_OK, returnData);
                    finish();
                }
            });

            searchInput.setHint(R.string.starting_point_search_hint);
        }
        else {
            //It would be pointless to set the current location as the destination
            Button currentLocationButton = findViewById(R.id.useCurrentLocationButton);
            currentLocationButton.setVisibility(GONE);

            searchInput.setHint(R.string.destination_search_hint);
        }

        if(previousLocation != null && !previousLocation.isEmpty() && !previousLocation.isBlank()) {
            searchInput.setText(previousLocation);
        }

        setDefaultLocationList();

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                // We use length here because the isEmpty() call requires min API 35 (we are currently 24)
                if(s.length() == 0) {
                    adapter.filter("");
                    return;
                }

                //may be unnecessary
                if(s.toString().contains("\n")) {
                    s.delete(s.length() - 3, s.length() - 1);
                }
                adapter.filter(s.toString());
            }
        });
    }


    private void setDefaultLocationList() {
        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();

        locations.addAll(manager.getBuildingsForCampus(CampusName.SGW));
        locations.addAll(manager.getBuildingsForCampus(CampusName.LOYOLA));

        resultsRecyclerView = findViewById(R.id.results_recycler_view);

        adapter = new LocationAdapter(locations);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        resultsRecyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new LocationAdapter.OnItemClickedListener() {
            @Override
            public void onClick(String buildingName, MapCoordinates coordinates) {
                runOnUiThread(() -> {
                    Intent returnData = new Intent();
                    returnData.putExtra(KEY_RETURN_CHOSEN_LOCATION, buildingName);
                    returnData.putExtra(KEY_RETURN_BOOL_CURRENT_LOCATION, false);
                    returnData.putExtra(KEY_RETURN_BOOL_IS_DESTINATION, !isStartLocation);
                    returnData.putExtra(KEY_RETURN_CHOSEN_LAT, coordinates.getLat());
                    returnData.putExtra(KEY_RETURN_CHOSEN_LNG, coordinates.getLng());
                    setResult(RESULT_OK, returnData);
                    finish();
                });
            }
        });
    }
}


class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private final List<Building> locations = new ArrayList<>();

    private List<Building> filteredLocations = new ArrayList<>();

    private OnItemClickedListener onClickListener;

    public LocationAdapter(List<Building> locations) {
        this.locations.addAll(locations);
        this.filteredLocations.addAll(locations);
    }

    public void setOnClickListener(OnItemClickedListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Building cur = filteredLocations.get(position);
        holder.locationText.setText(cur.getBuildingName());
        holder.locationText.setTextColor(Color.parseColor("#FFFFFF"));
        holder.itemView.setOnClickListener(v -> {
            if(onClickListener != null) {
                onClickListener.onClick(cur.getBuildingName(), cur.getLocation());
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredLocations.size();
    }

    public void filter(String filterText) {
        filteredLocations.clear();
        if(filterText.isBlank() || filterText.isEmpty()) {
            filteredLocations.addAll(locations);
        }
        else {
            String lowerFilterText = filterText.toLowerCase();
            for(Building building: locations) {
                if(building.getBuildingName().toLowerCase().contains(lowerFilterText)) {
                    filteredLocations.add(building);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView locationText;

        public ViewHolder(View itemView) {
            super(itemView);
            locationText = itemView.findViewById(android.R.id.text1);
        }
    }

    public interface OnItemClickedListener {
        void onClick(String buildingName, MapCoordinates coordinates);
    }
}
