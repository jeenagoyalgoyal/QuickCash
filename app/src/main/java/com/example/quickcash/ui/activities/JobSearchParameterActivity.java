package com.example.quickcash.ui.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.R;
import com.example.quickcash.utils.adapters.JobSearchAdapter;
import com.example.quickcash.models.Job;
import com.example.quickcash.utils.LocationHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class JobSearchParameterActivity extends AppCompatActivity {

    private static final String TAG = "JobSearchParameter";

    // Test locations around Halifax
    private static final Object[][] HALIFAX_TEST_LOCATIONS = {
            // Downtown Halifax
            {44.6488, -63.5752, "Software Developer", "Tech Halifax", "Full Time", 35, "40 hours"},

            // Halifax Shopping Centre
            {44.6350, -63.5917, "Retail Manager", "Mall Corp", "Full Time", 28, "35 hours"},

            // Dalhousie University
            {44.6366, -63.5917, "Research Assistant", "Dalhousie", "Part Time", 25, "20 hours"},

            // Spring Garden Road
            {44.6427, -63.5751, "Barista", "Coffee Shop", "Part Time", 30, "25 hours"},

            // Scotia Square
            {44.6483, -63.5739, "Office Admin", "Scotia Corp", "Full Time", 32, "40 hours"},

            // Halifax Central Library
            {44.6434, -63.5766, "Librarian", "Halifax Library", "Part Time", 27, "30 hours"},

            // Halifax Waterfront
            {44.6476, -63.5683, "Tour Guide", "Harbor Tours", "Contract", 29, "35 hours"},

            // Quinpool Road
            {44.6447, -63.5895, "Chef", "Restaurant NS", "Full Time", 40, "45 hours"},

            // Halifax Common
            {44.6497, -63.5891, "Recreation Coordinator", "City Parks", "Seasonal", 26, "40 hours"},

            // Point Pleasant Park
            {44.6276, -63.5686, "Park Maintenance", "Parks & Rec", "Part Time", 24, "25 hours"}
    };

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
    private List<Job> jobListToMap;
    private DatabaseReference jobsRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_search_parameter);

        init();

        jobsRef = FirebaseDatabase.getInstance().getReference("Jobs");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allEmptyFields()) {
                    errorText.setText("All Fields are empty");
                    errorText.setTextColor(Color.parseColor("#EB0101"));
                } else if (checkSalaryField()) {
                    errorText.setText("Enter Valid Salary Range");
                    errorText.setTextColor(Color.parseColor("#EB0101"));
                } else {
                    errorText.setText(""); // Clear any previous error
                    performSearch();
                }
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allEmptyFields()) {
                    errorText.setText("All Fields are empty");
                    errorText.setTextColor(Color.parseColor("#EB0101"));
                } else if (checkSalaryField()) {
                    errorText.setText("Enter Valid Salary Range");
                    errorText.setTextColor(Color.parseColor("#EB0101"));
                } else {
                    errorText.setText(""); // Clear any previous error
                    performSearchForMap();
                }
            }
        });
    }

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
        jobListToMap = new ArrayList<>();
        jobSearchAdapter = new JobSearchAdapter(jobList, this);
        recyclerView.setAdapter(jobSearchAdapter);
    }

    private void performSearch() {
        String title = jobTitle.getText().toString().trim();
        String company = companyName.getText().toString().trim();
        String minSalStr = minSalary.getText().toString().trim();
        String maxSalStr = maxSalary.getText().toString().trim();
        String jobDuration = duration.getText().toString().trim();
        String jobLocation = location.getText().toString().trim().toLowerCase();

        jobList.clear();
        jobSearchAdapter.notifyDataSetChanged();

        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    Job job = jobSnapshot.getValue(Job.class);
                    if (passesAdditionalFilters(job)) {
                        jobList.add(job);
                    }
                }

                if (jobList.isEmpty()) {
                    errorText.setText("No Results Found");
                } else {
                    errorText.setText("");
                }
                jobSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                errorText.setText("Failed to retrieve jobs.");
            }
        });
    }

    private void performSearchForMap() {
        Log.d(TAG, "Starting performSearchForMap");

        // Initialize ArrayLists
        ArrayList<Double> latitudes = new ArrayList<>();
        ArrayList<Double> longitudes = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<Integer> salaries = new ArrayList<>();
        ArrayList<String> durations = new ArrayList<>();
        ArrayList<String> companies = new ArrayList<>();
        ArrayList<String> jobTypes = new ArrayList<>();

        /*
        The following block of code is logic for getting from database
         */
        /*
        jobList.clear();
        jobSearchAdapter.notifyDataSetChanged();
        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {

                    Job job = jobSnapshot.getValue(Job.class);

                    if (passesAdditionalFilters(job)) {

                        jobList.add(job);
                        LocationHelper.LocationResult lr = LocationHelper.getCoordinates(JobSearchParameterActivity.this, job.getLocation());

                        latitudes.add(lr.getLatitude());
                        longitudes.add(lr.getLongitude());
                        titles.add(job.getJobTitle());
                        companies.add(job.getCompanyName());
                        jobTypes.add(job.getJobType());
                        salaries.add(job.getSalary());
                        durations.add(job.getExpectedDuration());
                    }
                }

                if (jobList.isEmpty()) {
                    errorText.setText("No Results Found");
                } else {
                    errorText.setText("");
                    jobSearchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                errorText.setText("Failed to retrieve jobs.");
            }
        });

         */


        // Use test locations for now - will be guaranteed to show markers
        addTestLocations(latitudes, longitudes, titles, companies, jobTypes, salaries, durations);

        Intent intent = new Intent(JobSearchParameterActivity.this, MapActivity.class);

        Log.d(TAG, "Preparing to send " + latitudes.size() + " locations to map");

        // Add data to intent using ArrayList<Serializable>
        intent.putExtra("latitudes", new ArrayList<>(latitudes));
        intent.putExtra("longitudes", new ArrayList<>(longitudes));
        intent.putStringArrayListExtra("titles", new ArrayList<>(titles));
        intent.putIntegerArrayListExtra("salaries", new ArrayList<>(salaries));
        intent.putStringArrayListExtra("durations", new ArrayList<>(durations));
        intent.putStringArrayListExtra("companies", new ArrayList<>(companies));
        intent.putStringArrayListExtra("jobTypes", new ArrayList<>(jobTypes));

        // Log what we're sending
        for (int i = 0; i < latitudes.size(); i++) {
            Log.d(TAG, String.format("Sending location %d: %s at (%f, %f)",
                    i, titles.get(i), latitudes.get(i), longitudes.get(i)));
        }

        startActivity(intent);
    }

    // Helper method to add test locations
    private void addTestLocations(ArrayList<Double> latitudes, ArrayList<Double> longitudes,
                                  ArrayList<String> titles, ArrayList<String> companies,
                                  ArrayList<String> jobTypes, ArrayList<Integer> salaries,
                                  ArrayList<String> durations) {

        // Downtown Halifax
        latitudes.add(44.6488);
        longitudes.add(-63.5752);
        titles.add("Software Developer");
        companies.add("Halifax Tech Hub");
        jobTypes.add("Full Time");
        salaries.add(35);
        durations.add("40 hours");

        // Halifax Shopping Centre
        latitudes.add(44.6350);
        longitudes.add(-63.5917);
        titles.add("Store Manager");
        companies.add("Halifax Mall");
        jobTypes.add("Full Time");
        salaries.add(28);
        durations.add("35 hours");

        // Dalhousie University
        latitudes.add(44.6366);
        longitudes.add(-63.5917);
        titles.add("Research Assistant");
        companies.add("Dalhousie University");
        jobTypes.add("Part Time");
        salaries.add(25);
        durations.add("20 hours");

        // Spring Garden Road
        latitudes.add(44.6427);
        longitudes.add(-63.5751);
        titles.add("Sales Associate");
        companies.add("Spring Garden Shop");
        jobTypes.add("Part Time");
        salaries.add(22);
        durations.add("25 hours");

        Log.d(TAG, "Added " + latitudes.size() + " test locations");
    }

    private boolean passesAdditionalFilters(Job job) {
        String title = jobTitle.getText().toString().trim();
        String company = companyName.getText().toString().trim();
        String minSalStr = minSalary.getText().toString().trim();
        String maxSalStr = maxSalary.getText().toString().trim();
        String jobDuration = duration.getText().toString().trim();
        String jobLocation = location.getText().toString().trim().toLowerCase();

        boolean matches = true;

        if (job != null) {
            if (isValidJobTitle(title) && !job.getJobTitle().equalsIgnoreCase(title)) {
                matches = false;
            }

            if (isValidCompany(company) && !job.getCompanyName().equalsIgnoreCase(company)) {
                matches = false;
            }

            if (isValidLocation(jobLocation)) {
                String jobLocLower = job.getLocation().toLowerCase();
                if (!jobLocLower.contains(jobLocation)) {
                    matches = false;
                }
            }

            if (isValidDuration(jobDuration) && !job.getExpectedDuration().equalsIgnoreCase(jobDuration)) {
                matches = false;
            }

            // Salary filtering
            if (!minSalStr.isEmpty() && !maxSalStr.isEmpty()) {
                int minSal = Integer.parseInt(minSalStr);
                int maxSal = Integer.parseInt(maxSalStr);
                int salary = job.getSalary();
                if (salary < minSal || salary > maxSal) {
                    matches = false;
                }
            } else if (!minSalStr.isEmpty()) {
                int minSal = Integer.parseInt(minSalStr);
                int salary = job.getSalary();
                if (salary < minSal) {
                    matches = false;
                }
            } else if (!maxSalStr.isEmpty()) {
                int maxSal = Integer.parseInt(maxSalStr);
                int salary = job.getSalary();
                if (salary > maxSal) {
                    matches = false;
                }
            }
        }
        return matches;
    }

    public static boolean isValidJobTitle(String title) {
        return title != null && !title.trim().isEmpty();
    }

    public static boolean isValidCompany(String company) {
        return company != null && !company.trim().isEmpty();
    }

    public static boolean isValidSalary(int minSalary, int maxSalary) {
        return minSalary >= 0 && maxSalary >= 0 && minSalary <= maxSalary;
    }

    public static boolean isValidDuration(String duration) {
        return duration != null && !duration.trim().isEmpty();
    }

    public static boolean isValidLocation(String location) {
        return location != null && !location.trim().isEmpty();
    }

    public boolean allEmptyFields() {
        return jobTitle.getText().toString().trim().isEmpty() &&
                companyName.getText().toString().trim().isEmpty() &&
                minSalary.getText().toString().trim().isEmpty() &&
                maxSalary.getText().toString().trim().isEmpty() &&
                duration.getText().toString().trim().isEmpty() &&
                location.getText().toString().trim().isEmpty();
    }

    public boolean checkSalaryField() {
        String minS = minSalary.getText().toString().trim();
        String maxS = maxSalary.getText().toString().trim();

        if (minS.isEmpty() || maxS.isEmpty()) {
            return false;
        } else {
            return !isValidSalary(Integer.parseInt(minS), Integer.parseInt(maxS));
        }
    }
}