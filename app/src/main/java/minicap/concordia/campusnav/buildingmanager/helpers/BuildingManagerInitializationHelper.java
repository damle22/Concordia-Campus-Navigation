package minicap.concordia.campusnav.buildingmanager.helpers;

import java.util.ArrayList;
import java.util.HashMap;

import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.BuildingFloor;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;

//TODO: This is all static data, needs to be updated
public class BuildingManagerInitializationHelper {

    public static HashMap<CampusName, Campus> createCampuses() {
        HashMap<CampusName, Campus> finalCampuses = new HashMap<>();

        //SGW initialization
        ArrayList<BuildingName> sgwBuildings = new ArrayList<>();
        sgwBuildings.add(BuildingName.HALL);
        sgwBuildings.add(BuildingName.MOLSON_SCHOOL_OF_BUSINESS);

        Campus sgw = new Campus("Sir George William campus", sgwBuildings, 45.49701f, -73.57877f);
        finalCampuses.put(CampusName.SGW, sgw);

        //Loyola initialization
        ArrayList<BuildingName> loyBuildings = new ArrayList<>();
        loyBuildings.add(BuildingName.LOYOLA_CENTRAL_BUILDING);
        loyBuildings.add(BuildingName.VANIER_EXTENSION);
        loyBuildings.add(BuildingName.VANIER_LIBRARY);

        Campus loyola = new Campus("Loyola campus", loyBuildings, 45.45863f, -73.64188f);
        finalCampuses.put(CampusName.LOYOLA, loyola);

        return finalCampuses;
    }

    public static HashMap<BuildingName, Building> createBuildings(){
        HashMap<BuildingName, Building> finalBuildings = new HashMap<>();

        //SGW buildings
        HashMap<String, BuildingFloor> hallBuildingFloors = new HashMap<>();
        BuildingFloor hallBuildingFloor1 = new BuildingFloor("1", BuildingName.HALL, "none");
        BuildingFloor hallBuildingFloor2 = new BuildingFloor("2", BuildingName.HALL, "none");
        BuildingFloor hallBuildingFloor8 = new BuildingFloor("8", BuildingName.HALL, "none");
        BuildingFloor hallBuildingFloor9 = new BuildingFloor("9", BuildingName.HALL, "none");
        hallBuildingFloors.put("1", hallBuildingFloor1);
        hallBuildingFloors.put("2", hallBuildingFloor2);
        hallBuildingFloors.put("8", hallBuildingFloor8);
        hallBuildingFloors.put("9", hallBuildingFloor9);
        Building hallBuilding = new Building("Hall building", "The hall building", CampusName.SGW, hallBuildingFloors, 45.49701f, -73.57877f);

        HashMap<String, BuildingFloor> msbFloors = new HashMap<>();
        BuildingFloor msbFloor1 = new BuildingFloor("1", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none");
        BuildingFloor msbFloorS2 = new BuildingFloor("S2", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none");
        msbFloors.put("1", msbFloor1);
        msbFloors.put("S2", msbFloorS2);
        Building msbBuilding = new Building("John Molson's School of Business", "The JMSB building", CampusName.SGW, msbFloors, 45.49565f, -73.57906f);

        finalBuildings.put(BuildingName.HALL, hallBuilding);
        finalBuildings.put(BuildingName.MOLSON_SCHOOL_OF_BUSINESS, msbBuilding);

        //Loy buildings
        HashMap<String, BuildingFloor> ccFloors = new HashMap<>();
        BuildingFloor ccFloor1 = new BuildingFloor("1", BuildingName.LOYOLA_CENTRAL_BUILDING, "none");
        ccFloors.put("1", ccFloor1);
        Building ccBuilding = new Building("Loyola Central Building", "The loyola central building", CampusName.LOYOLA, ccFloors, 45.45863f, -73.64066f);

        HashMap<String, BuildingFloor> vlFloors = new HashMap<>();
        BuildingFloor vlFloor1 = new BuildingFloor("1", BuildingName.VANIER_LIBRARY, "none");
        BuildingFloor vlFloor2 = new BuildingFloor("2", BuildingName.VANIER_LIBRARY, "none");
        vlFloors.put("1", vlFloor1);
        vlFloors.put("2", vlFloor2);
        Building vlBuilding = new Building("Vanier Library building", "The Vanier library", CampusName.LOYOLA, vlFloors, 45.45891f, -73.63888f);

        HashMap<String, BuildingFloor> veFloors = new HashMap<>();
        BuildingFloor veFloor1 = new BuildingFloor("1", BuildingName.VANIER_EXTENSION, "none");
        BuildingFloor veFloor2 = new BuildingFloor("2", BuildingName.VANIER_EXTENSION, "none");
        veFloors.put("1", veFloor1);
        veFloors.put("2", veFloor2);
        Building veBuilding = new Building("Vanier Extension building", "The Vanier extension building", CampusName.LOYOLA, veFloors, 45.45891f, -73.63888f);

        finalBuildings.put(BuildingName.LOYOLA_CENTRAL_BUILDING, ccBuilding);
        finalBuildings.put(BuildingName.VANIER_LIBRARY, vlBuilding);
        finalBuildings.put(BuildingName.VANIER_EXTENSION, veBuilding);

        return finalBuildings;
    }

    public static ArrayList<OutdoorPOI> createOutdoorPOIs(){
        ArrayList<OutdoorPOI> outdoorPOIS = new ArrayList<>();

        //Nothing to create for now...

        return outdoorPOIS;
    }
}
