package andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("Log", "MAPA CARGADO");
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng santafe = new LatLng(-31.62, -60.70);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(santafe));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12));

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        UiSettings settings = mMap.getUiSettings();
        settings.setZoomControlsEnabled(true);



        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng coordenadas) {
                Log.i("Coordenadas", coordenadas.toString());
                makeRequest();
                mMap.addCircle(new CircleOptions()
                        .center(coordenadas)
                        .radius(5000)
                        .strokeColor(Color.YELLOW)
                        .strokeWidth(2));
            }
        });


    }

    private void makeRequest(){
        new RequestTask().execute("");
    }

    private class RequestTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                StringBuilder urlBuilder = new StringBuilder();
                urlBuilder.append("https://api.foursquare.com/v2/venues/explore?");
                urlBuilder.append("client_id=GLGPMI1MIF0CPYUGPXGABU1QD2FBKFCW4MBVE0L3KTTV1VYF");
                urlBuilder.append("&client_secret=IH1OIF4CXDZA54XFAV5RPFHDBH1WN1PAAMGDCDA1SWJBBKR5");
                urlBuilder.append("&v=20130815");
                urlBuilder.append("&ll=-31.623,-60.690");
                urlBuilder.append("&limit=2");

                URL url = new URL(urlBuilder.toString());
                HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                InputStreamReader isw = new InputStreamReader(in);
                StringBuilder sb = new StringBuilder();

                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    sb.append(current);
                    data = isw.read();
                }
                return sb.toString();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("Request",result);
            try {
                JSONObject data = new JSONObject(result);
                JSONObject response = data.getJSONObject("response");
                JSONArray groups = response.getJSONArray("groups");
                JSONObject recommendedPlaces = groups.getJSONObject(0);
                JSONArray items = recommendedPlaces.getJSONArray("items");
                for(int i = 0; i < items.length(); i++){
                    JSONObject lugar = items.getJSONObject(i).getJSONObject("venue");
                    Log.i("Lugar", lugar.getString("name"));
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMap.clear();
                }
            });
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
