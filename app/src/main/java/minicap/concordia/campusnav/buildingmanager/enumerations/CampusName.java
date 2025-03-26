package minicap.concordia.campusnav.buildingmanager.enumerations;

public enum CampusName {
    SGW,
    LOYOLA;

    public String getResourceName() {
        switch(this) {
            case SGW:
                return "CampusSGW";
            case LOYOLA:
                return "CampusLoyola";
            default:
                return "Unknown";
        }
    }
}
