package cl.luckio.estacionesderadiosoffline;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

/**
 * Created by Luckio on 17/05/2016.
 */
public class ResultsHelper {

    private double longitud;
    private double latitud;
    private int station_id;
    private SQLiteDatabase db;
    private SqlHelper sqlHelper;
    Context context;

    public ResultsHelper(Context context, double latitud, double longitud, int station_id) {
        this.context = context;
        this.latitud = latitud;
        this.longitud = longitud;
        this.station_id = station_id;
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

    public int getStation_id() {
        return station_id;
    }

    public void setStation_id(int station_id) {
        this.station_id = station_id;
    }

    public int calcularStatus() {
        sqlHelper = new SqlHelper(context, "ESTACIONESDB", null, 1);
        db = sqlHelper.getReadableDatabase();
        int status = 0;

        if(db != null) {
            String sql = "SELECT c.ID_Cities FROM Frequency f " +
                    "INNER JOIN Cities c " +
                    "ON f.ID_City = c.ID_Cities " +
                    "INNER JOIN Stations s " +
                    "ON f.ID_Station = s.ID " +
                    "WHERE s.ID = " + this.getStation_id() + " " +
                    "AND " + this.getLatitud() + " <= c.North " +
                    "AND " + this.getLatitud() + " >= c.South " +
                    "AND " + this.getLongitud() + " >= c.West " +
                    "AND " + this.getLongitud() + " <= c.East";

            Cursor c = db.rawQuery(sql, null);

            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {
                    status = 1;
                    c.moveToNext();
                }
            }
        }

        return status;
    }
}
