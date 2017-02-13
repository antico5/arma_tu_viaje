package andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.dao.ViajeDAO;
import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo.Viaje;

public class MainActivity extends AppCompatActivity {

    private ViajeDAO dao;
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista = (ListView) findViewById(R.id.listaViajes);
        Button btnNuevoViaje = (Button) findViewById(R.id.btnNuevoViaje);
        dao = new ViajeDAO(this);

        llenarLista();

        btnNuevoViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NuevoViajeActivity.class);
                startActivity(i);
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
            Viaje viaje= this.getItem(position);

            TextView nombre = (TextView)row.findViewById(R.id.nombreViaje);
            Button btnEliminar = (Button)row.findViewById(R.id.btnEliminarViaje);

            nombre.setText(viaje.getNombre());
            row.setTag(viaje.getId());
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = (int) v.getTag();
                    Intent i = new Intent(MainActivity.this, DetalleViajeActivity.class);
                    i.putExtra("id_viaje", id);
                    startActivity(i);
                }
            });
            btnEliminar.setTag(viaje);
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Viaje viaje = (Viaje) v.getTag();
                    dao.eliminarViaje(viaje.getId());
                    llenarLista();
                }
            });

            return(row);
        }


    }
}
