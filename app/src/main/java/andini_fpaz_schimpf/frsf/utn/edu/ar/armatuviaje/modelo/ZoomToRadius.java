package andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo;

/**
 * Created by Armando on 1/31/2017.
 */

public class ZoomToRadius {
    public static int calcular(Float _zoom){
        int zoom = _zoom.intValue();
        int radius = 40000;
        if(zoom == 10)
            radius = 20000;
        if(zoom == 11)
            radius = 10000;
        else if (zoom == 12)
            radius = 5000;
        else if (zoom == 13)
            radius = 2500;
        else if (zoom == 14)
            radius = 1250;
        else if (zoom == 15)
            radius = 600;
        else if (zoom >= 16)
            radius = 300;
        return radius;
    }
}
