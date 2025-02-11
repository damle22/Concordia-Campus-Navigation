package minicap.concordia.campusnav.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.helpers.CoordinateResHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subscribeButtons(this);
    }

    protected void subscribeButtons(Context appContext) {
        Button sgwCampusBtn = (Button)findViewById(R.id.viewSGWCampusButton);

        sgwCampusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coords = null;
                try {
                    coords = CoordinateResHelper.getCoordsForGoogleMaps(appContext, CoordinateResHelper.BuildingNames.SGWHallBuilding);
                }
                catch (IllegalArgumentException e) {
                    Log.d("SGWCampusButton", "Error while retrieving coordinates for SGW Hall Building");
                }

                if(coords != null) {
                    Intent i = new Intent(MainActivity.this, MapsActivity.class);
                    i.putExtra(MapsActivity.KEY_STARTING_LAT, (float) coords.latitude);
                    i.putExtra(MapsActivity.KEY_STARTING_LNG, (float) coords.longitude);
                    startActivity(i);
                }
            }
        });

        Button loyCampusBtn = (Button)findViewById(R.id.viewLoyCampusButton);

        loyCampusBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LatLng coords = null;
                try {
                    coords = CoordinateResHelper.getCoordsForGoogleMaps(appContext, CoordinateResHelper.BuildingNames.LoyHUBuilding);
                }
                catch (IllegalArgumentException e) {
                    Log.d("LoyCampusBtn", "Error while retrieving coordinates for Loyola HU Building");
                }

                if(coords != null) {
                    Intent i = new Intent(MainActivity.this, MapsActivity.class);
                    i.putExtra(MapsActivity.KEY_STARTING_LAT, (float) coords.latitude);
                    i.putExtra(MapsActivity.KEY_STARTING_LNG, (float) coords.longitude);
                    startActivity(i);
                }
            }
        });
    }
}