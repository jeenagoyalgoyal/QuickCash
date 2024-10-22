package com.example.quickcash;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.Manifest;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationHelper {
    private final Context context;
    private final FusedLocationProviderClient fusedLocationProviderClient;

    public LocationHelper(Context context){
        this.context=context;
        this.fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(context);
    }

    public void requestLocationUpdate(LocationCallback callback){
        if(isLocationPermissionGranted()){
            LocationRequest locationRequest= LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10000);

            com.google.android.gms.location.LocationCallback locationCallback = new com.google.android.gms.location.LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    if (locationResult == null) {
                        callback.onPermissionDenied(); // Handle case when location is null
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        callback.onLocationReceived(location);
                    }
                }
            };

            if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
               callback.onPermissionDenied();
                return;
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, (com.google.android.gms.location.LocationCallback) callback, Looper.getMainLooper());
        }else {
            callback.onPermissionDenied();
        }
    }
    public boolean isLocationPermissionGranted() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public interface LocationCallback{
        void onLocationReceived(Location location);
        void onPermissionDenied();
    }
}
