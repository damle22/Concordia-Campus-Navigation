package minicap.concordia.campusnav.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.model.LatLng;

//to be removed
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.components.BuildingInfoBottomSheetFragment;
import android.widget.ImageButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.components.ShuttleSchedule;
import minicap.concordia.campusnav.helpers.ScheduleFetcher;
import minicap.concordia.campusnav.helpers.ShuttleScraper;

public class MainActivity extends AppCompatActivity {

    private ConcordiaBuildingManager buildingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildingManager = ConcordiaBuildingManager.getInstance();

        subscribeButtons(this);

        /* debugging to make sure scraper works
        ScheduleFetcher.fetch(new ScheduleFetcher.ScheduleFetchListener() {
            @Override
            public void onScheduleFetched(List<ShuttleSchedule> schedules) {
                // Log the fetched schedules to verify correctness
                logSchedules(schedules);
            }
        };
         */
    }
    protected void subscribeButtons(Context appContext) {

        Button sgwCampusBtn = (Button)findViewById(R.id.viewSGWCampusButton);

        sgwCampusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coords = null;

                Campus sgwCampus = buildingManager.getCampus(CampusName.SGW);
                float[] campusCoordinates = sgwCampus.getLocation();
                coords = new LatLng(campusCoordinates[0], campusCoordinates[1]);

                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                i.putExtra(MapsActivity.KEY_STARTING_LAT, coords.latitude);
                i.putExtra(MapsActivity.KEY_STARTING_LNG, coords.longitude);
                i.putExtra(MapsActivity.KEY_CAMPUS_NOT_SELECTED, "LOY");
                i.putExtra(MapsActivity.KEY_SHOW_SGW, true);
                startActivity(i);            }
        });

        Button loyCampusBtn = (Button)findViewById(R.id.viewLoyCampusButton);

        loyCampusBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LatLng coords = null;


                Campus loyolaCampus = buildingManager.getCampus(CampusName.LOYOLA);
                float[] campusCoordinates = loyolaCampus.getLocation();
                coords = new LatLng(campusCoordinates[0], campusCoordinates[1]);

                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                i.putExtra(MapsActivity.KEY_STARTING_LAT, coords.latitude);
                i.putExtra(MapsActivity.KEY_STARTING_LNG, coords.longitude);
                i.putExtra(MapsActivity.KEY_CAMPUS_NOT_SELECTED, "SGW");
                i.putExtra(MapsActivity.KEY_SHOW_SGW, false);
                startActivity(i);
            }
        });
    }
    private void logSchedules(List<ShuttleSchedule> schedules) {
        for (ShuttleSchedule schedule : schedules) {
            String day = schedule.getDay();
            String campus = schedule.getCampus();
            List<String> times = schedule.getDepartureTimes();

            // Log the day, campus, and departure times
            Log.d("ShuttleBus", "Day: " + day + ", Campus: " + campus);
            for (String time : times) {
                Log.d("ShuttleBus", "Departure: " + time);
            }
            Log.d("ShuttleBus", "-----------------------------");
        }
    }
}