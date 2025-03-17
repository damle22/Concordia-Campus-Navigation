package minicap.concordia.campusnav.helpers;

import java.io.IOException;
import okhttp3.*;

public class HttpHelper {
    private static final OkHttpClient client = new OkHttpClient();

    // Method to make a GET request and retrieve cookies
    public static String getSessionCookies(String url, String host) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Host", host)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Extract cookies from the response
            Headers headers = response.headers();
            String cookies = headers.get("Set-Cookie");
            return cookies;
        }
    }

    // Method to make a POST request with cookies
    public static String postRequest(String url, String cookies, String host) throws IOException {
        RequestBody body = RequestBody.create("", MediaType.parse("application/json; charset=UTF-8"));

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Host", host)
                .addHeader("Content-Length", "0")
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .addHeader("Cookie", cookies)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }
}
