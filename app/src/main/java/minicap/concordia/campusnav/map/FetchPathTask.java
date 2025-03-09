package minicap.concordia.campusnav.map;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import minicap.concordia.campusnav.BuildConfig;

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
     * Fetches the route given origin and destination.
     * Will invoke the listener for onRouteFetched with the route once complete
     * @param originObj LatLng
     * @param destinationObj LatLng
     */
    public void fetchRoute(LatLng originObj, LatLng destinationObj, String travelModeStr) {
        String urlString = "https://routes.googleapis.com/directions/v2:computeRoutes?key=" + BuildConfig.MAPS_API_KEY;

        JSONObject requestBody = new JSONObject();
        try {

            JSONObject origin = new JSONObject();
            JSONObject originLocation = new JSONObject();
            JSONObject originLatLng = new JSONObject();
            originLatLng.put("latitude", originObj.latitude);
            originLatLng.put("longitude", originObj.longitude);
            originLocation.put("latLng", originLatLng);
            origin.put("location", originLocation);
            requestBody.put("origin", origin);

            JSONObject destination = new JSONObject();
            JSONObject destinationLocation = new JSONObject();
            JSONObject destinationLatLng = new JSONObject();
            destinationLatLng.put("latitude", destinationObj.latitude);
            destinationLatLng.put("longitude", destinationObj.longitude);
            destinationLocation.put("latLng", destinationLatLng);
            destination.put("location", destinationLocation);
            requestBody.put("destination", destination);

            JSONObject travelMode = new JSONObject();
            travelMode.put("travelMode", travelModeStr);


        } catch (JSONException e) {
            Log.e("FetchRoute(): ", e.toString());
        }

        executorService.execute(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("X-Goog-FieldMask","*");
                connection.setRequestProperty("X-Goog-Api-Key",BuildConfig.MAPS_API_KEY);
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                byte[] input = requestBody.toString().getBytes("utf-8");
                os.write(input, 0, input.length);

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
    public List<LatLng> parseRoute(String json) {
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
                    JSONObject polyline = step.getJSONObject("polyline");
                    String encodedPolyline = polyline.getString("encodedPolyline");

                    path.addAll(PolyUtil.decode(encodedPolyline));
                }
            }
        } catch (JSONException e) {
            Log.e("ParseRoute(): ", e.toString());
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