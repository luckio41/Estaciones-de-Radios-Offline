package cl.luckio.estacionesderadiosoffline;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Luckio on 06/05/2016.
 */
public class SqlHelper extends SQLiteOpenHelper {

    String sqlStations = "CREATE TABLE Stations (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Name TEXT, " +
            "Description TEXT);";

    String sqlCities = "CREATE TABLE Cities (" +
            "ID_Cities INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "ID_Stations INTEGER, " +
            "Name_city TEXT, " +
            "Frequency TEXT, " +
            "North DOUBLE, " +
            "East DOUBLE, " +
            "South DOUBLE, " +
            "West DOUBLE, " +
            "FOREIGN KEY(ID_Stations) REFERENCES Stations(ID));";

    private String sqlInsertStatios(String name, String desc){

        return "INSERT INTO Stations (ID, Name, Description)" +
                "VALUES(null, '" + name + "', '" + desc + "')";
    }

    private String sqlInsertCities(int idStations, String name, String frequency, double north, double east, double south, double west){

        return "INSERT INTO Cities (ID_Cities, ID_Stations, Name_city, Frequency, North, East, South, West)" +
                "VALUES (null, "+idStations+", '"+name+"', '"+frequency+"', "+north+", "+east+", "+south+", "+west+")";
    }

    public SqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(sqlStations);
        db.execSQL(sqlCities);

        // Populate table "Stations"
        db.execSQL(sqlInsertStatios("Bío Bío La Radio", ""));
        db.execSQL(sqlInsertStatios("ADN Radio", ""));

        // Populate table "Cities [Bio Bio]"
        db.execSQL(sqlInsertCities(1, "Concepción y Talcahuano", "98.1 fm - 620 am", -36.376573, -72.665415, -36.986187, -73.216974));

        // Populate table "Cities [ADN"
        db.execSQL(sqlInsertCities(2, "Concepción y Talcahuano", "104.1", -36.376573, -72.665415, -36.986187, -73.216974));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
