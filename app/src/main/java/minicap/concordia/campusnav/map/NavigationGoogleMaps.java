package minicap.concordia.campusnav.map;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

public class NavigationGoogleMaps extends InternalGoogleMaps{

    private GoogleMap mMap;

    public NavigationGoogleMaps(MapUpdateListener listener) {
        super(listener);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        super.setMap(googleMap);
        this.mMap = googleMap;

        //Set certain settings
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        listener.onMapReady();
    }

    public void moveCameraToBounds(@NonNull LatLngBounds.Builder builder){

        mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(builder.build(), 100),
                500,
                null
        );

    }

    public void moveCameraToPosition(CameraPosition cameraPosition, int padding){
        mMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition),
                200,
                null)
        ;

        mMap.setPadding(0, padding, 0, 0);
    }

    public void zoomCamera(@NonNull MapCoordinates center, float zoom){
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(center.toGoogleMapsLatLng(), zoom)
        );
    }

    @Override
    public void updateMarkerPosition(Marker marker, LatLng position) {
        if (marker != null) {
            marker.setPosition(position);
        }
    }


}
