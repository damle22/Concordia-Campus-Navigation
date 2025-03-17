package minicap.concordia.campusnav.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;

//unused
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.components.BuildingInfoBottomSheetFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import android.widget.ImageButton;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.helpers.HttpHelper;

public class MainActivity extends AppCompatActivity {

    private ConcordiaBuildingManager buildingManager;
    private static final String GET_URL = "https://shuttle.concordia.ca/concordiabusmap/Map.aspx";
    private static final String POST_URL = "https://shuttle.concordia.ca/concordiabusmap/WebService/GService.asmx/GetGoogleObject";
    private static final String HOST = "shuttle.concordia.ca";
    private static final long UPDATE_INTERVAL = 30000; // 30 seconds for bus updates

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildingManager = ConcordiaBuildingManager.getInstance();
        new FetchBusDataTask().execute();
        startPeriodicUpdates();
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
    private class FetchBusDataTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Step 1: Get session cookies
                String cookies = HttpHelper.getSessionCookies(GET_URL, HOST);

                // Step 2: Make POST request with cookies
                String response = HttpHelper.postRequest(POST_URL, cookies, HOST);

                // Return the response (bus data)
                return response;
            } catch (IOException e) {
                Log.e("FetchBusDataTask", "Error fetching bus data", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                // Parse the JSON response
                Gson gson = new Gson();
                JsonObject busData = gson.fromJson(response, JsonObject.class);

                // Example: Log the parsed data
                Log.d("ParsedBusData", busData.toString());

                // Update UI or process the data further
            } else {
                Log.e("BusData", "Failed to fetch bus data");
            }
        }
    }
    private void startPeriodicUpdates() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                new FetchBusDataTask().execute();
                handler.postDelayed(this, UPDATE_INTERVAL);
            }
        };
        handler.postDelayed(runnable, UPDATE_INTERVAL);
    }
}