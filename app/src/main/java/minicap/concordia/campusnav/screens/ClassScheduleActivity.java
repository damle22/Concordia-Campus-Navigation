package minicap.concordia.campusnav.screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import minicap.concordia.campusnav.R;


public class ClassScheduleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //Waiting For Karim's Part
        // Menu button will open the menu (Maps, Directions, or class schedule)
        ImageButton menuButton = findViewById(R.id.button_menu);
        menuButton.setOnClickListener(v -> finish()); // for now it will close the activity

        // Import button to handle Google Calendar import
        Button importButton = findViewById(R.id.button_import_calendar);
        importButton.setOnClickListener(v -> {
            // TODO:Google Calendar or handle import logic
        });
    }
}
