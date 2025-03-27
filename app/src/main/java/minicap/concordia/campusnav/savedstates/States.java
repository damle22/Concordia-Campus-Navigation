package minicap.concordia.campusnav.savedstates;

public class States {

    private String campus;

    private boolean darkMode = false;

    private static final States instance = new States();

    private States(){}

    public static States getInstance(){
        return instance;
    }

    public String getCampus() {
        return campus;
    }

    public void toggleDarkMode(){
        darkMode = !darkMode;
    }

    public boolean isDarkModeOn(){
        return darkMode;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }
}
