package com.example.quickcash;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.Firebase.JobCRUD;
import com.example.quickcash.adapter.JobSearchAdapter;
import com.example.quickcash.model.Job;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class JobSearchParameterActivity extends AppCompatActivity{

    private EditText jobTitle;
    private EditText companyName;
    private EditText minSalary;
    private EditText maxSalary;
    private EditText duration;
    private EditText location;
    private TextView errorText;
    private Button searchButton;
    private RecyclerView recyclerView;
    private JobSearchAdapter jobSearchAdapter;
    private List<Job> jobList;
    private FirebaseDatabase jobsRef;
    private JobCRUD jobCRUD;
    private String email;
    public String userID;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_search_parameter);

        init();

        //getting email and ID
        Intent intentPreferredEmployers = getIntent();
        this.email = intentPreferredEmployers.getStringExtra("email");
        if (email!=null && !email.isEmpty()){
            this.userID = email.replace(".", ",");
        }

        //initializing references
        jobsRef = FirebaseDatabase.getInstance();
        jobCRUD = new JobCRUD(jobsRef);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allEmptyFields()){
                    errorText.setText("All Fields are empty");
                }else if(checkSalaryField()){
                    errorText.setText("Enter Valid Salary Range");
                }else{
                    errorText.setText("success"); // Clear any previous error
                    performSearch();
                }
            }
        });
    }

    public void init(){
        jobTitle = findViewById(R.id.jobTitle);
        companyName = findViewById(R.id.companyName);
        minSalary = findViewById(R.id.minSalary);
        maxSalary = findViewById(R.id.maxSalary);
        duration = findViewById(R.id.duration);
        location = findViewById(R.id.location);
        errorText = findViewById(R.id.jspErrorDisplay);
        searchButton = findViewById(R.id.search_job_parameter);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        jobList = new ArrayList<>();
        jobSearchAdapter = new JobSearchAdapter(jobList);
        recyclerView.setAdapter(jobSearchAdapter);
    }


    private void performSearch() {
        // Get search parameters
        String title = jobTitle.getText().toString().trim();
        String company = companyName.getText().toString().trim();
        String minSalStr = minSalary.getText().toString().trim();
        String maxSalStr = maxSalary.getText().toString().trim();
        String jobDuration = duration.getText().toString().trim();
        String jobLocation = location.getText().toString().trim();

        Query query = jobsRef.getReference("Jobs");

        // Apply filters based on non-empty inputs
        if (isValidJobTitle(title)) {
            query = query.orderByChild("jobTitle").equalTo(title);
        }else if(isValidCompany(company)){
            query =query.orderByChild("companyName").equalTo(company);
        }else if (isValidLocation(jobLocation)) {
            query = query.orderByChild("location").equalTo(jobLocation);
        }else if (!minSalStr.isEmpty() && !maxSalStr.isEmpty()) {
            int minSal = Integer.parseInt(minSalStr);
            int maxSal = Integer.parseInt(maxSalStr);
            query = query.orderByChild("salary").startAt(minSal).endAt(maxSal);
        }else if(!minSalStr.isEmpty()){
            int minSal = Integer.parseInt(minSalStr);
            query = query.orderByChild("salary").startAt(minSal);
        }else if(!maxSalStr.isEmpty()){
            int maxSal = Integer.parseInt(maxSalStr);
            query = query.orderByChild("salary").endAt(maxSal);
        }else if (!isValidDuration(jobDuration)) {
            query = query.orderByChild("expectedDuration").equalTo(jobDuration);
        }

        // Clear previous search results
        jobList.clear();
        jobSearchAdapter.notifyDataSetChanged();

        jobCRUD.getJobsByQuery(query).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Job> jobs = task.getResult();
                jobList.clear();
                for(Job j: jobs) {
                    if(passesAdditionalFilters(j)) {
                        jobList.add(j);
                    }
                }
                if (jobList != null && !jobList.isEmpty()) {
                    errorText.setText(""); // Clear any previous error
                } else {
                    errorText.setText("No Results Found");
                }
                jobSearchAdapter.notifyDataSetChanged();
            } else {
                errorText.setText("Failed to retrieve jobs.");
            }
        });
    }


    private boolean passesAdditionalFilters(Job job) {
        // Get the user input again
        String title = jobTitle.getText().toString().trim();
        String company = companyName.getText().toString().trim();
        String minSalStr = minSalary.getText().toString().trim();
        String maxSalStr = maxSalary.getText().toString().trim();
        String jobDuration = duration.getText().toString().trim();
        String jobLocation = location.getText().toString().trim();

        boolean matches = true;

        if (!title.isEmpty()){
            if( !title.equalsIgnoreCase(job.getJobTitle())) {
                matches = false;
            }
        }

        if (isValidCompany(company)){
            if(!company.equalsIgnoreCase(job.getCompanyName())) {
                matches = false;
            }
        }

        if (isValidLocation(jobLocation)){
            if(!jobLocation.equalsIgnoreCase(job.getLocation())) {
                matches = false;
            }
        }

        if (isValidDuration(jobDuration)){
            if(!jobDuration.equalsIgnoreCase(job.getExpectedDuration())) {
                matches = false;
            }
        }

        if (!minSalStr.isEmpty() && !maxSalStr.isEmpty()) {
            int minSal = Integer.parseInt(minSalStr);
            int maxSal = Integer.parseInt(maxSalStr);
            int salary = job.getSalary();
            if (salary < minSal || salary > maxSal) {
                matches = false;
            }
        }else if(!minSalStr.isEmpty()) {
            int minSal = Integer.parseInt(minSalStr);
            int salary = job.getSalary();
            if (salary < minSal){
                matches = false;
            }
        }else if(!maxSalStr.isEmpty()) {
            int maxSal = Integer.parseInt(maxSalStr);
            int salary = job.getSalary();
            if(salary > maxSal){
                matches = false;
            }
        }

        return matches;
    }


    // Tests the job title (can be empty)
    public static boolean isValidJobTitle(String title) {
        return title != null && !title.trim().isEmpty();
    }

    // Tests the job title (can be empty)
    public static boolean isValidCompany(String company) {
        return company != null && !company.trim().isEmpty();
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

    public boolean allEmptyFields(){
        return jobTitle.getText().toString().trim().isEmpty() &&
               companyName.getText().toString().trim().isEmpty() &&
               minSalary.getText().toString().trim().isEmpty() &&
               maxSalary.getText().toString().trim().isEmpty() &&
               duration.getText().toString().trim().isEmpty() &&
               location.getText().toString().trim().isEmpty();
    }

    public boolean checkSalaryField(){
        String minS = minSalary.getText().toString().trim();
        String maxS = maxSalary.getText().toString().trim();

        if(minS.isEmpty() || maxS.isEmpty()){
            return false;
        }else{
            return !isValidSalary(Integer.parseInt(minS), Integer.parseInt(maxS));
        }
    }
}
