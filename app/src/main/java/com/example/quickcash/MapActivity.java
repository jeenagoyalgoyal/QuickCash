package com.example.quickcash;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Google Maps API Key
        String googleMapsApiKey = "AIzaSyANlFb9RL5lWkHzKSPg0xd5aDnhk0TquG4";

        // Here we implement the map functionality (e.g., using Google Maps API)
    }

    //Placeholders for implementation of getting job details logic
    public static String getJobTitle(String name) {
        //Change input to what is required
        //Implement getter using firebase
        return "null";
    }
}
