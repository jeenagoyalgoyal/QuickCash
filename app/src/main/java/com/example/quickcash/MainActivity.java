package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.quickcash.databinding.ActivityMainBinding;
import com.example.quickcash.model.Job;
import com.example.quickcash.model.JobLocation;
import com.example.quickcash.model.UseRole;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.switch_role) {
            UseRole useRole = UseRole.getInstance();
            int userid = 123;
            Intent intent;

            if (useRole.getCurrentRole().equals("employer")) {
                intent = new Intent(MainActivity.this, EmployeeHomepageActivity.class);
                Toast.makeText(this, "Switched to Employee role", Toast.LENGTH_SHORT).show();
            } else {
                intent = new Intent(MainActivity.this, EmployerHomepageActivity.class);
                Toast.makeText(this, "Switched to Employer role", Toast.LENGTH_SHORT).show();
            }

            intent.putExtra("userID", userid);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_profile) {
            Intent intent = new Intent(MainActivity.this, Profile.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void singleCallToMap(Job job) {
        Intent intentToMapSingleJob = new Intent(this, GoogleSearchMapActivity.class);

        // Get location using the helper method from Job class
        JobLocation jobLocation = job.getJobLocation();
        if (jobLocation == null) {
            Toast.makeText(this, "No location data available for this job", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Double> latitudes = new ArrayList<>();
        ArrayList<Double> longitudes = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<Integer> salaries = new ArrayList<>();
        ArrayList<String> durations = new ArrayList<>();
        ArrayList<String> companies = new ArrayList<>();
        ArrayList<String> jobTypes = new ArrayList<>();

        latitudes.add(jobLocation.getLat());
        longitudes.add(jobLocation.getLng());
        titles.add(job.getJobTitle());
        salaries.add(job.getSalary());
        durations.add(job.getExpectedDuration());
        companies.add(job.getCompanyName());
        jobTypes.add(job.getJobType());

        intentToMapSingleJob.putExtra("latitudes", latitudes);
        intentToMapSingleJob.putExtra("longitudes", longitudes);
        intentToMapSingleJob.putStringArrayListExtra("titles", titles);
        intentToMapSingleJob.putIntegerArrayListExtra("salaries", salaries);
        intentToMapSingleJob.putStringArrayListExtra("durations", durations);
        intentToMapSingleJob.putStringArrayListExtra("companies", companies);
        intentToMapSingleJob.putStringArrayListExtra("jobTypes", jobTypes);

        startActivity(intentToMapSingleJob);
    }
}
