package minicap.concordia.campusnav.buildingmanager.entities;

import java.util.ArrayList;

import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;

public class Campus extends Location {

    private ArrayList<BuildingName> associatedBuildings;

    private String campusName;

    public Campus(String campusName, ArrayList<BuildingName> associatedBuildings, double latitude, double longitude) {
        super(latitude, longitude);
        this.campusName = campusName;
        this.associatedBuildings = associatedBuildings;
    }

    /**
     * Gets the BuildingNames that are associated with this campus
     * @return The BuildingNames that are associated with this campus
     */
    public ArrayList<BuildingName> getAssociatedBuildings() {
        return associatedBuildings;
    }

    /**
     * Gets the name of the campus
     * @return The name of the campus
     */
    public String getCampusName(){
        return campusName;
    }
}
