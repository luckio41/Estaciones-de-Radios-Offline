package cl.luckio.estacionesderadiosoffline;

import android.Manifest;
import android.app.SearchManager;
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
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private ListView lvRadios;
    private String listaRadios[];
    private String status[];
    private Integer[] imgid = {
            R.drawable.biobio,
            R.drawable.adn,
            R.drawable.digital
    };

    public double latitude;
    public double longitude;

    private ListView lista;
    // Init SqlHelper
    private SqlHelper sqlHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqlHelper = new SqlHelper(this, "ESTACIONESDB", null, 1);
        db = sqlHelper.getWritableDatabase();

        updateCampos(db);

        // Location manager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.getAllProviders();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                location.getProvider();

                if (db != null) {
                    String strUpdate = "UPDATE data_temp SET Latitud = " + latitude + ", Longitud = " + longitude + " WHERE rowid = 1;";
                    db.execSQL(strUpdate);
                    updateCampos(db);
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
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 15000, 15, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 15, locationListener);
        }
    }

    private void updateCampos(SQLiteDatabase db) {
        if (db != null) {
            Cursor c = db.rawQuery("SELECT * FROM Stations", null);
            Cursor c_ = db.rawQuery("SELECT Latitud, Longitud FROM data_temp", null);
            String txtStatus;

            listaRadios = new String[c.getCount()];
            status = new String[c.getCount()];
            ResultsHelper resultsHelper;

            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {
                    listaRadios[i] = c.getString(1);

                    if (c_.moveToFirst()) {
                        for (int z = 0; z < c_.getCount(); z++) {
                            resultsHelper = new ResultsHelper(this, c_.getDouble(0), c_.getDouble(1), c.getInt(0));
                            if (resultsHelper.calcularStatus() == 1) {
                                txtStatus = "al aire";
                            } else {
                                txtStatus = "fuera de alcance";
                            }
                            status[i] = txtStatus;
                            c_.moveToNext();
                        }
                    }
                    c.moveToNext();
                }
            }
        }

        RadiosListAdapter adapter = new RadiosListAdapter(this, listaRadios, status, imgid);
        lvRadios = (ListView) findViewById(R.id.lvRadios);
        lvRadios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowResults(position, view);
            }
        });

        lvRadios.setAdapter(adapter);
    }

    private void searchCampos(SQLiteDatabase db, String text) {
        if (db != null) {
            Cursor c = db.rawQuery("SELECT * FROM Stations WHERE Name LIKE '%" + text + "%'", null);
            Cursor c_ = db.rawQuery("SELECT Latitud, Longitud FROM data_temp", null);
            String txtStatus;

            listaRadios = new String[c.getCount()];
            status = new String[c.getCount()];
            ResultsHelper resultsHelper;

            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {
                    listaRadios[i] = c.getString(1);

                    if (c_.moveToFirst()) {
                        for (int z = 0; z < c_.getCount(); z++) {
                            resultsHelper = new ResultsHelper(this, c_.getDouble(0), c_.getDouble(1), c.getInt(0));
                            if (resultsHelper.calcularStatus() == 1) {
                                txtStatus = "al aire";
                            } else {
                                txtStatus = "fuera de alcance";
                            }
                            status[i] = txtStatus;
                            c_.moveToNext();
                        }
                    }
                    c.moveToNext();
                }
            }
        }

        RadiosListAdapter adapter = new RadiosListAdapter(this, listaRadios, status, imgid);
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

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);

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

    public void AcercaDe() {
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchCampos(db, newText);
        return false;
    }
}
