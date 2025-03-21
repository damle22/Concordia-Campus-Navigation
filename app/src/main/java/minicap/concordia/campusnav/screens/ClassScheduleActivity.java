package minicap.concordia.campusnav.screens;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.widget.Button;
import android.widget.Toast;
import android.widget.ImageButton;
import androidx.fragment.app.FragmentActivity;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.components.MainMenuDialog;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


public class ClassScheduleActivity extends FragmentActivity implements MainMenuDialog.MainMenuListener{

    private static final int RC_SIGN_IN = 100;
    private static final int REQUEST_CALENDAR_PERMISSION = 101;

    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //Main Menu dialog
        ImageButton menuButton = findViewById(R.id.button_menu);
        menuButton.setOnClickListener(v -> showMainMenuDialog());

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new com.google.android.gms.common.api.Scope("https://www.googleapis.com/auth/calendar.readonly"))
                .build();


        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInClient.signOut().addOnCompleteListener(task -> {
        });

        // Import button to handle Google Calendar import
        Button importButton = findViewById(R.id.button_import_calendar);
        importButton.setOnClickListener(v -> signIn());
    }

    public void showMainMenuDialog() {
        MainMenuDialog dialog = new MainMenuDialog(this);
        dialog.show();
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(this, "Google Sign-In Success", Toast.LENGTH_SHORT).show();
                requestCalendarPermission();
            } catch (ApiException e) {
                Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestCalendarPermission() {
        //we are requesting the permission if it is not granted yet
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, REQUEST_CALENDAR_PERMISSION);
        } else { //if already granted, we fetch the events
            fetchCalendarEvents();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALENDAR_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Calendar permission granted!", Toast.LENGTH_SHORT).show();
            // we fetch the calendar events once permission is granted
            fetchCalendarEvents();
        } else {
            Toast.makeText(this, "Calendar permission denied", Toast.LENGTH_SHORT).show();
        }
    }
    private void fetchCalendarEvents() {
        Toast.makeText(this, "Fetching calendar events...", Toast.LENGTH_SHORT).show();
    }


}