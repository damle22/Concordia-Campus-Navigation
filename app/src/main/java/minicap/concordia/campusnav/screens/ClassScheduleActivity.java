package minicap.concordia.campusnav.screens;

import android.os.Bundle;

import android.widget.Button;
import android.widget.ImageButton;
import androidx.fragment.app.FragmentActivity;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.components.MainMenuDialog;

public class ClassScheduleActivity extends FragmentActivity
        implements MainMenuDialog.MainMenuListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //Main Menu dialog
        ImageButton menuButton = findViewById(R.id.button_menu);
        menuButton.setOnClickListener(v -> showMainMenuDialog());

        // Import button to handle Google Calendar import
        Button importButton = findViewById(R.id.button_import_calendar);
        importButton.setOnClickListener(v -> {
            // TODO:Google Calendar or handle import logic
        });
    }

    public void showMainMenuDialog() {
        MainMenuDialog dialog = new MainMenuDialog(this);
        dialog.show();
    }
}

