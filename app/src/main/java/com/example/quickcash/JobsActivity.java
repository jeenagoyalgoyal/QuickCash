package com.example.quickcash;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class JobsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs); // Create a layout file for this Activity

        ListView jobsListView = findViewById(R.id.jobsListView); // ListView to display jobs
        List<TempJob.Job> jobList = TempJob.getJobList(); // Get the dummy jobs list

        // Create an ArrayAdapter to display job titles in the ListView
        ArrayAdapter<TempJob.Job> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                jobList
        );

        jobsListView.setAdapter(adapter);
    }
}
