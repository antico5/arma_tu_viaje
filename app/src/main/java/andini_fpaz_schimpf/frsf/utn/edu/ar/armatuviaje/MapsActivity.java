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

import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo.Lugar;
import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo.ResultadoBusqueda;


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
                mMap.clear();
                new RequestTask().execute(coordenadas.latitude, coordenadas.longitude);

                mMap.addCircle(new CircleOptions()
                        .center(coordenadas)
                        .radius(15000)
                        .strokeColor(Color.YELLOW)
                        .strokeWidth(2));
            }
        });


    }

    private class RequestTask extends AsyncTask<Double, Void, String> {
        @Override
        protected String doInBackground(Double... params) {
            try {
                double latitud = params[0];
                double longitud = params[1];

                StringBuilder urlBuilder = new StringBuilder();
                urlBuilder.append("https://api.foursquare.com/v2/venues/explore?");
                urlBuilder.append("client_id=" + getString(R.string.fs_client_id));
                urlBuilder.append("&client_secret=" + getString(R.string.fs_client_secret));
                urlBuilder.append("&v=20130815");
                urlBuilder.append("&ll=" + latitud + "," + longitud);
                urlBuilder.append("&limit=50");
                urlBuilder.append("&radius=15000");
                urlBuilder.append("&section=topPicks");

                Log.d("Request URL", urlBuilder.toString());

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
            final ResultadoBusqueda busqueda = new ResultadoBusqueda(result);
            Log.i("Resultado", busqueda.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(Lugar lugar : busqueda.getLugares()){
                        LatLng coordenadas = new LatLng(lugar.getLatitud(), lugar.getLongitud());
                        mMap.addMarker(new MarkerOptions().position(coordenadas).title(lugar.getNombre()));
                    }
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
