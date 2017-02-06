package andini_fpaz_schimpf.frsf.utn.edu.ar.armatuviaje.dao;

/**
 * Created by Armando on 2/1/2017.
 */

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ViajeOpenHelper extends SQLiteOpenHelper {

    private Context context;

    public ViajeOpenHelper(Context context) {
        super(context, "arma_tu_viaje.db", null, 1);
        this.context=context;
    }

    public void onCreate(SQLiteDatabase db) {
        try {
            ArrayList<String> tablas = this.leerTablas();
            for (String sql : tablas) {
                db.execSQL(sql);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        //db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public ArrayList<String> leerTablas() throws IOException {
        InputStream is = context.getAssets().open("estructura-db.sql");
        InputStreamReader r = new InputStreamReader(is, Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(r);
        String strLine;
        ArrayList<String> res = new ArrayList<String>();
        while ((strLine = br.readLine()) != null) {
            Log.d("SQL", strLine);
            res.add(strLine);
        }
        br.close();
        r.close();
        is.close();
        return res;
    }
}