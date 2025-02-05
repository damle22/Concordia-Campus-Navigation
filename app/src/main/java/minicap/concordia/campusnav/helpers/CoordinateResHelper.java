package minicap.concordia.campusnav.helpers;

import android.content.Context;
import android.util.TypedValue;

import com.google.android.gms.maps.model.LatLng;
import minicap.concordia.campusnav.R;

public class CoordinateResHelper {

    private static final int LAT_INDEX = 0;
    private static final int LNG_INDEX = 1;

    public static class BuildingNames {
        public static int[] SGWHallBuilding = new int[] { R.dimen.sgw_hall_building_lat, R.dimen.sgw_hall_building_lng };
        public static int[] LoyHUBuilding = new int[] { R.dimen.loy_hu_building_lat, R.dimen.loy_hu_building_lng };
    }

    public static LatLng getCoordsForGoogleMaps(Context context, int[] ids) throws IllegalArgumentException {
        if(ids.length != 2) {
            throw new IllegalArgumentException("invalid ids for Lat and Lng");
        }

        TypedValue lat = new TypedValue();
        TypedValue lng = new TypedValue();

        context.getResources().getValue(ids[LAT_INDEX], lat, true);
        context.getResources().getValue(ids[LNG_INDEX], lng, true);

        float latFloat = lat.getFloat();
        float lngFloat = lng.getFloat();

        return new LatLng(latFloat, lngFloat);
    }

    public static float getFloat(Context context, int id) {
        TypedValue val = new TypedValue();

        context.getResources().getValue(id, val, true);

        return val.getFloat();
    }
}