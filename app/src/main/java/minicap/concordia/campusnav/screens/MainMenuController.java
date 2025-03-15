package minicap.concordia.campusnav.screens;

import android.view.View;
import android.widget.ImageButton;

public class MainMenuController {

    View slidingMenu;
    ImageButton openMenuButton;
    ImageButton closeMenuButton;
    ImageButton classScheduleRedirect;
    ImageButton directionsRedirect;
    ImageButton campusMapRedirect;

    //This passes
    public MainMenuController(View slidingMenu, ImageButton openMenuButton, ImageButton closeMenuButton, ImageButton classScheduleRedirect, ImageButton directionsRedirect, ImageButton campusMapRedirect){
        this.slidingMenu = slidingMenu;
        this.openMenuButton = openMenuButton;
        this.closeMenuButton = closeMenuButton;
        this.classScheduleRedirect = classScheduleRedirect;
        this.directionsRedirect = directionsRedirect;
        this.campusMapRedirect = campusMapRedirect;

        slidingMenu.post(new Runnable() {
            @Override
            public void run() {
                close();
            }
        });

        openMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open();
            }
        });

        closeMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        campusMapRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCampusMap();
            }
        });
    }

    public void openCampusMap(){
        //Open Intent
    }

    public void open(){
        slidingMenu.animate()
                .translationX(0)
                .setDuration(300)
                .start();
    }

    public void close(){
        slidingMenu.animate()
                .translationX(-slidingMenu.getWidth())
                .setDuration(300)
                .start();
    }

}
