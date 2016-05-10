package cl.luckio.estacionesderadiosoffline;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Luckio on 06/05/2016.
 */
public class GpsService extends Service implements LocationListener {

    private final Context ctx;
    double lat;
    double len;
    Location location;
    boolean activeGps;
    TextView tvCoordinates;
    LocationManager locationManager;

    public GpsService() {
        super();
        this.ctx = this.getApplicationContext();
    }

    public GpsService(Context c) {
        super();
        this.ctx = c;
        getLocation();
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) this.ctx.getSystemService(LOCATION_SERVICE);
            activeGps = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
        } catch (Exception e) {
        }

        if (activeGps)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000 * 60, 10, this);
                location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                lat = location.getLatitude();
                len = location.getLongitude();
                return;
            }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
