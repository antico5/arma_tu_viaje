package andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo;

/**
 * Created by Armando on 1/31/2017.
 */

public class ZoomToRadius {
    public static int calcular(Float zoom){
        int radius = 30000;
        if(zoom.intValue() == 10)
            radius = 20000;
        if(zoom.intValue() == 11)
            radius = 10000;
        else if (zoom.intValue() == 12)
            radius = 5000;
        else if (zoom.intValue() == 13)
            radius = 2500;
        else if (zoom.intValue() == 14)
            radius = 1250;
        else if (zoom.intValue() == 15)
            radius = 600;
        else if (zoom.intValue() >= 16)
            radius = 300;
        return radius;
    }
}
