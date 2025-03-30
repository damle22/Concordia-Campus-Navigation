package minicap.concordia.campusnav.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class NavigationGoogleMaps extends InternalGoogleMaps{

    private GoogleMap mMap;
    private List<Polyline> routePolylines = new ArrayList<>();

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

    @Override
    public void addPolyline(PolylineOptions options) {
        Polyline polyline = mMap.addPolyline(options);
        routePolylines.add(polyline);
    }

    @Override
    public void clearAllPolylines() {
        for (Polyline polyline : routePolylines) {
            polyline.remove();
        }
        routePolylines.clear();
    }

    /**
     * Gets the points from the first polyline (if exists)
     * @return List of LatLng points or empty list if no polylines
     */
    public List<LatLng> getFirstPolylinePoints() {
        if (routePolylines.isEmpty() || routePolylines.get(0) == null) {
            return new ArrayList<>();
        }
        return routePolylines.get(0).getPoints();
    }

    @Override
    public Marker createUserMarker(MapCoordinates position, int iconResId, Context context) {
        BitmapDescriptor icon = bitmapDescriptorFromVector(context, iconResId);
        if (icon == null) {
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
        }

        return mMap.addMarker(new MarkerOptions()
                .position(position.toGoogleMapsLatLng())
                .title("Your Location")
                .icon(icon)
                .anchor(0.5f, 0.5f)
                .rotation(0)
                .flat(false));
    }

    @Override
    public void updateUserMarkerPosition(Marker marker, MapCoordinates position) {
        if (marker != null) {
            marker.setPosition(position.toGoogleMapsLatLng());
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorResId) {
        try {
            Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
            if (vectorDrawable == null) {
                return null;
            }

            int width = 100;
            int height = 100;

            vectorDrawable.setBounds(0, 0, width, height);
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.draw(canvas);
            return BitmapDescriptorFactory.fromBitmap(bitmap);
        } catch (Exception e) {
            Log.e("Navigation", "Vector icon error", e);
            return null;
        }
    }


}
