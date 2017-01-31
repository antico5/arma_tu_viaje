package andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo;

/**
 * Created by Armando on 1/31/2017.
 */

public class Query {
    private double latitud;
    private double longitud;
    private int radio;

    public Query(double _latitud, double _longitud, int _radio){
        latitud = _latitud;
        longitud = _longitud;
        radio = _radio;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getRadio() {
        return radio;
    }

    public void setRadio(int radio) {
        this.radio = radio;
    }
}
