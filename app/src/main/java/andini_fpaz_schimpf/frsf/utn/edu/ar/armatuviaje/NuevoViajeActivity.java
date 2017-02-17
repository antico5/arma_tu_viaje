package andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide exitTransition = new Slide(Gravity.RIGHT);
            View actionBar = getWindow().getDecorView().findViewById(R.id.action_bar_container);
            exitTransition.excludeTarget(actionBar, true);
            exitTransition.excludeTarget(android.R.id.statusBarBackground, true);
            exitTransition.excludeTarget(android.R.id.navigationBarBackground, true);

            getWindow().setExitTransition(exitTransition);
            getWindow().setReturnTransition(exitTransition);
            getWindow().setEnterTransition(exitTransition);
        }

        setContentView(R.layout.activity_nuevo_viaje);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editNombre = (EditText) findViewById(R.id.editNombre);
        btnAceptar = (Button) findViewById(R.id.btnAceptar);
        btnVolver = (Button) findViewById(R.id.btnVolver);
        dao = new ViajeDAO(this);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreViaje = editNombre.getText().toString();
                if(nombreViaje.isEmpty()) {
                    editNombre.setError("Completa éste campo");
                    return;
                }
                Viaje viaje = new Viaje();
                viaje.setNombre(nombreViaje);
                int id = dao.guardarViaje(viaje);
                Intent i = new Intent(NuevoViajeActivity.this, DetalleViajeActivity.class);
                i.putExtra("id_viaje", id);
//                startActivity(i);
                startActivityForResult(i,1, setAnimations());
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Bundle setAnimations() {
        Bundle bundle = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            bundle = ActivityOptions.makeSceneTransitionAnimation(NuevoViajeActivity.this).toBundle();
        } else {
            bundle = new Bundle();
        }
        return bundle;
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
                overridePendingTransition(R.xml.fade_in, R.xml.right_slide_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}
