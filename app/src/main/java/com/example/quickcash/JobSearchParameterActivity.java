package com.example.quickcash;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class JobSearchParameterActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_search_parameter);
    }


    // Tests the job title (can be empty)
    public static boolean isValidJobTitle(String title) {
        return title != null;
    }

    // Tests salary is within boundary
    public static boolean isValidSalary(int minSalary, int maxSalary) {
        return minSalary >= 0 && maxSalary >= 0 && minSalary <= maxSalary;
    }

    // Tests valid duration
    public static boolean isValidDuration(String duration) {
        return duration != null && !duration.trim().isEmpty();
    }

    // Tests valid location
    public static boolean isValidLocation(String location) {
        return location != null && !location.trim().isEmpty();
    }
}
