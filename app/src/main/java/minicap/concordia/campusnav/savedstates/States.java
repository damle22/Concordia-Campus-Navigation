package minicap.concordia.campusnav.savedstates;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.content.Context;
import android.util.Log;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.List;

import minicap.concordia.campusnav.buildingmanager.entities.Campus;

public class States {

    private Campus campus;

    private String campusName;

    private String otherCampus;

    private String otherCampusAbrev;

    private boolean darkMode = false;

    private boolean menuOpen = false;

    private static final States instance = new States();

    private long lastToggleTime = 0;
    private static final long DEBOUNCE_DELAY_MS = 500;

    private States(){}

    public static States getInstance(){
        return instance;
    }

    public void toggleDarkMode(boolean check) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastToggleTime < DEBOUNCE_DELAY_MS) {
            return;
        }

        lastToggleTime = currentTime;
        darkMode = check;
        applyDarkMode();
    }

    public boolean isDarkModeOn(){
        return darkMode;
    }

    public void applyDarkMode() {
        AppCompatDelegate.setDefaultNightMode(
                darkMode ? MODE_NIGHT_YES
                        : MODE_NIGHT_NO
        );
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        String campusFullName = campus.getCampusName();
        if (campusFullName.equals("Loyola campus")){
            campusName = "LOYOLA";
            otherCampus = "SGW";
            otherCampusAbrev = "SGW";
        } else if (campusFullName.equals("Sir George William campus")){
            campusName = "SGW";
            otherCampus = "LOYOLA";
            otherCampusAbrev = "LOY";
        }
        this.campus = campus;
    }

    public String getCampusName(){
        return campusName;
    }

    public String getOtherCampusName(){
        return otherCampus;
    }

    public String getOtherCampusAbrev() {
        return otherCampusAbrev;
    }

    public boolean isMenuOpen() {
        return menuOpen;
    }

    public void toggleMenu(boolean state){
        menuOpen = state;
    }
}
