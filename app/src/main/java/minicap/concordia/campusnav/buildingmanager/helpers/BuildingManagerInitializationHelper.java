package minicap.concordia.campusnav.buildingmanager.helpers;

import android.util.Log;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.BuildingFloor;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.entities.poi.IndoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.buildingmanager.resources.IndoorPOIResource_en_CA;

public class BuildingManagerInitializationHelper {

    private static final String TAG = "BuildingManagerInitializationHelper";

    private BuildingManagerInitializationHelper() {}

    /**
     * Creates a map of CampusName, Campus objects that are read from the resource bundle
     * @return Map of CampusName, Campus based on resources
     */
    public static Map<CampusName, Campus> createCampuses() {
        CampusName[] campusesToGet = new CampusName[] {CampusName.SGW, CampusName.LOYOLA};
        Map<CampusName, Campus> finalCampuses = new EnumMap<>(CampusName.class);

        try {
            Locale locale = new Locale("en", "CA");
            ResourceBundle bundle = ResourceBundle.getBundle("minicap.concordia.campusnav.buildingmanager.resources.BuildingResource", locale);

            for(int i = 0; i < campusesToGet.length; i++){
                Campus fetchedCampus = (Campus)bundle.getObject(campusesToGet[i].getResourceName());
                finalCampuses.put(campusesToGet[i], fetchedCampus);
            }
        }
        catch(MissingResourceException e){
            Log.d(TAG, "Missing campus resource: " + e.getMessage());
        }
        catch(ClassCastException ce){
            Log.d(TAG, "One of the resources could not be casted as a campus!\nError: " + ce.getMessage());
        }

        return finalCampuses;
    }

    /**
     * Creates a map of BuildingName, Building based on the resources.
     *  Also populates indoor points of interest for each building floor
     * @return Map of BuildingName, Building based on the resource bundles
     */
    public static Map<BuildingName, Building> createBuildings(){
        // When adding a building, add the key and the resource key below
        // Maintaining the order is extremely important
        BuildingName[] buildingsToGet = new BuildingName[] {
                BuildingName.HALL,
                BuildingName.MOLSON_SCHOOL_OF_BUSINESS,
                BuildingName.LOYOLA_CENTRAL_BUILDING,
                BuildingName.VANIER_LIBRARY,
                BuildingName.VANIER_EXTENSION
        };

        EnumMap<BuildingName, Building> finalBuildings = new EnumMap<>(BuildingName.class);

        try {
            Locale locale = new Locale("en", "CA");
            ResourceBundle bundle = ResourceBundle.getBundle("minicap.concordia.campusnav.buildingmanager.resources.BuildingResource", locale);
            ResourceBundle indoorPOIBundle = ResourceBundle.getBundle("minicap.concordia.campusnav.buildingmanager.resources.IndoorPOIResource", locale);

            for(int i = 0; i < buildingsToGet.length; i++){
                Building fetchedBuilding = (Building)bundle.getObject(buildingsToGet[i].getResourceName());

                Collection<BuildingFloor> floors = fetchedBuilding.getFloors();
                for(BuildingFloor floor : floors) {
                    String desiredResource = buildingsToGet[i].getResourceName() + IndoorPOIResource_en_CA.FLOOR_TAG + floor.getFloorName();
                    List<IndoorPOI> fetchedPOIs = (List<IndoorPOI>) indoorPOIBundle.getObject(desiredResource);

                    floor.setFloorPOIs(fetchedPOIs);
                }

                finalBuildings.put(buildingsToGet[i], fetchedBuilding);
            }
        }
        catch(MissingResourceException e){
            Log.d(TAG, "Missing building resource: " + e.getMessage());
        }
        catch(ClassCastException ce){
            Log.d(TAG, "One of the resources could not be casted as a building!\nError: " + ce.getMessage());
        }

        return finalBuildings;
    }
}
