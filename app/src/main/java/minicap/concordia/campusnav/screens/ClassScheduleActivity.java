package minicap.concordia.campusnav.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import minicap.concordia.campusnav.R;

public class ClassScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //Add main menu functionality to page
        View slidingMenu = findViewById(R.id.sliding_menu);
        ImageButton openMenuButton = findViewById(R.id.button_menu);
        ImageButton closeMenuButton = findViewById(R.id.closeMenu);
        ImageButton classScheduleRedirect = findViewById(R.id.classScheduleRedirect);
        ImageButton directionsRedirect = findViewById(R.id.directionsRedirect);
        ImageButton campusMapRedirect = findViewById(R.id.campusMapRedirect);
        MainMenuController menu = new MainMenuController(this, slidingMenu, openMenuButton, closeMenuButton, classScheduleRedirect, directionsRedirect, campusMapRedirect);

        // Import button to handle Google Calendar import
        Button importButton = findViewById(R.id.button_import_calendar);
        importButton.setOnClickListener(v -> {
            // TODO:Google Calendar or handle import logic
        });
    }
}
