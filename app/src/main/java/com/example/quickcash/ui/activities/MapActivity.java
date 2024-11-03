package com.example.quickcash.ui.activities;

<<<<<<< HEAD
import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
=======
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
>>>>>>> c8452335f077f68a71191e31cd517c40ec38c3f0

import com.example.quickcash.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<MarkerOptions> markers = new ArrayList<>();
    private boolean addressEntered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Dummy data for testing
        addDummyMarkers();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add markers to the map
        for (MarkerOptions markerOptions : markers) {
            Marker marker = mMap.addMarker(markerOptions);
        }

        // Move the camera to a default location if no address was entered
        if (!markers.isEmpty()) {
            LatLng defaultLocation = markers.get(0).getPosition();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12));
        } else {
            LatLng defaultLocation = new LatLng(-34, 151);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
        }
    }

    private void addDummyMarkers() {
        // Add some dummy markers for testing purposes
        markers.add(new MarkerOptions().position(new LatLng(-34, 151)).title("Marker in Sydney"));
        markers.add(new MarkerOptions().position(new LatLng(-35, 150)).title("Marker near Sydney"));
        markers.add(new MarkerOptions().position(new LatLng(-33, 149)).title("Marker further from Sydney"));
    }
}
