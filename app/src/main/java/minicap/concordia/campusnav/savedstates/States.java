package minicap.concordia.campusnav.savedstates;

import minicap.concordia.campusnav.buildingmanager.entities.Campus;

public class States {

    private Campus campus;

    private String campusName;

    private String otherCampus;

    private boolean darkMode = false;

    private static final States instance = new States();

    private States(){}

    public static States getInstance(){
        return instance;
    }

    public Campus getCampus() {
        return campus;
    }

    public void toggleDarkMode(){
        darkMode = !darkMode;
    }

    public boolean isDarkModeOn(){
        return darkMode;
    }

    public void setCampus(Campus campus) {
        campusName = campus.getCampusName();
        if (campusName.equals("LOYOLA")){
            otherCampus = "SGW";
        } else if (campusName.equals("SGW")){
            otherCampus = "LOY";
        }

        this.campus = campus;
    }

    public String getCampusName(){
        return campusName;
    }

    public String getOtherCampus(){
        return otherCampus;
    }
}
