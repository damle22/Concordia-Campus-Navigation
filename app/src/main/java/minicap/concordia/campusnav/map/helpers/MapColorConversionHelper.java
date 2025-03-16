package minicap.concordia.campusnav.map.helpers;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import minicap.concordia.campusnav.map.enums.MapColors;

public class MapColorConversionHelper {

    /**
     * Returns the float representing the desired color in google maps
     * @param color The color enum
     * @return float indicating the desired color
     */
    public static float getGoogleMapsColor(MapColors color) {
        switch(color){
            case BLUE:
                return BitmapDescriptorFactory.HUE_AZURE;
            default:
                return BitmapDescriptorFactory.HUE_RED;
        }
    }

    /**
     * Returns the html string for the MappedIn color desired
     * @param color The color enum
     * @return string html that contains color information
     */
    public static String getMappedInMarkerHTML(MapColors color, String title) {
        StringBuilder builder = new StringBuilder();
        builder.append("<div style=\"background-color:white; border: 2px solid ");
        switch(color){
            case BLUE:
                builder.append("blue");
                break;
            default:
                builder.append("red");
                break;
        }

        builder.append("; padding: 0.4rem; border-radius: 0.4rem;\">");
        builder.append(title);
        builder.append("</div>");

        return builder.toString();
    }
}
