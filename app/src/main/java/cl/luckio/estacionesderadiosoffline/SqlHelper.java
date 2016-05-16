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
            "Name TEXT);";

    String sqlCities = "CREATE TABLE Cities (" +
            "ID_Cities INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Name_city TEXT, " +
            "North DOUBLE, " +
            "East DOUBLE, " +
            "South DOUBLE, " +
            "West DOUBLE );";

    String sqlFrequency = "CREATE TABLE Frequency (" +
            "ID_Frequency INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Data_Frequency TEXT, " +
            "ID_Station INTEGER, " +
            "ID_City INTEGER, " +
            "FOREIGN KEY(ID_Station) REFERENCES Stations(ID), " +
            "FOREIGN KEY(ID_City) REFERENCES Cities(ID_Cities));";

    String sqlDataTemp = "CREATE TABLE data_temp (" +
            "Longitud DOUBLE, " +
            "Latitud DOUBLE);";

    private String sqlInsertStatios(String name){

        return "INSERT INTO Stations (ID, Name)" +
                "VALUES(null, '" + name + "')";
    }

    private String sqlInsertCities(String name, double north, double east, double south, double west){

        return "INSERT INTO Cities (ID_Cities, Name_city, North, East, South, West)" +
                "VALUES (null, '"+name+"', "+north+", "+east+", "+south+", "+west+")";
    }

    private String sqlInsertFrequency(String dataFrequency, int idStation, int idCity) {

        return "INSERT INTO Frequency (ID_Frequency, Data_Frequency, ID_Station, ID_City)" +
                "VALUES (null, '"+dataFrequency+"', "+idStation+", "+idCity+");";
    }

    public SqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(sqlStations);
        db.execSQL(sqlCities);
        db.execSQL(sqlFrequency);
        db.execSQL(sqlDataTemp);

        // Populate table "data_temp"
        db.execSQL("INSERT INTO data_temp (Longitud, Latitud) " +
                "VALUES (0, 0);");

        // Populate table "Stations"
        db.execSQL(sqlInsertStatios("Bío Bío La Radio"));
        db.execSQL(sqlInsertStatios("ADN Radio"));
        db.execSQL(sqlInsertStatios("Digital FM"));

        // Populate table "Cities"
        db.execSQL(sqlInsertCities("Concepción y Talcahuano", -36.376573, -72.665415, -36.986187, -73.216974));
        db.execSQL(sqlInsertCities("Chillán", -36.381292, -71.814757, -36.813968, -72.434296));

        // Populate table Frequency biobio
        db.execSQL(sqlInsertFrequency("98.1 fm - 620 am", 1, 1)); //Concepción
        db.execSQL(sqlInsertFrequency("105.3 fm", 1, 2)); // Chillán

        // ADN
        db.execSQL(sqlInsertFrequency("104.1 fm", 2, 1)); //Concepción
        db.execSQL(sqlInsertFrequency("101.3 fm", 2, 2)); // Chillán

        // Digital FM
        db.execSQL(sqlInsertFrequency("88.1 fm", 3, 1)); //Concepción

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
