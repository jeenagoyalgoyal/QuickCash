package com.example.quickcash.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quickcash.R;

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
    public static int getSalary(String name, String job) {
        //return salary based on the job by given person(job is "job" input and person is "name" input)
        return -1;
    }

    //Return Duration and length(years/months/days)
    public static String[] getDuration(String Name, String jobListing) {
        return new String[]{"3","years"};
    }
}
