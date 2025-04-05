package minicap.concordia.campusnav.map;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import minicap.concordia.campusnav.components.MappedInWebViewFragment;
import minicap.concordia.campusnav.map.enums.MapColors;

public class InternalMappedIn extends AbstractMap implements MappedInWebViewFragment.MappedInMapEventListener {

    private MappedInWebViewFragment mappedInFragment;

    public InternalMappedIn(MapUpdateListener listener) {
        super(listener);
        this.isIndoor = true;
    }

    @Override
    public Fragment initialize() {
        mappedInFragment = MappedInWebViewFragment.newInstance(this);

        return mappedInFragment;
    }

    @Override
    public void addMarker(OutdoorPOI opoi) {
        //TODO
    }

    @Override
    public void addMarker(MapCoordinates position, String title, MapColors color, boolean clearOtherMarkers) {
        if(clearOtherMarkers) {
            clearAllMarkers();
        }

        mappedInFragment.addMarker(position, title);
    }

    @Override
    public void addMarker(MapCoordinates position, String title, MapColors color) {
        addMarker(position, title, color, false);
    }

    @Override
    public void addMarker(MapCoordinates position, String title, boolean clearOtherMarkers) {
        addMarker(position, title, MapColors.DEFAULT, clearOtherMarkers);
    }

    @Override
    public void addMarker(MapCoordinates position, String title) {
        addMarker(position, title, MapColors.DEFAULT, false);
    }

    @Override
    public void clearAllMarkers() {
        mappedInFragment.removeAllMarkers();
    }

    @Override
    public void clearPathFromMap() {

    }

    @Override
    public void displayRoute(MapCoordinates origin, MapCoordinates destination, String travelMode) {
        boolean isAccessibility = travelMode.equals("WHEELCHAIR");

        mappedInFragment.drawPath(origin, destination, isAccessibility);
    }

    @Override
    public void displayPOI(MapCoordinates origin, POIType type) {

    }

    @Override
    public void centerOnCoordinates(MapCoordinates newCameraPosition) {

    }

    @Override
    public void loadBuilding(Building building, String initialFloor) {
        Log.d("eh", "eh");
    }

    @Override
    public boolean toggleLocationTracking(boolean isEnabled) {
        mappedInFragment.toggleLocationTracking(isEnabled);
        return true;
    }

    @Override
    public void setStyle(Context context, int resourceID) {
        //Not used
    }

    @Override
    public void mapLoaded() {
        listener.onMapReady();
    }

    @Override
    public void mapClicked(MapCoordinates coords) {
        listener.onMapClicked(coords);
    }
}
