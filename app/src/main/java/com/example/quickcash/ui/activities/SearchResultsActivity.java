package com.example.quickcash.ui.activities;

import android.content.Intent;
import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        firebaseCRUD = new FirebaseCRUD(FirebaseDatabase.getInstance());

        recyclerView = findViewById(R.id.recyclerView); // Ensure this ID matches your layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobList = new ArrayList<>();
        jobAdapter = new JobAdapter(jobList);
        recyclerView.setAdapter(jobAdapter);

        // Get the search query from the Intent
        Intent intent = getIntent();
        String query = intent.getStringExtra("search_query");
        if (query != null && !query.isEmpty()) {
            performJobSearch(query);
        } else {
            // Handle case where no query is provided
        }
    }

    private void performJobSearch(String query) {
        firebaseCRUD.searchJobs(query, new JobDataCallback() {
            @Override
            public void onCallback(List<Job> jobs) {
                jobList.clear();
                jobList.addAll(jobs);
                jobAdapter.notifyDataSetChanged();
            }
        });
    }
}
