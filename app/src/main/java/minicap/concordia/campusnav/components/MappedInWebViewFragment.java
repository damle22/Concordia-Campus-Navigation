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

import minicap.concordia.campusnav.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MappedInWebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MappedInWebViewFragment extends Fragment {

    WebView map;

    public MappedInWebViewFragment() {
        // Required empty public constructor
    }

    public static MappedInWebViewFragment newInstance() {
        MappedInWebViewFragment fragment = new MappedInWebViewFragment();
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
            public void onGeolocationPermissionShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

        map.addJavascriptInterface(new AndroidJSListener(), "Android");

        String unencodedHtml = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\t<head>\n" +
                "\t\t<link href=\"https://cdn.jsdelivr.net/npm/@mappedin/mappedin-js@beta/lib/index.css\" rel=\"stylesheet\" />\n" +
                "\t\t<style>\n" +
                "\t\t\thtml, body {\n" +
                "\t\t\t\theight: 100%;\n" +
                "\t\t\t\tmargin: 0;\n" +
                "\t\t\t}\n" +
                "\t\t\t\n" +
                "\t\t\t.full-screen {\n" +
                "\t\t\t\theight: 100%;\n" +
                "\t\t\t}\n" +
                "\t\t\t\n" +
                "\t\t\t.marker {\n" +
                "\t\t\t  border-radius: 10px;\t\n" +
                "\t\t\t  border: 1px solid #293136;\n" +
                "\t\t\t\t\t  display: flex;\n" +
                "\t\t\t\t\t  align-items: center;\n" +
                "\t\t\t\t\t  background-color: #fff;\n" +
                "\t\t\t\t\t  padding: 3px;\n" +
                "\t\t\t\t\t  font-family: sans-serif;\n" +
                "\t\t\t\tbox-shadow: 0px 0px 1px rgba(0, 0, 0, 0.25);\n" +
                "            }\n" +
                "\n" +
                "\t\t\t.marker:before {\n" +
                "\t\t\t\tcontent: '';\n" +
                "\t\t\t\twidth: 0;\n" +
                "\t\t\t\theight: 0;\n" +
                "\t\t\t\ttop: calc(50% - 10px);\n" +
                "\t\t\t\tleft: -10px;\n" +
                "\t\t\t\tz-index: 1;\n" +
                "\t\t\t\tposition: absolute;\n" +
                "\t\t\t\tborder-bottom: 10px solid transparent;\n" +
                "\t\t\t\tborder-top: 10px solid transparent;\n" +
                "\t\t\t\tz-index: -1;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.mappedin-marker[data-anchor=\"left\"] .marker:before {\n" +
                "\t\t\t\tleft: auto;\n" +
                "\t\t\t\tright: -5px;\n" +
                "\t\t\t\tborder-left: 10px solid #333333;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.mappedin-marker[data-anchor=\"right\"] .marker:before {\n" +
                "\t\t\t\tleft: -5px;\n" +
                "\t\t\t\tright: auto;\n" +
                "\t\t\t\tborder-right: 10px solid #333333;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.mappedin-marker[data-anchor=\"top\"] .marker:before {\n" +
                "\t\t\t\tleft: calc(50% - 10px);\n" +
                "\t\t\t\ttop: calc(100% - 5px);\n" +
                "\t\t\t\tright: auto;\n" +
                "\t\t\t\tborder-bottom: 10px solid transparent;\n" +
                "\t\t\t\tborder-top: 10px solid #333333;\n" +
                "\t\t\t\tborder-left: 10px solid transparent;\n" +
                "\t\t\t\tborder-right: 10px solid transparent;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.mappedin-marker[data-anchor=\"bottom\"] .marker:before {\n" +
                "\t\t\t\tleft: calc(50% - 10px);\n" +
                "\t\t\t\ttop: -15px;\n" +
                "\t\t\t\tright: auto;\n" +
                "\t\t\t\tborder-bottom: 10px solid #333333;\n" +
                "\t\t\t\tborder-top: 10px solid transparent;\n" +
                "\t\t\t\tborder-left: 10px solid transparent;\n" +
                "\t\t\t\tborder-right: 10px solid transparent;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.mappedin-marker[data-anchor=\"bottom-left\"] .marker:before {\n" +
                "\t\t\t\tleft: auto;\n" +
                "\t\t\t\tright: -10px;\n" +
                "\t\t\t\ttop: -10px;\n" +
                "\t\t\t\tborder-bottom: 10px solid #333333;\n" +
                "\t\t\t\tborder-top: 10px solid transparent;\n" +
                "\t\t\t\tborder-left: 10px solid transparent;\n" +
                "\t\t\t\tborder-right: 10px solid transparent;\n" +
                "\t\t\t\ttransform: rotate(45deg);\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.mappedin-marker[data-anchor=\"bottom-right\"] .marker:before {\n" +
                "\t\t\t\tleft: -10px;\n" +
                "\t\t\t\tright: auto;\n" +
                "\t\t\t\ttop: -10px;\n" +
                "\t\t\t\tborder-bottom: 10px solid #333333;\n" +
                "\t\t\t\tborder-top: 10px solid transparent;\n" +
                "\t\t\t\tborder-left: 10px solid transparent;\n" +
                "\t\t\t\tborder-right: 10px solid transparent;\n" +
                "\t\t\t\ttransform: rotate(-45deg);\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.mappedin-marker[data-anchor=\"top-left\"] .marker:before {\n" +
                "\t\t\t\tleft: auto;\n" +
                "\t\t\t\tright: -10px;\n" +
                "\t\t\t\ttop: calc(100% - 10px);\n" +
                "\t\t\t\tborder-bottom: 10px solid transparent;\n" +
                "\t\t\t\tborder-top: 10px solid #333333;\n" +
                "\t\t\t\tborder-left: 10px solid transparent;\n" +
                "\t\t\t\tborder-right: 10px solid transparent;\n" +
                "\t\t\t\ttransform: rotate(-45deg);\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.mappedin-marker[data-anchor=\"top-right\"] .marker:before {\n" +
                "\t\t\t\tleft: -10px;\n" +
                "\t\t\t\tright: auto;\n" +
                "\t\t\t\ttop: calc(100% - 10px);\n" +
                "\t\t\t\tborder-bottom: 10px solid transparent;\n" +
                "\t\t\t\tborder-top: 10px solid #333333;\n" +
                "\t\t\t\tborder-left: 10px solid transparent;\n" +
                "\t\t\t\tborder-right: 10px solid transparent;\n" +
                "\t\t\t\ttransform: rotate(45deg);\n" +
                "\t\t\t}\n" +
                "\t\t</style>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\t<div id=\"mappedin-map\" class=\"full-screen\" >\n" +
                "\t\t<script type=\"module\" >\n" +
                "\t\t\timport { getMapData, show3dMap } from 'https://cdn.jsdelivr.net/npm/@mappedin/mappedin-js@beta/lib/esm/index.js';\n" +
                "\t\t\tvar mapView;\n" +
                "\t\t\tvar mapData;\n" +
                "\t\t\t\n" +
                "\t\t\tconst options = {\n" +
                "\t\t\t\tkey: \"mik_5uAk35rZYqcWpSJRR528c0760\",\n" +
                "\t\t\t\tsecret: \"mis_g78fKHSHWKFM91AdeHNOikvShotU3KwABDenGzavW5m79f50ffe\",\n" +
                "\t\t\t\tmapId: \"67df02d0aa7c59000baf8d83\"\n" +
                "\t\t\t};\n" +
                "\t\t\t\n" +
                "\t\t\tgetMapData(options).then(async (data) => {\n" +
                "\t\t\t\tmapView = await show3dMap(document.getElementById('mappedin-map'), data);\n" +
                "\t\t\t\tmapData = data;\n" +
                "\t\t\t\tmapView.Labels.all();\n" +
                "\t\t\t\t\n" +
                "\t\t\t\tmapData.getByType('space').forEach(space => {\n" +
                "\t\t\t\t\tmapView.updateState(space, {\n" +
                "\t\t\t\t\t\tinteractive: true,\n" +
                "\t\t\t\t\t\thoverColor: '#f26336',\n" +
                "\t\t\t\t\t});\n" +
                "\t\t\t\t});\n" +
                "\t\t\t\t\n" +
                "\t\t\t\tmapView.on('click', async (e) => {\n" +
                "\t\t\t\t\tmapClicked(e)\n" +
                "\t\t\t\t});\n" +
                "\t\t\t\t\n" +
                "\t\t\t\tmapView.BlueDot.enable();\n" +
                "\t\t\t\tAndroid.mapReady();\n" +
                "\t\t\t\t\n" +
                "\t\t\t});\n" +
                "\t\t\t\n" +
                "\t\t\tfunction mapClicked(e) {\n" +
                "\t\t\t\tvar name = \"Location\"\n" +
                "\t\t\t\tif(e.spaces[0]?.name) {\n" +
                "\t\t\t\t\tname = e.spaces[0].name\n" +
                "\t\t\t\t}\t\t\t\t\n" +
                "\t\t\t\t\n" +
                "\t\t\t\tAndroid.mapClicked(name, e.coordinate.latitude, e.coordinate.longitude);\n" +
                "\t\t\t\taddMarker(e.coordinate, name);\n" +
                "\t\t\t\tdrawPath('H196', 'H103');\n" +
                "\t\t\t}\n" +
                "\t\t\t\n" +
                "\t\t\tfunction addMarker(coordinate, name) {\n" +
                "\t\t\t\tconst markerContent = '<div class=\"marker\">' + name + '</div>'\n" +
                "\t\t\t\tmapView.Markers.add(coordinate, markerContent)\n" +
                "\t\t\t}\n" +
                "\t\t\t\n" +
                "\t\t\tfunction drawPath(start, end) {\n" +
                "\t\t\t\tconst firstSpace = mapData.getByType('space').find((s) => s.name === start)\n" +
                "\t\t\t\tconst secondSpace = mapData.getByType('space').find((s) => s.name === end)\n" +
                "\t\t\t\t\n" +
                "\t\t\t\tif(firstSpace && secondSpace) {\n" +
                "\t\t\t\t\talert(\"found first and second space\");\n" +
                "\t\t\t\t\tconst dir = mapData.getDirections(firstSpace, secondSpace);\n" +
                "\t\t\t\t\t\n" +
                "\t\t\t\t\t\n" +
                "\t\t\t\t\tif(dir) {\n" +
                "\t\t\t\t\t\talert(\"Found dir\");\n" +
                "\t\t\t\t\t\tmapView.Navigation.draw(dir);\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t</script>\n" +
                "\t\t</div>\n" +
                "\t</body>\n" +
                "</html>";
        String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(), Base64.NO_PADDING);

        map.loadData(encodedHtml, "text/html", "base64");
    }

    private class AndroidJSListener {
        @JavascriptInterface
        public void mapClicked(String name, double lat, double lng) {
            Log.d("MappedInWebview", "Map was clicked");
        }

        @JavascriptInterface
        public void mapReady() {
            Log.d("MappedInWebview", "Map is ready for use");
        }
    }
}