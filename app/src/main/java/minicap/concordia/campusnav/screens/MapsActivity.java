package minicap.concordia.campusnav.screens;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.TypedValue;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        TypedValue hallLat = new TypedValue();
        TypedValue hallLon = new TypedValue();

        getResources().getValue(R.dimen.sgw_hall_building_lat, hallLat, true);
        getResources().getValue(R.dimen.sgw_hall_building_lon, hallLon, true);

        float hallLatFloat = hallLat.getFloat();
        float hallLonFloat = hallLon.getFloat();

        LatLng concordia = new LatLng(hallLatFloat, hallLonFloat);
        mMap.addMarker(new MarkerOptions().position(concordia).title("Marker at Concordia"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(concordia, 18f));


    }
}