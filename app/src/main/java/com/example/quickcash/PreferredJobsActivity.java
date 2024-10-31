package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PreferredJobsActivity extends AppCompatActivity {

    private ListView preferredJobListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_jobs);

        preferredJobListView = findViewById(R.id.preferredJobListView);
        ArrayList<TempJob.Job> preferredJobs = TempJob.getPreferredJobList();

        ArrayAdapter<TempJob.Job> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, preferredJobs);
        preferredJobListView.setAdapter(adapter);

        // Set up the back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PreferredJobsActivity.this, JobsActivity.class);
            startActivity(intent);
            finish(); // Optional: Call finish() if you don't want to keep the JobsActivity in the back stack
        });
    }
}
