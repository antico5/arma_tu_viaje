package andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Armando on 1/31/2017.
 */

public class ResultadoBusqueda {
    private String nombreDestino;
    private ArrayList<Lugar> lugares;

    public ResultadoBusqueda(String json){
        lugares = new ArrayList<Lugar>();
        try {
            JSONObject data = new JSONObject(json);
            JSONObject response = data.getJSONObject("response");
            nombreDestino = response.getString("headerFullLocation");
            JSONArray groups = response.getJSONArray("groups");
            JSONObject recommendedPlaces = groups.getJSONObject(0);
            JSONArray items = recommendedPlaces.getJSONArray("items");
            for(int i = 0; i < items.length(); i++){
                JSONObject lugar = items.getJSONObject(i);
                lugares.add(new Lugar(lugar));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getNombreDestino() {
        return nombreDestino;
    }

    public void setNombreDestino(String nombreDestino) {
        this.nombreDestino = nombreDestino;
    }

    public ArrayList<Lugar> getLugares() {
        return lugares;
    }

    public void setLugares(ArrayList<Lugar> lugares) {
        this.lugares = lugares;
    }

    @Override
    public String toString() {
        return "ResultadoBusqueda{" +
                "lugares=" + lugares +
                ", nombreDestino='" + nombreDestino + '\'' +
                '}';
    }
}
