package com.example.quickcash.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.R;
import com.example.quickcash.ui.models.Job;
import com.example.quickcash.ui.repositories.FirebaseCRUD;
import com.example.quickcash.ui.repositories.FirebaseCRUD.JobDataCallback;
import com.example.quickcash.ui.utils.JobAdapter;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {
    private FirebaseCRUD firebaseCRUD;
    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;
    private List<Job> jobList;
    private Button showMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Initialize Firebase CRUD
        firebaseCRUD = new FirebaseCRUD(FirebaseDatabase.getInstance());

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobList = new ArrayList<>();
        jobAdapter = new JobAdapter(jobList);
        recyclerView.setAdapter(jobAdapter);

        // Initialize Show Map Button
        showMapButton = findViewById(R.id.showMapButton);
        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMapWithJobs();
            }
        });

        // Get the search query from the Intent
        Intent intent = getIntent();
        String query = intent.getStringExtra("search_query");
        if (query != null && !query.isEmpty()) {
            performJobSearch(query);
        }
    }

    private void performJobSearch(String query) {
        firebaseCRUD.searchJobs(query, new JobDataCallback() {
            @Override
            public void onCallback(List<Job> jobs) {
                jobList.clear();
                jobList.addAll(jobs);
                jobAdapter.notifyDataSetChanged();

                // Enable/disable map button based on results
                showMapButton.setEnabled(!jobs.isEmpty());
            }
        });
    }

    private void showMapWithJobs() {
        Intent mapIntent = new Intent(this, MapActivity.class);
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> salaries = new ArrayList<>();
        ArrayList<String> durations = new ArrayList<>();
        ArrayList<String> lats = new ArrayList<>();
        ArrayList<String> longs = new ArrayList<>();

        // Convert job details to string format for intent extras
        for (Job job : jobList) {
            titles.add(job.getJobTitle());
            salaries.add(String.valueOf(job.getSalary()));
            durations.add(job.getDuration());
            lats.add(String.valueOf(job.getLatitude()));
            longs.add(String.valueOf(job.getLongitude()));
        }

        // Put the arrays in the intent
        mapIntent.putStringArrayListExtra("titles", titles);
        mapIntent.putStringArrayListExtra("salaries", salaries);
        mapIntent.putStringArrayListExtra("durations", durations);
        mapIntent.putStringArrayListExtra("latitudes", lats);
        mapIntent.putStringArrayListExtra("longitudes", longs);

        startActivity(mapIntent);
    }
}