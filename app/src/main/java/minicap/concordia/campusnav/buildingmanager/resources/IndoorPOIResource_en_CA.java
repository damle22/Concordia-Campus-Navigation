package minicap.concordia.campusnav.buildingmanager.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListResourceBundle;

import minicap.concordia.campusnav.buildingmanager.entities.poi.IndoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import minicap.concordia.campusnav.map.MapCoordinates;

/**
 * Notes for maintainability:
 *  The naming convention follows: <buildingResourceName>_FloorX
 *  I recommend using the BuildingName's getResourceName for the buildingResourceName
 */
public class IndoorPOIResource_en_CA extends ListResourceBundle {
        public static final String FLOOR_TAG = "_Floor";

    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "1", new ArrayList<IndoorPOI>(Arrays.asList(
                        new IndoorPOI(new MapCoordinates(45.49724976, -73.57868389, "H196"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49704222, -73.57864411, "H103"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.9738, -73.57864, "Test Accessibility"), POIType.ELEVATOR, BuildingName.HALL, "1", true)
                    ))
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "2", new ArrayList<IndoorPOI>(Arrays.asList(
                        new IndoorPOI(new MapCoordinates(45.49716762, -73.57915985, "H283"), POIType.CLASS_ROOM, BuildingName.HALL, "2", false),
                        new IndoorPOI(new MapCoordinates(45.49738, -73.57864, "Test Washroom Hall F2"), POIType.WASHROOM, BuildingName.HALL, "2", false)
                    ))
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "3", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "4", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "5", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "6", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "7", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "8", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "9", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "10", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "11", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "S2", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "S1", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "1", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "2", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "3", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "4", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "5", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "6", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "7", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "8", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "9", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "10", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "11", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "12", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "13", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "14", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "15", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.LOYOLA_CENTRAL_BUILDING.getResourceName() + FLOOR_TAG + "1", new ArrayList<>()
                },
                {
                    BuildingName.VANIER_LIBRARY.getResourceName() + FLOOR_TAG + "1", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.VANIER_LIBRARY.getResourceName() + FLOOR_TAG + "2", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.VANIER_LIBRARY.getResourceName() + FLOOR_TAG + "3", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.VANIER_EXTENSION.getResourceName() + FLOOR_TAG + "1", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.VANIER_EXTENSION.getResourceName() + FLOOR_TAG + "2", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.VANIER_EXTENSION.getResourceName() + FLOOR_TAG + "3", new ArrayList<IndoorPOI>()
                }
        };
    }
}
