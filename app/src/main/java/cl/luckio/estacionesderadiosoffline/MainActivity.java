package cl.luckio.estacionesderadiosoffline;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

        SqlHelper sqlHelper = new SqlHelper(this, "ESTACIONESDB", null, 1);
        SQLiteDatabase db = sqlHelper.getReadableDatabase();

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ShowResults(int position, View view) {
        Intent i = new Intent(this, ResultsActivity.class);

        if(latitude > 0)
            latitude = latitude;
        else
            latitude = 0;

        if(longitude > 0)
            longitude = longitude;
        else
            longitude = 0;

        i.putExtra("position", position);
        i.putExtra("latitude", latitude);
        i.putExtra("longitude", longitude);
        startActivity(i);
    }

    public void AcercaDe(){
        Intent intent = new Intent(this, AcercaDe.class);
        startActivity(intent);
    }
}
