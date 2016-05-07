package cl.luckio.estacionesderadiosoffline;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Luckio on 06/05/2016.
 */
public class SqlHelper extends SQLiteOpenHelper {

    String sqlStation = "CREATE TABLE Stations (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "Name TEXT, " +
            "Description TEXT);";

    private String sqlInsert(String name, String desc){

        return "INSERT INTO Stations (ID, Name, Description)" +
                "VALUES(null, '" + name + "', '" + desc + "')";
    }

    public SqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlStation);
        db.execSQL(sqlInsert("Bío Bío La Radio", ""));
        db.execSQL(sqlInsert("ADN Radio", ""));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
