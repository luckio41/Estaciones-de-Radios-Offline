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
            "North TEXT, " +
            "East TEXT, " +
            "South TEXT, " +
            "West TEXT, " +
            "FOREIGN KEY(ID_Stations) REFERENCES Stations(ID));";

    private String sqlInsert(String name, String desc){

        return "INSERT INTO Stations (ID, Name, Description)" +
                "VALUES(null, '" + name + "', '" + desc + "')";
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
        db.execSQL(sqlInsert("Bío Bío La Radio", ""));
        db.execSQL(sqlInsert("ADN Radio", ""));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
