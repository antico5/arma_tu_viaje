package andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Armando on 1/31/2017.
 */

public class Lugar {
    private int id;
    private String nombre;
    private double longitud;
    private double latitud;
    private double rating;
    private String tipo;
    private String descripcion;

    public Lugar() {
        nombre = "";
        tipo = "";
        descripcion = "";
    }

    public Lugar(JSONObject item){
        try {
            JSONObject venue = item.getJSONObject("venue");
            JSONArray tips = item.getJSONArray("tips");
            if(tips.length() > 0)
                descripcion = tips.getJSONObject(0).getString("text");
            nombre = venue.getString("name");
            rating = venue.getDouble("rating");
            JSONObject location = venue.getJSONObject("location");
            longitud = location.getDouble("lng");
            latitud = location.getDouble("lat");
            JSONArray categories = venue.getJSONArray("categories");
            JSONObject category = categories.getJSONObject(0);
            tipo = category.getString("name");

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString(){
        return "[Nombre: " + nombre + ", Lat: " + latitud + ", Long: " + longitud + ", Rating: " + rating + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
