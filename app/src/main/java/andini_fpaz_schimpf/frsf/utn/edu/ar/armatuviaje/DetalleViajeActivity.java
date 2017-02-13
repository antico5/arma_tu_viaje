package andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.dao.ViajeDAO;
import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo.Lugar;
import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo.Viaje;

public class DetalleViajeActivity extends AppCompatActivity {

    Button btnAgregarDestino;
    TextView tNombreViaje;
    Viaje viaje;
    ViajeDAO dao;
    ListView listaLugares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_viaje);

        btnAgregarDestino = (Button) findViewById(R.id.btnAgregarDestinos);
        tNombreViaje = (TextView) findViewById(R.id.tNombreViaje);
        dao = new ViajeDAO(this);
        viaje = dao.buscarViaje(getIntent().getIntExtra("id_viaje",1));
        listaLugares = (ListView) findViewById(R.id.listaLugares);

        tNombreViaje.setText(viaje.getNombre());

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

            Button btnEliminar = (Button)row.findViewById(R.id.btnEliminarLugar);


            nombre.setText(lugar.getNombre());
            tipo.setText(lugar.getTipo());
            descripcion.setText(lugar.getDescripcion());
            ratingBar.setRating((float) lugar.getRating());

            btnEliminar.setTag(lugar.getId());
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = (int) v.getTag();
                    dao.eliminarLugar(id);
                    llenarLista();
                }
            });

            return(row);
        }


    }
}
