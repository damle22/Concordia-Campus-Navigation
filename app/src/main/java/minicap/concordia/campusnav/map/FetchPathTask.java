package minicap.concordia.campusnav.map;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Callback Interface
public class FetchPathTask {
    private final OnRouteFetchedListener listener;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    /**
     * Constructor with Listener
     * @param listener OnRouteFetchedListener
     */
    public FetchPathTask(OnRouteFetchedListener listener) {
        this.listener = listener;
    }

    /**
     * Fetches the route given the google api url.
     * Will invoke the listener for onRouteFetched with the route once complete
     * @param urlString GoogleAPI String
     */
    public void fetchRoute(String urlString) {
        executorService.execute(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                stream.close();

                List<LatLng> route = parseRoute(response.toString());

                mainThreadHandler.post(() -> {
                    if (listener != null) {
                        listener.onRouteFetched(route);
                    }
                });

            } catch (IOException e) {
                Log.e("FetchRoute(): ", e.toString());
            }
        });
    }

    /**
     * Given a json response from google API, it will parse the Route
     * @param json response from google API
     * @return List of LatLng points
     */
    private List<LatLng> parseRoute(String json) {
        List<LatLng> path = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray routes = jsonObject.getJSONArray("routes");

            if (routes.length() > 0) {
                JSONObject route = routes.getJSONObject(0);
                JSONArray legs = route.getJSONArray("legs");
                JSONObject leg = legs.getJSONObject(0);
                JSONArray steps = leg.getJSONArray("steps");

                for (int i = 0; i < steps.length(); i++) {
                    JSONObject step = steps.getJSONObject(i);
                    JSONObject start = step.getJSONObject("start_location");
                    LatLng point = new LatLng(start.getDouble("lat"), start.getDouble("lng"));
                    String encodedPolyline = step.getJSONObject("polyline").getString("points");
                    path.addAll(PolyUtil.decode(encodedPolyline));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * Listener for Google Api
     */
    public interface OnRouteFetchedListener {
        void onRouteFetched(List<LatLng> route);
    }
}