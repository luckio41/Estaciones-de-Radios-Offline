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
import android.widget.ProgressBar;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    private TextView tvCity;
    private TextView tvFrequency;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private SqlHelper sqlHelper;
    private SQLiteDatabase db;
    private String nameCity;
    private String frequency;
    private double latitude;
    private double longitude;
    private ProgressBar progressBar;
    private TextView tvInfo;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        latitude = extras.getDouble("latitude");
        longitude = extras.getDouble("longitude");

        tvCity = (TextView) findViewById(R.id.tvCity);
        tvFrequency = (TextView) findViewById(R.id.tvFrequency);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        tvInfo = (TextView) findViewById(R.id.tvInfo);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        sqlHelper = new SqlHelper(this, "ESTACIONESDB", null, 1);
        db = sqlHelper.getReadableDatabase();

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if(latitude > 0)
                    latitude = latitude;
                else
                    latitude = location.getLatitude();

                if(longitude > 0)
                    longitude = longitude;
                else
                    longitude = location.getLongitude();

                if(db != null){
                    String sql = "SELECT c.Name_city, c.Frequency FROM Cities c " +
                            "INNER JOIN Stations s " +
                            "ON (c.ID_Cities = s.ID) " +
                            "WHERE "+ latitude +" <= c.North " +
                            "AND "+ latitude +" >= c.South " +
                            "AND "+ longitude +" >= c.West " +
                            "AND "+ longitude +" <= c.East";

                    Cursor c = db.rawQuery(sql, null);

                    if (c.moveToFirst()){
                        for (int i = 0; i < c.getCount() ; i ++) {
                            nameCity = c.getString(0).toString();
                            frequency = c.getString(1).toString();
                            c.moveToNext();
                        }
                    }

                    tvCity.setText(nameCity);
                    tvFrequency.setText(frequency);
                    progressBar.setVisibility(View.GONE);
                    tvInfo.setText("");

                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
            }, 10);
            return;
        } else {
            locationManager.requestLocationUpdates("gps", 1000, 0, locationListener);
        }
    }
}
