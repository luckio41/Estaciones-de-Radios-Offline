package cl.luckio.estacionesderadiosoffline;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    public double latitude;
    public double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        EasySplashScreen config = new EasySplashScreen(SplashActivity.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(4000)
                .withBackgroundResource(android.R.color.holo_red_light)
                .withHeaderText("")
                .withFooterText("Luckio Software 2016")
                .withBeforeLogoText("")
                .withAfterLogoText("");

        //set your own animations
        myCustomTextViewAnimation(config.getFooterTextView());

        config.getHeaderTextView().setTextColor(Color.WHITE);
        config.getFooterTextView().setTextColor(Color.WHITE);

        //create the view
        View easySplashScreenView = config.create();

        setContentView(easySplashScreenView);

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
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 15000, 15, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 15, locationListener);
        }
    }

    private void myCustomTextViewAnimation(TextView tv){
        Animation animation=new TranslateAnimation(0,0,480,0);
        animation.setDuration(1200);
        tv.startAnimation(animation);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        locationManager.removeUpdates(locationListener);
    }
}
