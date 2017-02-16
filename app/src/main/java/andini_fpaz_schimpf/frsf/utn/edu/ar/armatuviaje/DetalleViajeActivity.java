package andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.dao.ViajeDAO;
import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo.Lugar;
import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo.Viaje;

public class DetalleViajeActivity extends AppCompatActivity {

    FloatingActionButton btnAgregarDestino;
    TextView tNombreViaje;
    Viaje viaje;
    ViajeDAO dao;
    ListView listaLugares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_viaje);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnAgregarDestino = (FloatingActionButton) findViewById(R.id.btnAgregarDestinos);
        dao = new ViajeDAO(this);
        viaje = dao.buscarViaje(getIntent().getIntExtra("id_viaje",1));
        listaLugares = (ListView) findViewById(R.id.listaLugares);

        setTitle(viaje.getNombre());

        btnAgregarDestino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetalleViajeActivity.this, MapsActivity.class);
                i.putExtra("id_viaje", viaje.getId());
                startActivity(i);
            }
        });

        llenarLista();
    }

    @Override
    protected void onResume() {
        super.onResume();
        llenarLista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detalle_viaje_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            case R.id.action_add:
                Intent i = new Intent(DetalleViajeActivity.this, MapsActivity.class);
                i.putExtra("id_viaje", viaje.getId());
                startActivity(i);
                return true;
            case R.id.acrion_share:
                String textoCompartir = "Acompañame en este viaje!! \n\n" + viaje.getNombre() + ":\n";
                List<Lugar> lista = dao.listarLugares(viaje.getId());
                for(Lugar l : lista) {
                    textoCompartir += "\t-" + l.getNombre() + " (" + l.getRating() + ")\n";
                }
                textoCompartir += "\nPara saber mas ingresa en www.armatuviaje.com";

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,textoCompartir);
                startActivity(Intent.createChooser(intent, "Compartir"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void llenarLista(){
        LugaresAdapter adapter = new LugaresAdapter(getApplicationContext(), dao.listarLugares(viaje.getId()));
        listaLugares.setAdapter(adapter);
    }

    public class LugaresAdapter extends ArrayAdapter<Lugar> {
        LayoutInflater inflater;

        LugaresAdapter(Context context, List<Lugar> items) {
            super(context, R.layout.item_lugar, items);
            inflater = LayoutInflater.from(context);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View row = inflater.inflate(R.layout.item_lugar, parent, false);
            Lugar lugar= this.getItem(position);

            TextView nombre = (TextView)row.findViewById(R.id.tNombreLugar);
            TextView tipo = (TextView) row.findViewById(R.id.tTipo);
            TextView descripcion = (TextView) row.findViewById(R.id.tDescripcion);
            RatingBar ratingBar = (RatingBar) row.findViewById(R.id.ratingBar);

            ImageButton btnEliminar = (ImageButton)row.findViewById(R.id.btnEliminarLugar);


            nombre.setText(lugar.getNombre());
            tipo.setText(lugar.getTipo());
            descripcion.setText(lugar.getDescripcion());
            ratingBar.setRating((float) lugar.getRating());

            btnEliminar.setTag(lugar.getId());
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int id = (int) v.getTag();

                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(DetalleViajeActivity.this, R.style.AppTheme));
                    builder.setTitle("Eliminar lugar");
                    builder.setMessage("¿Está seguro que desea eliminar el lugar seleccionado?");
                    builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dao.eliminarLugar(id);
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
}
