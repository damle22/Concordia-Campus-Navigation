package minicap.concordia.campusnav.helpers;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import minicap.concordia.campusnav.BuildConfig;

public class LocationSearchHelper {
    private static final String API_KEY = BuildConfig.MAPS_API_KEY;

    public static List<String> searchLocation(String query) throws Exception {
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("Invalid search query");
        }

        String urlString = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + query + "&key=" + API_KEY;
        return fetchLocationResults(urlString);
    }

    public static List<String> fetchLocationResults(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return parseLocationResults(response.toString());
    }

    public static List<String> parseLocationResults(String jsonResponse) throws Exception {
        List<String> locations = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray predictions = jsonObject.getJSONArray("predictions");
        for (int i = 0; i < predictions.length(); i++) {
            locations.add(predictions.getJSONObject(i).getString("description"));
        }
        return locations;
    }
}
