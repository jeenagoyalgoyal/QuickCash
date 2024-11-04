package com.example.quickcash;

import static com.example.quickcash.filter.JobSearchFilter.*;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.Firebase.JobCRUD;
import com.example.quickcash.adapter.JobSearchAdapter;
import com.example.quickcash.model.Job;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

/**
 * This class is used for searching jobs via using filters to find the
 * posted jobs in the database.
 */
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

    /**
     * On create, initialize the job search parameter form
     * @param savedInstanceState
     */
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

        searchButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When user clicks search button, check the filters are filled
             * @param view
             */
            @Override
            public void onClick(View view) {
                if(allEmptyFields()){
                    errorText.setText("All Fields are empty");
                }else if(checkSalaryField()){
                    errorText.setText("Enter Valid Salary Range");
                }else{
                    errorText.setText(""); // Clear any previous error
                    performSearch();
                }
            }
        });

        // Set up the back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(JobSearchParameterActivity.this, EmployeeHomepageActivity.class);
            startActivity(intent);
            finish(); // Optional: Call finish() if you don't want to keep the  in the back stack
        });
    }

    /**
     * Method initializes the job search form input variables
     */
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

        jobsRef = FirebaseDatabase.getInstance();
        jobCRUD = new JobCRUD(jobsRef);
    }


    /**
     * This method finds the results from the job search
     */
    private void performSearch() {

        Query query = createQuery();

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

    /**
     * Creates a firebase query with the input by user
     * @return
     */
    private Query createQuery(){
        // Get search parameters
        String title = jobTitle.getText().toString().trim();
        String company = companyName.getText().toString().trim();
        String minSalStr = minSalary.getText().toString().trim();
        String maxSalStr = maxSalary.getText().toString().trim();
        String jobDuration = duration.getText().toString().trim();
        String jobLocation = location.getText().toString().trim();
        String salary = "salary";

        Query query = jobsRef.getReference("Jobs");

        // Apply filters based on non-empty inputs
        if (isValidField(title)) {
            query = query.orderByChild("jobTitle").equalTo(title);
        }else if(isValidField(company)){
            query =query.orderByChild("companyName").equalTo(company);
        }else if (isValidField(jobLocation)) {
            query = query.orderByChild("location").equalTo(jobLocation);
        }else if (isValidField(minSalStr) && isValidField(maxSalStr)) {
            int minSal = Integer.parseInt(minSalStr);
            int maxSal = Integer.parseInt(maxSalStr);
            query = query.orderByChild(salary).startAt(minSal).endAt(maxSal);
        }else if(isValidField(minSalStr)){
            int minSal = Integer.parseInt(minSalStr);
            query = query.orderByChild(salary).startAt(minSal);
        }else if(isValidField(maxSalStr)){
            int maxSal = Integer.parseInt(maxSalStr);
            query = query.orderByChild(salary).endAt(maxSal);
        }else if (isValidField(jobDuration)) {
            query = query.orderByChild("expectedDuration").equalTo(jobDuration);
        }

        return query;
    }


    /**
     * Filters for the user to see if it's valid
     * @param job
     * @return true if valid, false otherwise
     */
    private boolean passesAdditionalFilters(Job job) {
        // Get the user input again
        String title = jobTitle.getText().toString().trim();
        String company = companyName.getText().toString().trim();
        String minSalStr = minSalary.getText().toString().trim();
        String maxSalStr = maxSalary.getText().toString().trim();
        String jobDuration = duration.getText().toString().trim();
        String jobLocation = location.getText().toString().trim();

        return passesAdditionalJobFilters(job, title, company, minSalStr, maxSalStr, jobDuration, jobLocation);
    }

    /**
     * Checks if fields are all empty
     * @return true if all empty
     */
    public boolean allEmptyFields(){
        return jobTitle.getText().toString().trim().isEmpty() &&
               companyName.getText().toString().trim().isEmpty() &&
               minSalary.getText().toString().trim().isEmpty() &&
               maxSalary.getText().toString().trim().isEmpty() &&
               duration.getText().toString().trim().isEmpty() &&
               location.getText().toString().trim().isEmpty();
    }

    /**
     * Validates to see if salary is valid
     * @return false if empty
     */
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
