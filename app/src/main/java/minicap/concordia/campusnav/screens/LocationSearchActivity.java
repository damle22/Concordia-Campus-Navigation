package minicap.concordia.campusnav.screens;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;


import minicap.concordia.campusnav.R;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;

import minicap.concordia.campusnav.helpers.LocationSearchHelper;

public class LocationSearchActivity extends AppCompatActivity {
    private EditText searchInput;
    private Button searchButton;
    private RecyclerView resultsRecyclerView;
    private LocationAdapter adapter;
    private List<String> locationResults = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);

        searchInput = findViewById(R.id.search_input);
        searchButton = findViewById(R.id.search_button);
        resultsRecyclerView = findViewById(R.id.results_recycler_view);

        adapter = new LocationAdapter(locationResults);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        resultsRecyclerView.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
    }

    private void performSearch() {
        String query = searchInput.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(this, "Enter a location", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                List<String> results = LocationSearchHelper.searchLocation(query);
                runOnUiThread(() -> {
                    locationResults.clear();
                    locationResults.addAll(results);
                    adapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error fetching results", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}


class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private final List<String> locations;

    public LocationAdapter(List<String> locations) {
        this.locations = locations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.locationText.setText(locations.get(position));
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView locationText;

        public ViewHolder(View itemView) {
            super(itemView);
            locationText = itemView.findViewById(android.R.id.text1);
        }
    }
}
