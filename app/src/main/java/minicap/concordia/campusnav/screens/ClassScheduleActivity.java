package minicap.concordia.campusnav.screens;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import minicap.concordia.campusnav.BuildConfig;
import minicap.concordia.campusnav.R;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.services.calendar.CalendarScopes;

public class ClassScheduleActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    private static final int REQUEST_CALENDAR_PERMISSION = 101;

    private GoogleSignInClient googleSignInClient;

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

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(CalendarScopes.CALENDAR_READONLY))
                .requestIdToken("37758349959-7eh7pjvbrjaqov5nd412d2l0ml7fdkul.apps.googleusercontent.com")
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Import button to handle Google Calendar import
        Button importButton = findViewById(R.id.button_import_calendar);
        importButton.setOnClickListener(v -> signIn());
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

                Toast.makeText(this, "Google Sign-In successful", Toast.LENGTH_SHORT).show();
                requestCalendarPermission();
            } catch (ApiException e) {
                Log.e("Google Sign in,", "Failed - " + e.toString());
                Toast.makeText(this, "Google Sign-In failed - " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestCalendarPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, REQUEST_CALENDAR_PERMISSION);
        } else {
            // fetchCalendarEvents();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALENDAR_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // fetchCalendarEvents();
        } else {
            Toast.makeText(this, "Calendar permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
