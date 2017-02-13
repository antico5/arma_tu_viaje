package andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.dao.ViajeDAO;
import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo.Viaje;

public class NuevoViajeActivity extends AppCompatActivity {

    EditText editNombre;
    Button btnAceptar;
    Button btnVolver;
    ViajeDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_viaje);

        editNombre = (EditText) findViewById(R.id.editNombre);
        btnAceptar = (Button) findViewById(R.id.btnAceptar);
        btnVolver = (Button) findViewById(R.id.btnVolver);
        dao = new ViajeDAO(this);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreViaje = editNombre.getText().toString();
                Viaje viaje = new Viaje();
                viaje.setNombre(nombreViaje);
                int id = dao.guardarViaje(viaje);
                Intent i = new Intent(NuevoViajeActivity.this, DetalleViajeActivity.class);
                i.putExtra("id_viaje", id);
                startActivity(i);
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
