package andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Armando on 1/31/2017.
 */

public class Lugar {
    private String nombre;
    private double longitud;
    private double latitud;
    private double rating;


    public Lugar(JSONObject item){
        try {
            JSONObject venue = item.getJSONObject("venue");
            nombre = venue.getString("name");
            rating = venue.getDouble("rating");
            JSONObject location = venue.getJSONObject("location");
            longitud = location.getDouble("lng");
            latitud = location.getDouble("lat");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString(){
        return "[Nombre: " + nombre + ", Lat: " + latitud + ", Long: " + longitud + ", Rating: " + rating + "]";
    }

}
