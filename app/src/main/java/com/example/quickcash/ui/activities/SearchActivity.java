package com.example.quickcash.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.R;
import com.example.quickcash.ui.models.Job;
import com.example.quickcash.ui.repositories.FirebaseCRUD;
import com.example.quickcash.ui.utils.JobAdapter;
import com.example.quickcash.ui.utils.JobSearchValidator;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private FirebaseCRUD firebaseCRUD;
    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;
    private List<Job> jobList;
    private Button showMapButton;

    private EditText jobTitle;
    private EditText minSalary;
    private EditText maxSalary;
    private EditText duration;
    private EditText location;
    private EditText errorText;
    private Button searchButton;

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

                boolean flag = false;

                jobTitle = findViewById(R.id.jobTitleText);
                    String jobTitleString = jobTitle.getText().toString().trim();
                minSalary = findViewById(R.id.MinSalary);
                    String minSalaryString = minSalary.getText().toString().trim();
                maxSalary = (findViewById(R.id.MaxSalary));
                    String maxSalaryString = maxSalary.getText().toString().trim();
                duration = findViewById(R.id.companyNameText);
                    String durationString = duration.getText().toString().trim();
                location = findViewById(R.id.Location);
                    String locationString = location.getText().toString().trim();
                searchButton = findViewById(R.id.showMapButton);

                if(JobSearchValidator.isValidSalaryString(minSalaryString, maxSalaryString)){

                    flag = true;
                } else if(JobSearchValidator.isValidJobTitle(jobTitleString)){

                    flag = true;
                } else if (JobSearchValidator.isValidDuration(durationString)) {

                    flag = true;
                } else if(JobSearchValidator.isValidLocation(locationString)){

                    flag = true;
                }

                if(flag){
                    showMapWithJobs();
                } else{
                    Toast.makeText(SearchActivity.this, "All fields can't be empty!", Toast.LENGTH_LONG).show();
                }

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
        firebaseCRUD.searchJobs(query, new FirebaseCRUD.JobDataCallback() {
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
