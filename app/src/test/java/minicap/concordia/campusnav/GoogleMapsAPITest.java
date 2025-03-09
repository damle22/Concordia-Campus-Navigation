package minicap.concordia.campusnav;

import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoogleMapsAPITest {
    @Test
    public void testDirectionsAPIConnection() {
        OkHttpClient client = new OkHttpClient();

        String url = String.format(
                "https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&key=%s",
                "45.48971252030307, -73.58808030276997", "45.49701,-73.57877", BuildConfig.MAPS_API_KEY);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            //Make sure HTML response was successful first
            assertNotNull("Response should not be null", response);
            assertEquals("Expected HTTP status 200", 200, response.code());
            String responseBody = response.body().string();
            assertNotNull("Response body should not be null", responseBody);
            //Make sure the status was success (This also includes checking for proper permissions)
            JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
            String status = json.get("status").getAsString();
            assertEquals("API call was not successful", "OK", status);

        } catch (Exception e) {
            assert false : "API connection failed: " + e.getMessage();
        }
    }
}
