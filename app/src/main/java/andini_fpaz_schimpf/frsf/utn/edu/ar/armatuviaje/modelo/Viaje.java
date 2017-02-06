package andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo;

/**
 * Created by Armando on 2/1/2017.
 */

public class Viaje {
    private int id;
    private String nombre;

    public Viaje(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Viaje{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
