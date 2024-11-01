package com.example.quickcash.ui.activities;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.ui.adapter.JobSearchAdapter;
import com.example.quickcash.ui.models.JobToMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class JobSearchParameterToMap {

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
    private List<JobToMap> jobList;
    private DatabaseReference jobsRef;

    private void performSearch() {

        // Get search parameters
        String title = jobTitle.getText().toString().trim();
        String company = companyName.getText().toString().trim();
        String minSalStr = minSalary.getText().toString().trim();
        String maxSalStr = maxSalary.getText().toString().trim();
        String jobDuration = duration.getText().toString().trim();
        String jobLocation = location.getText().toString().trim();

        Query query = jobsRef;

        // Apply filters based on non-empty inputs
        if (isValidJobTitle(title)) {
            query = query.orderByChild("jobTitle").equalTo(title);
        } else if (isValidCompany(company)) {
            query = query.orderByChild("companyName").equalTo(company);
        } else if (isValidLocation(jobLocation)) {
            query = query.orderByChild("location").equalTo(jobLocation);
        } else if (!minSalStr.isEmpty() && !maxSalStr.isEmpty()) {
            int minSal = Integer.parseInt(minSalStr);
            int maxSal = Integer.parseInt(maxSalStr);
            query = query.orderByChild("salary").startAt(minSal).endAt(maxSal);
        } else if (!minSalStr.isEmpty()) {
            int minSal = Integer.parseInt(minSalStr);
            query = query.orderByChild("salary").startAt(minSal);
        } else if (!maxSalStr.isEmpty()) {
            int maxSal = Integer.parseInt(maxSalStr);
            query = query.orderByChild("salary").endAt(maxSal);
        } else if (!isValidDuration(jobDuration)) {
            query = query.orderByChild("expectedDuration").equalTo(jobDuration);
        }

        // Clear previous search results
        jobList.clear();
        jobSearchAdapter.notifyDataSetChanged();

        // Attach a listener to read the data
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jobList.clear(); // Clear the list before adding new items
                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    JobToMap job = jobSnapshot.getValue(JobToMap.class);
                    // Additional filtering if necessary
                    if (passesAdditionalFilters(job)) {
                        jobList.add(job);
                    }
                }
                if (jobList.isEmpty()) {
                    errorText.setText("No Results Found");
                } else {
                    errorText.setText(""); // Clear any previous error
                }
                jobSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                errorText.setText("Failed to retrieve jobs.");
            }
        });
    }


    private boolean passesAdditionalFilters(JobToMap job) {

        // Get the user input again
        String title = jobTitle.getText().toString().trim();
        String company = companyName.getText().toString().trim();
        String minSalStr = minSalary.getText().toString().trim();
        String maxSalStr = maxSalary.getText().toString().trim();
        String jobDuration = duration.getText().toString().trim();
        String jobLocation = location.getText().toString().trim();

        boolean matches = true;

        if (isValidJobTitle(title) && !job.getJobTitle().equalsIgnoreCase(title)) {
            matches = false;
        }

        if (isValidCompany(company) && !job.getCompanyName().equalsIgnoreCase(company)) {
            matches = false;
        }

        if (isValidLocation(jobLocation) && !job.getLocation().equalsIgnoreCase(jobLocation)) {
            matches = false;
        }

        if (isValidDuration(jobDuration) && !job.getDuration().equalsIgnoreCase(jobDuration)) {
            matches = false;
        }

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
            errorText.setText(salary + " " + maxSal);
            if (salary > maxSal) {
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
