package minicap.concordia.campusnav;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.google.maps.android.PolyUtil;

import minicap.concordia.campusnav.map.FetchPathTask;

import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
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

    @Test
    public void testParseRoute_withValidJson() {
        try{
            String jsonResponse = "{\n" +
                    "   \"routes\": [\n" +
                    "      {\n" +
                    "         \"legs\": [\n" +
                    "            {\n" +
                    "               \"steps\": [\n" +
                    "                  {\n" +
                    "                     \"polyline\": {\n" +
                    "                        \"encodedPolyline\": \"austGztc`MHIJ?Zj@H^TZJLPDB?LI\\\\{@JQJ@bAbA[`AgA`Dq@xBcCuCsB}B{BcCu@{@QUc@s@eAyAg@}@w@{Aa@y@IOe@_AcBeDOYYe@Ua@KKMM][m@i@aBwA}AsAAA}@s@gA}@e@a@e@a@_@Y_Au@_AaAMMQSYYuAqAcAcAs@s@[[}AyAGEUUt@mB~BkG|@x@`A|@bBvA\"\n" +
                    "                     }\n" +
                    "                  }\n" +
                    "               ]\n" +
                    "            }\n" +
                    "         ]\n" +
                    "      }\n" +
                    "   ]\n" +
                    "}";

            FetchPathTask fetchPathTask = new FetchPathTask(null);
            JSONArray steps = fetchPathTask.parseRoute(jsonResponse);
            assertNotNull("The steps should not be null", steps);
            String encodedPolyline = steps.getJSONObject(0).getJSONObject("polyline").getString("encodedPolyline");
            List<LatLng> decodedPoints = PolyUtil.decode(encodedPolyline);
            assertEquals("Latitude should match", 45.4896, decodedPoints.get(0).latitude, 0.0001);
            assertEquals("Longitude should match", -73.5881, decodedPoints.get(0).longitude, 0.0001);
        }catch(Exception e){
            e.printStackTrace();
            assert false : "API connection failed: " + e.getMessage();
        }

    }

    @Test
    public void testParseRoute_withInvalidJson() {
        String invalidJson = "{ invalid json response }";
        FetchPathTask fetchPathTask = new FetchPathTask(null);
        JSONArray steps = fetchPathTask.parseRoute(invalidJson);
        assertNull("The path should be null", steps);
    }
}