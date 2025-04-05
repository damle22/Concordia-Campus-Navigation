package minicap.concordia.campusnav.components;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import minicap.concordia.campusnav.BuildConfig;
import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.helpers.HTMLAssetHelper;
import minicap.concordia.campusnav.helpers.UserLocationService;
import minicap.concordia.campusnav.map.MapCoordinates;

public class MappedInWebViewFragment extends Fragment implements UserLocationService.UserLocationUpdatedListener {

    private static final String MAPPED_IN_ASSET_NAME = "mappedIn.html";
    private static final String MAPPED_IN_KEY_STRING = "<MAPPED_IN_KEY>";
    private static final String MAPPED_IN_SECRET_STRING = "<MAPPED_IN_SECRET>";
    private static final String TAG = "MappedInWebViewFragment";

    private UserLocationService locationService;

    private MappedInMapEventListener curListener;
    private WebView map;

    public MappedInWebViewFragment() {
        // Required empty public constructor
    }

    public static MappedInWebViewFragment newInstance(MappedInMapEventListener listener) {
        MappedInWebViewFragment fragment = new MappedInWebViewFragment();
        fragment.curListener = listener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mapped_in_web_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        map = view.findViewById(R.id.mapped_in_map);

        map.getSettings().setJavaScriptEnabled(true);
        map.getSettings().setGeolocationEnabled(true);
        map.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                testLoad();
            }
        });
        map.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

        map.addJavascriptInterface(new AndroidJSListener(curListener), "Android");

        HTMLAssetHelper helper = new HTMLAssetHelper(view.getContext());

        String unencodedHtml = helper.getHtmlContent(MAPPED_IN_ASSET_NAME);

        unencodedHtml = unencodedHtml.replace(MAPPED_IN_KEY_STRING, BuildConfig.MAPPED_IN_KEY);
        unencodedHtml = unencodedHtml.replace(MAPPED_IN_SECRET_STRING, BuildConfig.MAPPED_IN_SECRET);

        String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(), Base64.NO_PADDING);

        map.loadData(encodedHtml, "text/html", "base64");
    }


    public void toggleLocationTracking(boolean isEnabled) {
        String[] args = new String[1];
        args[0] = String.valueOf(isEnabled);
        runJavascriptCommand("toggleBlueDot", args);

        startManualLocationTracking(this.getContext());
    }

    public void drawPath(MapCoordinates origin, MapCoordinates end, boolean isAccessibility) {
        String[] args = new String[5];
        args[0] = String.valueOf(origin.getLat());
        args[1] = String.valueOf(origin.getLng());
        args[2] = String.valueOf(end.getLat());
        args[3] = String.valueOf(end.getLng());
        args[4] = String.valueOf(isAccessibility);

        runJavascriptCommand("drawPathWithCoordinates", args);
    }

    public void addMarker(MapCoordinates coordinates, String title) {
        String[] args = new String[3];
        args[0] = String.valueOf(coordinates.getLat());
        args[1] = String.valueOf(coordinates.getLng());
        args[2] = title;

        runJavascriptCommand("addMarkerFromAndroid", args);
    }

    public void removeAllMarkers() {
        runJavascriptCommand("removeAllMarkers");
    }

    private void startManualLocationTracking(Context context) {
        locationService = new UserLocationService(context, this);
        locationService.start(1000);
    }

    private void testLoad() {
        String[] args = new String[1];
        args[0] = "67df02d0aa7c59000baf8d83";
        runJavascriptCommand("loadMap", args);
    }

    private void updateUserPosition(MapCoordinates coords) {
        String[] args = new String[2];
        args[0] = String.valueOf(coords.getLat());
        args[1] = String.valueOf(coords.getLng());

        runJavascriptCommand("updateBlueDotPosition", args);
    }

    private void runJavascriptCommand(String methodName) {
        Log.d(TAG, "Running command: " + methodName);
        map.post(new Runnable() {
            @Override
            public void run() {
                map.evaluateJavascript("javascript:" + methodName + "()", null);
            }
        });
    }

    private void runJavascriptCommand(String methodName, String[] args) {
        StringBuilder argList = new StringBuilder();
        for(int i = 0; i < args.length - 1; i++) {
            argList.append("'");
            argList.append(args[i]);
            argList.append("', ");
        }
        argList.append("'");
        argList.append(args[args.length - 1]);
        argList.append("'");

        Log.d(TAG, "Running command: " + methodName + ", with params: " + argList);

        map.post(new Runnable() {
            @Override
            public void run() {
                map.evaluateJavascript("javascript:" + methodName + "(" + argList + ")", null);
            }
        });
    }

    @Override
    public void OnUserLocationUpdated(MapCoordinates newPosition) {
        updateUserPosition(newPosition);
    }

    private class AndroidJSListener {

        private MappedInMapEventListener listener;

        public AndroidJSListener(MappedInMapEventListener listener) {
            this.listener = listener;
        }
        @JavascriptInterface
        public void mapClicked(String name, double lat, double lng) {
            MapCoordinates clicked = new MapCoordinates(lat, lng, name);

            listener.mapClicked(clicked);
        }

        @JavascriptInterface
        public void mapReady() {
            listener.mapLoaded();
        }
    }

    public interface MappedInMapEventListener {
        void mapLoaded();

        void mapClicked(MapCoordinates coords);
    }
}