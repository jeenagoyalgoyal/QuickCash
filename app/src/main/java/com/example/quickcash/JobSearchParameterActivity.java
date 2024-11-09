package com.example.quickcash;

import static android.content.Intent.getIntent;
import static com.example.quickcash.filter.JobSearchFilter.*;

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

/**
 * This class is used for searching jobs via filters to find the
 * posted jobs in the database.
 */
public class JobSearchParameterActivity extends AppCompatActivity {

    private static final String TAG = "JobSearchParameter";

    // UI components
    private EditText jobTitle, companyName, minSalary, maxSalary, duration, location;
    private TextView errorText;
    private Button searchButton, mapButton;
    private RecyclerView recyclerView;

    // Adapter and Data
    private JobSearchAdapter jobSearchAdapter;
    private List<Job> jobList;
    private FirebaseDatabase jobsRef;
    private JobCRUD jobCRUD;

    // User info
    private String email, userID;

    // Map data
    private ArrayList<Double> latitudes = new ArrayList<>();
    private ArrayList<Double> longitudes = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<Integer> salaries = new ArrayList<>();
    private ArrayList<String> durations = new ArrayList<>();
    private ArrayList<String> companies = new ArrayList<>();
    private ArrayList<String> jobTypes = new ArrayList<>();

    /**
     * Initializes the job search parameter form.
     *
     * @param savedInstanceState Saved instance state bundle.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_search_parameter);

        init();

        // Getting email and user ID
        Intent intentPreferredEmployers = getIntent();
        this.email = intentPreferredEmployers.getStringExtra("email");
        if (email != null && !email.isEmpty()) {
            this.userID = email.replace(".", ",");
        }

        searchButton.setOnClickListener(view -> {
            if (checkSalaryField()) {
                errorText.setText("Enter Valid Salary Range");
            } else {
                errorText.setText(""); // Clear any previous error
                performSearch();
            }
        });

        mapButton.setOnClickListener(view -> {
            errorText.setText(""); // Clear any previous error
            performSearch();
            queryJobsForMap();
        });
    }

    /**
     * Initializes the job search form input variables.
     */
    public void init() {
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

        jobList = new ArrayList<>();
        jobSearchAdapter = new JobSearchAdapter(jobList);
        recyclerView.setAdapter(jobSearchAdapter);

        jobsRef = FirebaseDatabase.getInstance();
        jobCRUD = new JobCRUD(jobsRef);
    }

    /**
     * Queries job data for map-based results and processes each job.
     */
    private void queryJobsForMap() {
        Log.e(TAG, "Starting map search");

        for (Job job : jobList) {
            latitudes.add(job.getJobLocation().getLat());
            longitudes.add(job.getJobLocation().getLng());
            titles.add(job.getJobTitle());
            salaries.add(job.getSalary());
            durations.add(job.getExpectedDuration());
            companies.add(job.getCompanyName());
            jobTypes.add(job.getJobType());
        }

        launchMapActivity();
    }

    /**
     * Launches the MapActivity to display job listings on a map.
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
     * Retrieves job search results based on input parameters.
     */
    private void performSearch() {
        Query query = createQuery();

        jobList.clear();
        jobSearchAdapter.notifyDataSetChanged();

        jobCRUD.getJobsByQuery(query).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Job> jobs = task.getResult();
                jobList.clear();

                for (Job job : jobs) {
                    if (passesAdditionalFilters(job)) {
                        jobList.add(job);
                    }
                }

                if (!jobList.isEmpty()) {
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
     * Creates a Firebase query with the input provided by the user.
     */
    private Query createQuery() {
        String title = jobTitle.getText().toString().trim();
        String company = companyName.getText().toString().trim();
        String minSalStr = minSalary.getText().toString().trim();
        String maxSalStr = maxSalary.getText().toString().trim();
        String jobDuration = duration.getText().toString().trim();
        String jobLocation = location.getText().toString().trim();

        Query query = jobsRef.getReference("Jobs");

        if (isValidField(title)) {
            query = query.orderByChild("jobTitle").equalTo(title);
        } else if (isValidField(company)) {
            query = query.orderByChild("companyName").equalTo(company);
        } else if (isValidField(jobLocation)) {
            query = query.orderByChild("location").equalTo(jobLocation);
        } else if (isValidField(minSalStr) && isValidField(maxSalStr)) {
            int minSal = Integer.parseInt(minSalStr);
            int maxSal = Integer.parseInt(maxSalStr);
            query = query.orderByChild("salary").startAt(minSal).endAt(maxSal);
        } else if (isValidField(minSalStr)) {
            int minSal = Integer.parseInt(minSalStr);
            query = query.orderByChild("salary").startAt(minSal);
        } else if (isValidField(maxSalStr)) {
            int maxSal = Integer.parseInt(maxSalStr);
            query = query.orderByChild("salary").endAt(maxSal);
        } else if (isValidField(jobDuration)) {
            query = query.orderByChild("expectedDuration").equalTo(jobDuration);
        }

        return query;
    }

    /**
     * Checks if a job passes additional user-defined filters.
     */
    private boolean passesAdditionalFilters(Job job) {
        String title = jobTitle.getText().toString().trim();
        String company = companyName.getText().toString().trim();
        String minSalStr = minSalary.getText().toString().trim();
        String maxSalStr = maxSalary.getText().toString().trim();
        String jobDuration = duration.getText().toString().trim();
        String jobLocation = location.getText().toString().trim();

        return passesAdditionalJobFilters(job, title, company, minSalStr, maxSalStr, jobDuration, jobLocation);
    }

    /**
     * Validates salary fields.
     */
    public boolean checkSalaryField() {
        String minS = minSalary.getText().toString().trim();
        String maxS = maxSalary.getText().toString().trim();

        if (minS.isEmpty() || maxS.isEmpty()) {
            return false;
        }

        return !isValidSalary(Integer.parseInt(minS), Integer.parseInt(maxS));
    }
}
