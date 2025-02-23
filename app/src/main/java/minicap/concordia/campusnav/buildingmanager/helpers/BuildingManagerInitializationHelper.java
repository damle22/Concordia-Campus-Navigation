package minicap.concordia.campusnav.buildingmanager.helpers;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;

public class BuildingManagerInitializationHelper {

    public static HashMap<CampusName, Campus> createCampuses() {
        CampusName[] mapKeysForResource = new CampusName[] {CampusName.SGW, CampusName.LOYOLA};
        String[] campusesToGet = new String[] {"CampusSGW", "CampusLoyola"};
        HashMap<CampusName, Campus> finalCampuses = new HashMap<>();

        try {
            Locale locale = new Locale("en", "CA");
            ResourceBundle bundle = ResourceBundle.getBundle("minicap.concordia.campusnav.buildingmanager.resources.BuildingResource", locale);

            for(int i = 0; i < campusesToGet.length; i++){
                Campus fetchedCampus = (Campus)bundle.getObject(campusesToGet[i]);
                finalCampuses.put(mapKeysForResource[i], fetchedCampus);
            }
        }
        catch(MissingResourceException e){
            Log.d("BuildingManagerInitializationHelper", "Missing campus resource: " + e.getMessage());
        }
        catch(ClassCastException ce){
            Log.d("BuildingManagerInitializationHelper", "One of the resources could not be casted as a campus!\nError: " + ce.getMessage());
        }

        return finalCampuses;
    }

    public static HashMap<BuildingName, Building> createBuildings(){
        // When adding a building, add the key and the resource key below
        // Maintaining the order is extremely important
        BuildingName[] mapKeysForResource = new BuildingName[] {
                BuildingName.HALL,
                BuildingName.MOLSON_SCHOOL_OF_BUSINESS,
                BuildingName.LOYOLA_CENTRAL_BUILDING,
                BuildingName.VANIER_LIBRARY,
                BuildingName.VANIER_EXTENSION
        };
        String[] buildingsToGet = new String[]{
                "BuildingHall",
                "BuildingJMSB",
                "BuildingCC",
                "BuildingVL",
                "BuildingVE"
        };
        HashMap<BuildingName, Building> finalBuildings = new HashMap<>();

        try {
            Locale locale = new Locale("en", "CA");
            ResourceBundle bundle = ResourceBundle.getBundle("minicap.concordia.campusnav.buildingmanager.resources.BuildingResource", locale);

            for(int i = 0; i < buildingsToGet.length; i++){
                Building fetchedBuilding = (Building)bundle.getObject(buildingsToGet[i]);
                finalBuildings.put(mapKeysForResource[i], fetchedBuilding);
            }
        }
        catch(MissingResourceException e){
            Log.d("BuildingManagerInitializationHelper", "Missing building resource: " + e.getMessage());
        }
        catch(ClassCastException ce){
            Log.d("BuildingManagerInitializationHelper", "One of the resources could not be casted as a building!\nError: " + ce.getMessage());
        }

        return finalBuildings;
    }

    public static ArrayList<OutdoorPOI> createOutdoorPOIs(){
        ArrayList<OutdoorPOI> outdoorPOIS = new ArrayList<>();

        //Nothing to create for now...

        return outdoorPOIS;
    }
}
