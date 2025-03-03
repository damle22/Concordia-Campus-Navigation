package minicap.concordia.campusnav.buildingmanager.entities.poi;

import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;

public class OutdoorPOI extends POI{

    public OutdoorPOI(String name, POIType type, boolean isAccessibilityFeature, float latitude, float longitude) {
        super(name, type, isAccessibilityFeature, latitude, longitude);
    }
}
