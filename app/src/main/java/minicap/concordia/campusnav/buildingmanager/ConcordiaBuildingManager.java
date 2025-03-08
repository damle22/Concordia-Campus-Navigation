package minicap.concordia.campusnav.buildingmanager;

import java.util.ArrayList;
import java.util.HashMap;

import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import minicap.concordia.campusnav.buildingmanager.helpers.BuildingManagerInitializationHelper;

public class ConcordiaBuildingManager {

    private static ConcordiaBuildingManager mInstance = null;

    private HashMap<BuildingName, Building> buildings;
    private HashMap<CampusName, Campus> campuses;
    private ArrayList<OutdoorPOI> outdoorPOIs;

    private ConcordiaBuildingManager() {
        buildings = new HashMap<>();
        campuses = new HashMap<>();
        outdoorPOIs = new ArrayList<>();
        initialize();
    }

    /**
     * Gets the singleton instance of ConcordiaBuildingManager.
     * @return The current instance of ConcordiaBuildingManager.
     */
    public static ConcordiaBuildingManager getInstance() {
        if (mInstance == null) {
            mInstance = new ConcordiaBuildingManager();
        }
        return mInstance;
    }

    /**
     * Gets a Building object based on the given BuildingName enum.
     * @param name The enum representing the desired building.
     * @return The Building if found, otherwise null.
     */
    public Building getBuilding(BuildingName name) {
        return buildings.get(name);
    }

    /**
     * Gets a Campus object based on the given CampusName enum.
     * @param name The enum for the desired campus.
     * @return The Campus object if found, otherwise null.
     */
    public Campus getCampus(CampusName name) {
        return campuses.get(name);
    }

    /**
     * Gets all buildings associated with a specific campus.
     * @param name The campus enum for which buildings are returned.
     * @return An ArrayList of Buildings for the given campus.
     */
    public ArrayList<Building> getBuildingsForCampus(CampusName name) {
        Campus campus = campuses.get(name);
        ArrayList<BuildingName> buildingNames = new ArrayList<>();
        ArrayList<Building> finalBuildings = new ArrayList<>();

        if (campus != null) {
            buildingNames = campus.getAssociatedBuildings();
        }

        if (!buildingNames.isEmpty()) {
            for (BuildingName buildingName : buildingNames) {
                Building newBuilding = buildings.get(buildingName);
                if (newBuilding != null) {
                    finalBuildings.add(newBuilding);
                }
            }
        }
        return finalBuildings;
    }

    /**
     * Returns all OutdoorPOI objects.
     * @return ArrayList of all OutdoorPOI.
     */
    public ArrayList<OutdoorPOI> getAllOutdoorPOIs() {
        return outdoorPOIs;
    }

    /**
     * Returns all OutdoorPOI objects that match a specified POIType.
     * @param type The type of POI desired.
     * @return An ArrayList of matching OutdoorPOI objects.
     */
    public ArrayList<OutdoorPOI> getAllOutdoorPOIsOfType(POIType type) {
        ArrayList<OutdoorPOI> finalPOIs = new ArrayList<>();
        for (OutdoorPOI poi : outdoorPOIs) {
            if (poi.getPOIType() == type) {
                finalPOIs.add(poi);
            }
        }
        return finalPOIs;
    }

    /**
     * Returns all OutdoorPOI objects that have the accessibility flag, regardless of POIType.
     * @return ArrayList of OutdoorPOIs with the accessibility flag.
     */
    public ArrayList<OutdoorPOI> getOutdoorAccessibilityPOIs() {
        ArrayList<OutdoorPOI> finalPOIs = new ArrayList<>();
        for (OutdoorPOI poi : outdoorPOIs) {
            if (poi.getIsAccessibilityFeature()) {
                finalPOIs.add(poi);
            }
        }
        return finalPOIs;
    }

    /**
     * Returns all OutdoorPOI objects that have the accessibility flag and match the specified POIType.
     * @param type The POI type to filter by.
     * @return ArrayList of OutdoorPOI that match the type and have the accessibility flag.
     */
    public ArrayList<OutdoorPOI> getOutdoorAccessibilityPOIs(POIType type) {
        ArrayList<OutdoorPOI> finalPOIs = new ArrayList<>();
        for (OutdoorPOI poi : outdoorPOIs) {
            if (poi.getIsAccessibilityFeature() && poi.getPOIType() == type) {
                finalPOIs.add(poi);
            }
        }
        return finalPOIs;
    }

    /**
     * Initializes all known campuses, buildings, and outdoor POIs by calling the helper methods.
     */
    private void initialize() {
        campuses = BuildingManagerInitializationHelper.createCampuses();
        buildings = BuildingManagerInitializationHelper.createBuildings();
        outdoorPOIs = BuildingManagerInitializationHelper.createOutdoorPOIs();
    }

    /**
     * Returns an ArrayList of all Buildings (combining all campus buildings).
     * @return A list of every Building stored in this manager.
     */
    public ArrayList<Building> getAllBuildings() {
        return new ArrayList<>(buildings.values());
    }
}
