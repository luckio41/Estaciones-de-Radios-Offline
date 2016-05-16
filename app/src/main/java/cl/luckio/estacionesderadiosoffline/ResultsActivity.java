package cl.luckio.estacionesderadiosoffline;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ResultsActivity extends AppCompatActivity {

    private TextView tvCity;
    private TextView tvFrequency;
    private ImageView imgCity;
    private SqlHelper sqlHelper;
    private SQLiteDatabase db;
    private String nameCity;
    private String frequency;
    private double _latitude;
    private double _longitude;
    private int position;
    private ProgressBar progressBar;
    private int idCity;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        _latitude = extras.getDouble("latitude");
        _longitude = extras.getDouble("longitude");
        position = extras.getInt("position");

        tvCity = (TextView) findViewById(R.id.tvCity);
        tvFrequency = (TextView) findViewById(R.id.tvFrequency);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgCity = (ImageView) findViewById(R.id.imgCity);
        progressBar.setVisibility(View.VISIBLE);

        sqlHelper = new SqlHelper(this, "ESTACIONESDB", null, 1);
        db = sqlHelper.getReadableDatabase();

        if(db != null) {
            String sql = "SELECT c.ID_Cities, c.Name_city, f.Data_Frequency, s.Name FROM Frequency f " +
                    "INNER JOIN Cities c " +
                    "ON f.ID_City = c.ID_Cities " +
                    "INNER JOIN Stations s " +
                    "ON f.ID_Station = s.ID " +
                    "WHERE s.ID = " + (position + 1) + " " +
                    "AND " + _latitude + " <= c.North " +
                    "AND " + _latitude + " >= c.South " +
                    "AND " + _longitude + " >= c.West " +
                    "AND " + _longitude + " <= c.East";

            Cursor c = db.rawQuery(sql, null);

            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {

                    idCity = c.getInt(0);
                    nameCity = c.getString(1).toString();
                    frequency = c.getString(2).toString();

                    if(idCity == 1){
                        imgCity.setImageResource(R.drawable.concepcion);
                    }
                    if(idCity == 2){
                        imgCity.setImageResource(R.drawable.chillan);
                    }

                    c.moveToNext();
                }
            }

            tvCity.setText(nameCity);
            tvFrequency.setText(frequency);
            progressBar.setVisibility(View.GONE);
        }
    }
}
