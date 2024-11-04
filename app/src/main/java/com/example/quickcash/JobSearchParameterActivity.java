package com.example.quickcash;

import static com.example.quickcash.filter.JobSearchFilter.*;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.Firebase.JobCRUD;
import com.example.quickcash.adapter.JobSearchAdapter;
import com.example.quickcash.model.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

/**
 * This class is used for searching jobs via using filters to find the
 * posted jobs in the database.
 */
public class JobSearchParameterActivity extends AppCompatActivity{

    private static final String TAG = "JobSearchParameter";
    private EditText jobTitle;
    private EditText companyName;
    private EditText minSalary;
    private EditText maxSalary;
    private EditText duration;
    private EditText location;
    private TextView errorText;
    private Button searchButton;
    private Button mapButton;

    private RecyclerView recyclerView;
    private JobSearchAdapter jobSearchAdapter;
    private List<Job> jobList;
    private FirebaseDatabase jobsRef;
    private JobCRUD jobCRUD;
    private String email;
    public String userID;

    private ArrayList<Double> latitudes = new ArrayList<>();
    private ArrayList<Double> longitudes = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<Integer> salaries = new ArrayList<>();
    private ArrayList<String> durations = new ArrayList<>();
    private ArrayList<String> companies = new ArrayList<>();
    private ArrayList<String> jobTypes = new ArrayList<>();

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

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allEmptyFields()){
                    errorText.setText("All Fields are empty");
                }else if(checkSalaryField()){
                    errorText.setText("Enter Valid Salary Range");
                }else{
                    errorText.setText(""); // Clear any previous error
                    performSearch();
                    queryJobsForMap();
                }
            }
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
        mapButton = findViewById(R.id.showMapButton);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        latitudes = new ArrayList<>();
        longitudes = new ArrayList<>();
        titles = new ArrayList<>();
        salaries = new ArrayList<>();
        durations = new ArrayList<>();
        companies = new ArrayList<>();
        jobTypes = new ArrayList<>();

        jobList = new ArrayList<>();
        jobSearchAdapter = new JobSearchAdapter(jobList);
        recyclerView.setAdapter(jobSearchAdapter);

        jobsRef = FirebaseDatabase.getInstance();
        jobCRUD = new JobCRUD(jobsRef);
    }

    /**
     * Queries job data for map-based results and processes each job to extract relevant information.
     *
     * <p>This method initiates a one-time read operation on a Firebase reference {@code jobsRef} to retrieve
     * job postings. For each job that meets specified filters, it extracts details like latitude, longitude,
     * title, salary, duration, company, and job type. The method defaults to Halifax coordinates for unspecified
     * locations and custom coordinates for Montreal; additional cities can be added as needed.
     *
     * <p>If jobs are found, this information is passed to {@code launchMapActivity()} to display results on a map.
     * If no jobs match the criteria, a "No Results Found" message is displayed. In case of errors, appropriate
     * messages are logged and displayed on {@code jspErrorDisplay}.
     *
     * @see #passesAdditionalFilters(Job)
     */
    private void queryJobsForMap() {
        Log.e(TAG, "Starting map search");


        for(Job j: jobList){

            // Add job data to lists

            latitudes.add(j.getJobLocation().getLat());
            longitudes.add(j.getJobLocation().getLng());

            Log.e(TAG, String.valueOf(j.getJobLocation().getLat()));
            Log.e(TAG, String.valueOf(j.getJobLocation().getLng()));

            titles.add(j.getJobTitle());
            salaries.add(j.getSalary());
            durations.add(j.getExpectedDuration());
            companies.add(j.getCompanyName());
            jobTypes.add(j.getJobType());
        }
        launchMapActivity();
    }

    /**
     * Launches the {@code MapActivity} to display job listings on a map.
     *
     * <p>This method creates an {@code Intent} to start {@code MapActivity} and attaches job-related
     * data as extras, including latitude and longitude coordinates, job titles, salaries, durations,
     * company names, and job types. The data is passed as {@code ArrayList} objects, allowing
     * {@code MapActivity} to access and display multiple job listings simultaneously.
     *
     */
    private void launchMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("latitudes", latitudes);
        intent.putExtra("longitudes", longitudes);
        intent.putStringArrayListExtra("titles", titles);
        intent.putIntegerArrayListExtra("salaries", salaries);
        intent.putStringArrayListExtra("durations", durations);
        intent.putStringArrayListExtra("companies", companies);
        intent.putStringArrayListExtra("jobTypes", jobTypes);
        startActivity(intent);
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
