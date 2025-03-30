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
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

public class NavigationGoogleMaps extends InternalGoogleMaps{

    private GoogleMap mMap;
    private List<Polyline> routePolylines = new ArrayList<>();

    //================ Starter methods ================

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

    //================ Camera methods ================
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
    public void zoomToRouteSmoothly(List<MapCoordinates> coordinates) {
        if (coordinates == null || coordinates.isEmpty()) {
            return;
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (MapCoordinates coordinate : coordinates) {
            builder.include(coordinate.toGoogleMapsLatLng());
        }

        mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(builder.build(), 100),
                500,
                null
        );
    }

    //================ Marker Methods ================

    @Override
    public void updateMarkerPosition(Marker marker, LatLng position) {
        if (marker != null) {
            marker.setPosition(position);
        }
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

    //================ Polyline Methods ================

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

    @Override
    public void addPolyline(List<MapCoordinates> coordinates, int width, int color, boolean geodesic) {
        List<LatLng> points = new ArrayList<>();
        for (MapCoordinates coord : coordinates) {
            points.add(coord.toGoogleMapsLatLng());
        }

        PolylineOptions options = new PolylineOptions()
                .addAll(points)
                .width(width)
                .color(color)
                .geodesic(geodesic);

        addPolyline(options);
    }

    @Override
    public List<MapCoordinates> decodePolyline(String encodedPolyline) {
        List<MapCoordinates> coordinates = new ArrayList<>();
        List<LatLng> points = PolyUtil.decode(encodedPolyline);
        for (LatLng point : points) {
            coordinates.add(MapCoordinates.fromGoogleMapsLatLng(point));
        }
        return coordinates;
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

    //================ Calculation Methods ================
    @Override
    public float calculateRemainingDistance(MapCoordinates currentPosition) {
        List<LatLng> pathPoints = getFirstPolylinePoints();
        if (pathPoints.isEmpty()) return 0;

        float totalDistance = 0;
        boolean passedCurrentPos = false;
        LatLng currentLatLng = currentPosition.toGoogleMapsLatLng();

        for (int i = 0; i < pathPoints.size() - 1; i++) {
            LatLng start = pathPoints.get(i);
            LatLng end = pathPoints.get(i + 1);

            if (!passedCurrentPos) {
                if (SphericalUtil.computeDistanceBetween(currentLatLng, end) <
                        SphericalUtil.computeDistanceBetween(currentLatLng, start)) {
                    passedCurrentPos = true;
                }
            }

            if (passedCurrentPos) {
                totalDistance += SphericalUtil.computeDistanceBetween(start, end);
            }
        }

        return totalDistance;
    }

    @Override
    public float calculatePathBearing(MapCoordinates currentPosition) {
        List<LatLng> pathPoints = getFirstPolylinePoints();
        if (pathPoints.size() < 2) {
            return 0;
        }

        float minDistance = Float.MAX_VALUE;
        LatLng closestPoint = null;
        LatLng nextPoint = null;
        LatLng currentLatLng = currentPosition.toGoogleMapsLatLng();

        for (int i = 0; i < pathPoints.size() - 1; i++) {
            LatLng start = pathPoints.get(i);
            LatLng end = pathPoints.get(i + 1);

            double distance = SphericalUtil.computeDistanceBetween(currentLatLng, start);
            if (distance < minDistance) {
                minDistance = (float) distance;
                closestPoint = start;
                nextPoint = end;
            }
        }

        if (closestPoint == null || nextPoint == null) {
            return 0;
        }

        return (float) SphericalUtil.computeHeading(closestPoint, nextPoint);
    }


    //================ Bitmap methods ================

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
