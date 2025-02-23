package minicap.concordia.campusnav.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import minicap.concordia.campusnav.firebase.Persistable;

public class Campus implements Persistable {

    private String id;
    private String name;
    private List<Building> buildings;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    @Override
    public void populateFieldsFromMap(Map<String, Object> data) {
        this.id = (String) data.get("id");
        this.name = (String) data.get("name");
        buildings = new ArrayList<>();
        List<Map<String, Object>> buildingList = (List<Map<String, Object>>) data.get("buildings");
        if (buildingList != null) {
            this.buildings = new ArrayList<>();
            for (Map<String, Object> buildingData : buildingList) {
                Building building = new Building();
                building.populateFieldsFromMap(buildingData);
                this.buildings.add(building);
            }
        }
    }
}
