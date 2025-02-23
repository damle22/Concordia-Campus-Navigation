package minicap.concordia.campusnav.beans;

import java.util.Map;

import minicap.concordia.campusnav.firebase.Persistable;

public class Building implements Persistable {

    //Id is building code Example: MB
    private String id;
    private String name;
    private String description;

    public Building() {}

    public Building(String name, String campus, String description) {
        this.name = name;
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
        this.description = (String) data.get("description");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
