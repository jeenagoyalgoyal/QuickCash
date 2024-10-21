package com.example.quickcash;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class LocationService implements LocationListener{

    private  final Activity activity;
    private  LocationManager locationManager;
    private Location currentLocation;

    public LocationService(Activity activity){
        this.activity=activity;
        this.locationManager=(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }


    public Location getCurrentLocation(){
        return currentLocation;
    }


    public boolean isLocationEnabled(){
        return  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void requestLocationPermission(){
        if((ActivityCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
        ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else {
            startLocationUpdate();
        }
    }

    private void startLocationUpdate(){

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}
