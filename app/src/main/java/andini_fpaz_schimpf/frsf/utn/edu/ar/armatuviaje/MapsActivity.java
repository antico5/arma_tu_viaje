package andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.dao.ViajeDAO;
import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo.FSQueryParams;
import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo.Lugar;
import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo.ResultadoBusqueda;
import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo.Viaje;
import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo.ZoomToRadius;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FloatingActionButton fab;
    private ViajeDAO dao;
    private Lugar lugarSeleccionado;
    private Marker markerSeleccionado;
    private Viaje viaje;
    private boolean listarLugares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Definir las animaciones si la api es mayor a la 21
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            getWindow().setAllowEnterTransitionOverlap(false);

            Fade transition = new Fade(Fade.IN);
            View actionBar = getWindow().getDecorView().findViewById(R.id.action_bar_container);
            transition.excludeTarget(actionBar, true);
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            transition.excludeTarget(android.R.id.navigationBarBackground, true);
            transition.setDuration(500);

            getWindow().setEnterTransition(transition);
            transition.setDuration(100);
            getWindow().setReturnTransition(transition);
            getWindow().setExitTransition(transition);

        }

        setContentView(R.layout.activity_maps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fabAgregar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        dao = new ViajeDAO(this);
        Intent intent = getIntent();
        viaje = dao.buscarViaje(intent.getIntExtra("id_viaje", 1));
        listarLugares = intent.getBooleanExtra("listar", false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lugarSeleccionado.setIdViaje(viaje.getId());
                dao.guardarLugar(lugarSeleccionado);
                toast("Se agrego " + lugarSeleccionado.getNombre() + " al viaje.");
                markerSeleccionado.hideInfoWindow();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent upIntent = NavUtils.getParentActivityIntent(this);
//                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
//                    TaskStackBuilder.create(this)
//                            .addNextIntentWithParentStack(upIntent)
//                            .startActivities();
//                } else {
//                    NavUtils.navigateUpTo(this, upIntent);
//                }
                finish();
                overridePendingTransition(R.xml.fade_in, R.xml.right_slide_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

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

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        UiSettings settings = mMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
        settings.setMapToolbarEnabled(false);

        setLugaresActuales();


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng coordenadas) {
                Log.i("Coordenadas", coordenadas.toString());
                int radius = ZoomToRadius.calcular(mMap.getCameraPosition().zoom);
                FSQueryParams params = new FSQueryParams(coordenadas.latitude, coordenadas.longitude, radius);

                mMap.clear();
                setLugaresActuales();

                new RequestTask().execute(params);

                mMap.addCircle(new CircleOptions()
                        .center(coordenadas)
                        .radius(radius)
                        .strokeColor(Color.BLUE)
                        .strokeWidth(2));
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.hideInfoWindow();
            }
        });

        googleMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
            @Override
            public void onInfoWindowClose(Marker marker) {
                lugarSeleccionado = null;
                fab.hide();
            }
        });

        mMap.setInfoWindowAdapter(new CustomInfoWindow());


    }

    private void setLugaresActuales() {
        if(!listarLugares) {
            return;
        }
        List<Lugar> lista = dao.listarLugares(viaje.getId());
        for(Lugar lugar : lista) {
            LatLng coordenadas = new LatLng(lugar.getLatitud(), lugar.getLongitud());
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(coordenadas)
                    .title(lugar.getNombre())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            marker.setTag(lugar);
        }
    }

    private class RequestTask extends AsyncTask<FSQueryParams, Void, String> {
        @Override
        protected String doInBackground(FSQueryParams... _params) {
            try {
                FSQueryParams params = _params[0];
                double latitud = params.getLatitud();
                double longitud = params.getLongitud();
                int radio = params.getRadio();

                StringBuilder urlBuilder = new StringBuilder();
                urlBuilder.append("https://api.foursquare.com/v2/venues/explore?");
                urlBuilder.append("client_id=" + getString(R.string.fs_client_id));
                urlBuilder.append("&client_secret=" + getString(R.string.fs_client_secret));
                urlBuilder.append("&v=20130815");
                urlBuilder.append("&ll=" + latitud + "," + longitud);
                urlBuilder.append("&limit=50");
                urlBuilder.append("&radius=" + radio);

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
                        Marker marker = mMap.addMarker(new MarkerOptions().position(coordenadas).title(lugar.getNombre()));
                        marker.setTag(lugar);
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

    private class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

        // Use default InfoWindow frame
        @Override
        public View getInfoWindow(Marker arg0) {
            return null;
        }

        // Defines the contents of the InfoWindow
        @Override
        public View getInfoContents(Marker marker) {
            Lugar lugar = (Lugar) marker.getTag();
            lugarSeleccionado = lugar;
            markerSeleccionado = marker;

            View v = getLayoutInflater().inflate(R.layout.marker_info_layout, null);
            v.setClickable(false);

            TextView nombre = (TextView) v.findViewById(R.id.tNombre);
            TextView tipo = (TextView) v.findViewById(R.id.tTipo);
            TextView descripcion = (TextView) v.findViewById(R.id.tDescripcion);
            TextView rating = (TextView) v.findViewById(R.id.tRating);

            //RatingBar ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);

            // ratingBar.setMax(10);
            // ratingBar.setRating((float)lugar.getRating());

            nombre.setText(lugar.getNombre());
            tipo.setText(lugar.getTipo());
            rating.setText("" + lugar.getRating());
            descripcion.setText(lugar.getDescripcion());

            fab.show();

            return v;
        }
    }

    private void toast(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
