package andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.dao.ViajeDAO;
import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo.Viaje;

public class MainActivity extends AppCompatActivity {

    public static int CURSOR_VIAJE = 1;

    private ViajeDAO dao;
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);

        //Definir las animaciones si la api es mayor a la 21
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            Explode transition = new Explode();
            View actionBar = getWindow().getDecorView().findViewById(R.id.action_bar_container);
            transition.excludeTarget(actionBar, true);
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            transition.excludeTarget(android.R.id.navigationBarBackground, true);
            transition.setDuration(500);

            getWindow().setExitTransition(transition);
        }

        lista = (ListView) findViewById(R.id.listaViajes);
        FloatingActionButton btnNuevoViaje = (FloatingActionButton) findViewById(R.id.btnNuevoViaje);
        dao = new ViajeDAO(this);

        llenarLista();

        btnNuevoViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NuevoViajeActivity.class);
                Bundle bundle = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                } else {
                    bundle = new Bundle();
                }
                startActivity(i, bundle);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        llenarLista();
    }

    private void llenarLista(){
        ViajesAdapter adapter = new ViajesAdapter(getApplicationContext(), dao.listarViajes());
        lista.setAdapter(adapter);
    }

    public class ViajesAdapter extends ArrayAdapter<Viaje> {
        LayoutInflater inflater;

        ViajesAdapter(Context context, List<Viaje> items) {
            super(context, R.layout.item_viaje, items);
            inflater = LayoutInflater.from(context);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View row = inflater.inflate(R.layout.item_viaje, parent, false);
            final Viaje viaje= this.getItem(position);

            TextView nombre = (TextView)row.findViewById(R.id.nombreViaje);
            TextView infoViaje = (TextView)row.findViewById(R.id.infoViaje);
            ImageButton btnEliminar = (ImageButton)row.findViewById(R.id.btnEliminarViaje);

            nombre.setText(viaje.getNombre());
            infoViaje.setText(dao.listarLugares(viaje.getId()).size() + " lugares seleccionados");

            row.setTag(viaje.getId());
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = (int) v.getTag();
                    Intent i = new Intent(MainActivity.this, DetalleViajeActivity.class);
                    i.putExtra("id_viaje", id);
                    Bundle bundle = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                    } else {
                        bundle = new Bundle();
                    }
                    startActivity(i, bundle);
                }
            });
            btnEliminar.setTag(viaje);
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final Viaje viaje = (Viaje) v.getTag();

                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.AppTheme));
                    builder.setTitle("Eliminar viaje");
                    builder.setMessage("¿Está seguro que desea eliminar el viaje?");
                    builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dao.eliminarViaje(viaje.getId());
                            llenarLista();
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            return(row);
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent i = new Intent(MainActivity.this, NuevoViajeActivity.class);
                Bundle bundle = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                } else {
                    bundle = new Bundle();
                }
                startActivity(i, bundle);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
