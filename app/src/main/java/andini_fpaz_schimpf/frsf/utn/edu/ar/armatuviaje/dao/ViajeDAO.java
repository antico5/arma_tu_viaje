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
        Log.i("Eliminado viaje", "ID: " + id);
        close();
    }
}
