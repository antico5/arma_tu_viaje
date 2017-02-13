package andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo.Lugar;
import andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.modelo.Viaje;

/**
 * Created by Armando on 2/1/2017.
 */

public class ViajeDAO {
    private ViajeOpenHelper helper;
    SQLiteDatabase db;

    public ViajeDAO(Context c) {
        this.helper = new ViajeOpenHelper(c);
    }

    public void open() {
        this.open(false);
    }

    public void open(Boolean toWrite) {
        if (toWrite) {
            db = helper.getWritableDatabase();
        } else {
            db = helper.getReadableDatabase();
        }
    }

    public void close(){
        db.close();
    }

    public Viaje buscarViaje(Integer id){
        open();
        Viaje viaje = new Viaje();
        String sql = "select _id, NOMBRE from VIAJE where _id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{id.toString()});
        cursor.moveToFirst();
        viaje.setId(cursor.getInt(0));
        viaje.setNombre(cursor.getString(1));
        Log.i("Buscado viaje", viaje.toString());
        close();
        return viaje;
    }

    public List<Viaje> listarViajes(){
        open();
        ArrayList<Viaje> viajes = new ArrayList<Viaje>();
        String sql = "select _id, NOMBRE from VIAJE";
        Cursor cursor = db.rawQuery(sql,null);
        while(cursor.moveToNext()){
            Viaje viaje = new Viaje();
            viaje.setId(cursor.getInt(0));
            viaje.setNombre(cursor.getString(1));
            viajes.add(viaje);
        }
        Log.i("Lista viajes", viajes.toString());
        close();
        return viajes;
    }

    public int guardarViaje(Viaje viaje){
        open(true);
        ContentValues values = new ContentValues();
        values.put("NOMBRE", viaje.getNombre());
        long id = db.insert("VIAJE", null, values);
        Log.i("Nuevo viaje", "" + id);
        close();
        return (int)id;
    }

    public void eliminarViaje(Integer id){
        open(true);
        db.delete("VIAJE", "_id = ?", new String[]{id.toString()});
        db.delete("LUGAR", "ID_VIAJE = ?", new String[]{id.toString()});
        Log.i("Eliminado viaje", "ID: " + id);
        close();
    }

    public int guardarLugar(Lugar lugar){
        open(true);
        ContentValues values = new ContentValues();
        values.put("NOMBRE", lugar.getNombre());
        values.put("LATITUD", lugar.getLatitud());
        values.put("LONGITUD", lugar.getLongitud());
        values.put("DESCRIPCION", lugar.getDescripcion());
        values.put("RATING", lugar.getRating());
        values.put("ID_VIAJE", lugar.getIdViaje());
        values.put("TIPO", lugar.getTipo());
        long id = db.insert("LUGAR", null, values);
        Log.i("Nuevo lugar", "" + id);
        close();
        return (int)id;
    }

    public void eliminarLugar(Integer idLugar){
        open(true);
        db.delete("LUGAR", "_id = ?", new String[]{idLugar.toString()});
        Log.i("Eliminado lugar", "ID: " + idLugar);
        close();
    }

    public List<Lugar> listarLugares(Integer idViaje){
        open();
        ArrayList<Lugar> lugares= new ArrayList<Lugar>();
        String sql = "select _id, NOMBRE, LATITUD, LONGITUD, DESCRIPCION, RATING, ID_VIAJE, TIPO from LUGAR where ID_VIAJE = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{idViaje.toString()});
        while(cursor.moveToNext()){
            Lugar lugar = new Lugar();
            lugar.setId(cursor.getInt(0));
            lugar.setNombre(cursor.getString(1));
            lugar.setLatitud(cursor.getDouble(2));
            lugar.setLongitud(cursor.getDouble(3));
            lugar.setDescripcion(cursor.getString(4));
            lugar.setRating(cursor.getDouble(5));
            lugar.setIdViaje(cursor.getInt(6));
            lugar.setTipo(cursor.getString(7));
            lugares.add(lugar);
        }
        Log.i("Lista lugares", lugares.toString());
        close();
        return lugares;
    }
}
