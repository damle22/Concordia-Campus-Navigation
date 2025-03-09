package minicap.concordia.campusnav;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GoogleMapsAPITest {

    @Test
    public void testRoutesAPIConnection() {
        String urlString = "https://routes.googleapis.com/directions/v2:computeRoutes?key=" + BuildConfig.MAPS_API_KEY;

        JsonObject requestBody = new JsonObject();

        JsonObject origin = new JsonObject();
        JsonObject originLocation  = new JsonObject();
        JsonObject originLatLng  = new JsonObject();
        originLatLng.addProperty("latitude", 45.48971252030307);
        originLatLng.addProperty("longitude", -73.58808030276997);
        originLocation.add("latLng", originLatLng);
        origin.add("location", originLocation);
        requestBody.add("origin", origin);

        JsonObject destination = new JsonObject();
        JsonObject destinationLocation = new JsonObject();
        JsonObject destinationLatLng = new JsonObject();
        destinationLatLng.addProperty("latitude", 37.417670);
        destinationLatLng.addProperty("longitude", -122.079595);
        destinationLocation.add("latLng", destinationLatLng);
        destination.add("location", destinationLocation);
        requestBody.add("destination", destination);
        try{
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
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBody.append(line);
            }

            assertEquals("Expected HTTP status 200", 200,connection.getResponseCode());
            assertNotNull("Response body should not be null", responseBody);

            reader.close();
            stream.close();
        }catch (Exception e){
            e.printStackTrace();
            assert false : "API connection failed: " + e.getMessage();
        }

    }
}