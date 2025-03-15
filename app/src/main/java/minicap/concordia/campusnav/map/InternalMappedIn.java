package minicap.concordia.campusnav.map;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mappedin.sdk.MPIMapView;
import com.mappedin.sdk.listeners.MPIMapClickListener;
import com.mappedin.sdk.listeners.MPIMapViewListener;
import com.mappedin.sdk.models.MPIBlueDotPositionUpdate;
import com.mappedin.sdk.models.MPIBlueDotStateChange;
import com.mappedin.sdk.models.MPIData;
import com.mappedin.sdk.models.MPIMap;
import com.mappedin.sdk.models.MPIMapClickEvent;
import com.mappedin.sdk.models.MPINavigatable;
import com.mappedin.sdk.models.MPIState;
import com.mappedin.sdk.web.MPIOptions;

import java.util.Iterator;
import java.util.List;

import minicap.concordia.campusnav.components.MappedInFragment;

public class InternalMappedIn extends AbstractMap implements MPIMapViewListener, MPIMapClickListener {

    private List<String> markers;

    private MappedInFragment mappedInFragment;

    private MPIMapView curMap;

    public InternalMappedIn(MapUpdateListener listener) {
        super(listener);
    }

    @Override
    public Fragment initialize() {
        mappedInFragment = MappedInFragment.newInstance(this, this);

        return mappedInFragment;
    }

    @Override
    public void addMarker(double lat, double lng, String title, float color, boolean clearOtherMarkers) {
        if(clearOtherMarkers) {
            clearAllMarkers();
        }

        
    }

    @Override
    public void addMarker(double lat, double lng, String title, float color) {
        addMarker(lat, lng, title, color, false);
    }

    @Override
    public void addMarker(double lat, double lng, String title, boolean clearOtherMarkers) {
        addMarker(lat, lng, title, 0, clearOtherMarkers);
    }

    @Override
    public void addMarker(double lat, double lng, String title) {
        addMarker(lat, lng, title, 0, false);
    }

    @Override
    public void clearAllMarkers() {

    }

    @Override
    public void clearPathFromMap() {

    }

    @Override
    public void displayRoute(double originLat, double originLng, double destinationLat, double destinationLng, String travelMode) {

    }

    @Override
    public void centerOnCoordinates(double latitude, double longitude) {

    }

    @Override
    public void switchToFloor(String floorName) {

    }

    @Override
    public boolean toggleLocationTracking(boolean isEnabled) {
        return false;
    }

    @Override
    public void onBlueDotPositionUpdate(@NonNull MPIBlueDotPositionUpdate mpiBlueDotPositionUpdate) {

    }

    @Override
    public void onDataLoaded(@NonNull MPIData mpiData) {

    }

    @Override
    public void onMapChanged(@NonNull MPIMap mpiMap) {

    }

    @Override
    public void onPolygonClicked(@NonNull MPINavigatable.MPIPolygon mpiPolygon) {

    }

    @Override
    public void onNothingClicked() {

    }

    @Override
    public void onFirstMapLoaded() {
        Log.d("InternalMappedIn", "First map loaded");

        curMap = mappedInFragment.getMap();

        listener.onMapReady();
    }

    @Override
    public void onStateChanged(@NonNull MPIState mpiState) {

    }

    @Override
    public void onBlueDotStateChange(@NonNull MPIBlueDotStateChange mpiBlueDotStateChange) {

    }

    @Override
    public void onClick(@NonNull MPIMapClickEvent mpiMapClickEvent) {

    }
}
