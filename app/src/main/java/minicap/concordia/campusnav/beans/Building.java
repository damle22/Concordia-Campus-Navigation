package minicap.concordia.campusnav.beans;

import java.util.Map;

import minicap.concordia.campusnav.firebase.Persistable;

public class Building implements Persistable {

    //Id is generated when saving
    private String id;
    private String name;
    private String campus;
    private String description;

    public Building() {}

    public Building(String name, String campus, String description) {
        this.name = name;
        this.campus = campus;
        this.description = description;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void populateFieldsFromMap(Map<String, Object> data) {
        this.id = (String) data.get("id");
        this.name = (String) data.get("name");
        this.campus = (String) data.get("campus");
        this.description = (String) data.get("description");

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
