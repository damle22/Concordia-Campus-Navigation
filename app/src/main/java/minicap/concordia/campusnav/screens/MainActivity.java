package minicap.concordia.campusnav.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;

public class MainActivity extends AppCompatActivity {

    private ConcordiaBuildingManager buildingManager;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildingManager = ConcordiaBuildingManager.getInstance();

        /**
         * Example on how to fetch object from DB using ID
         * Building u = new Building();
         * u.setId("ID HERE");
         * u.fetch(aVoid -> {
         *    Log.d("MainActivity", "User fetched successfully: " + u.getName());
         *   });
         **/

        /**
         * Example on how to save Object to DB
         * Building u = new Building("test","test","Test");
         * u.save();
         */

        /**
         * Example on how to Fetch object using Field
         * Building u = new Building();
         * u.fetch("name", "test", aVoid -> {
         *    Log.d("MainActivity", "User fetched successfully: " + u.getName());
         * });
         **/

        subscribeButtons(this);
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
                startActivity(i);
            }
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
                startActivity(i);
            }
        });
    }
}