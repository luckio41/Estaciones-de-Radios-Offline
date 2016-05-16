package cl.luckio.estacionesderadiosoffline;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private ListView lvRadios;
    private String listaRadios[];
    private Integer[] imgid = {
            R.drawable.biobio,
            R.drawable.adn,
            R.drawable.digital
    };

    public double latitude;
    public double longitude;

    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init SqlHelper
        final SqlHelper sqlHelper = new SqlHelper(this, "ESTACIONESDB", null, 1);
        final SQLiteDatabase db = sqlHelper.getWritableDatabase();

        // Location manager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.getAllProviders();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                location.getProvider();

                if(db != null) {
                    String strUpdate = "UPDATE data_temp SET Latitud = "+latitude+", Longitud = "+longitude+ " WHERE rowid = 1;";
                    db.execSQL(strUpdate);
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
            }, 10);
            return;
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 35000, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, locationListener);
        }

        if (db != null) {
            Cursor c = db.rawQuery("SELECT * FROM Stations", null);
            listaRadios = new String[c.getCount()];

            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {
                    listaRadios[i] = c.getString(1);
                    c.moveToNext();
                }
            }
        }

        RadiosListAdapter adapter = new RadiosListAdapter(this, listaRadios, imgid);
        lvRadios = (ListView) findViewById(R.id.lvRadios);
        lvRadios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowResults(position, view);
            }
        });
        lvRadios.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_acerca_de:
                AcercaDe();
                return true;
            case R.id.action_salir:
                Salir();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ShowResults(int position, View view) {
        Intent i = new Intent(this, ResultsActivity.class);
        double _latitude = 0;
        double _longitude = 0;

        // Init SqlHelper
        final SqlHelper sqlHelper = new SqlHelper(this, "ESTACIONESDB", null, 1);
        final SQLiteDatabase db = sqlHelper.getReadableDatabase();

        if (db != null) {
            Cursor c = db.rawQuery("SELECT Latitud, Longitud FROM data_temp", null);

            if (c.moveToFirst()) {
                for (int x = 0; x < c.getCount(); x++) {
                    _latitude = c.getDouble(0);
                    _longitude = c.getDouble(1);
                    c.moveToNext();
                }
            }
        }

        i.putExtra("position", position);
        i.putExtra("latitude", _latitude);
        i.putExtra("longitude", _longitude);
        startActivity(i);
    }

    public void AcercaDe(){
        Intent intent = new Intent(this, AcercaDe.class);
        startActivity(intent);
    }

    public void Salir() {
        AlertDialog dialog = new AlertDialog.Builder(this).setMessage("¿Salir de la aplicación?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // Do stuff when user neglects.
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                        // Do stuff when cancelled
                    }
                }).create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog dialog = new AlertDialog.Builder(this).setMessage("¿Salir de la aplicación?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // Do stuff when user neglects.
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                        // Do stuff when cancelled
                    }
                }).create();
        dialog.show();
    }
}
