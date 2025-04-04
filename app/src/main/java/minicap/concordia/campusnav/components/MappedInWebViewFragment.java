package minicap.concordia.campusnav.components;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import minicap.concordia.campusnav.BuildConfig;
import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.helpers.HTMLAssetHelper;
import minicap.concordia.campusnav.map.MapCoordinates;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MappedInWebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MappedInWebViewFragment extends Fragment {

    private static final String MAPPED_IN_ASSET_NAME = "mappedIn.html";
    private static final String MAPPED_IN_KEY_STRING = "<MAPPED_IN_KEY>";
    private static final String MAPPED_IN_SECRET_STRING = "<MAPPED_IN_SECRET>";
    private static final String TAG = "MappedInWebViewFragment";

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

    private class AndroidJSListener {

        private MappedInMapEventListener listener;

        public AndroidJSListener(MappedInMapEventListener listener) {
            this.listener = listener;
        }
        @JavascriptInterface
        public void mapClicked(String name, double lat, double lng) {
            MapCoordinates clicked = new MapCoordinates(lat, lng);

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