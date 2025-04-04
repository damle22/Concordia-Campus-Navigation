package minicap.concordia.campusnav.map;

import android.content.Context;
import androidx.fragment.app.Fragment;


import com.mappedin.sdk.MPIMapView;
import com.mappedin.sdk.models.MPIMap;
import com.mappedin.sdk.web.MPIOptions;

import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import minicap.concordia.campusnav.components.MappedInWebViewFragment;
import minicap.concordia.campusnav.map.enums.MapColors;
import minicap.concordia.campusnav.map.helpers.MapColorConversionHelper;

public class InternalMappedIn extends AbstractMap {

    private MappedInWebViewFragment mappedInFragment;

    private MPIMapView curMap;

    public InternalMappedIn(MapUpdateListener listener) {
        super(listener);
    }

    @Override
    public Fragment initialize() {
        mappedInFragment = MappedInWebViewFragment.newInstance();

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

        String markerHTML = MapColorConversionHelper.getMappedInMarkerHTML(color, title);

        MPIMap.MPICoordinate coord = position.toMappedInCoordinate(curMap.getCurrentMap());
        curMap.getMarkerManager().addByCoordinate(coord,
                markerHTML,
                new MPIOptions.Marker(),
                null);
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
        curMap.getMarkerManager().removeAll();
    }

    @Override
    public void clearPathFromMap() {

    }

    @Override
    public void displayRoute(MapCoordinates origin, MapCoordinates destination, String travelMode) {

    }

    @Override
    public void displayPOI(MapCoordinates origin, POIType type) {

    }

    @Override
    public void centerOnCoordinates(MapCoordinates newCameraPosition) {

    }

    @Override
    public void switchToFloor(String floorName) {

    }

    @Override
    public boolean toggleLocationTracking(boolean isEnabled) {
        if(isEnabled) {
            curMap.getBlueDotManager().enable(new MPIOptions.BlueDot());
        }
        else {
            curMap.getBlueDotManager().disable();
        }
        return true;
    }

    @Override
    public void setStyle(Context context, int resourceID) {
        //Not used
    }
}
