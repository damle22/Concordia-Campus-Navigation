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

    public void toggleDarkMode(){
        darkMode = !darkMode;
    }

    public boolean isDarkModeOn(){
        return darkMode;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        campusName = campus.getCampusName();
        if (campusName.equals("Loyola campus")){
            otherCampus = "SGW";
        } else if (campusName.equals("Sir George William campus")){
            otherCampus = "LOYOLA";
        }
        this.campus = campus;
    }

    public String getCampusName(){
        return campusName;
    }

    public String getOtherCampusName(){
        return otherCampus;
    }

    public String getOtherCampusNameAbreviated(){
        return otherCampus.substring(0, 3);
    }
}
