package com.example.quickcash.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quickcash.R;
import com.example.quickcash.models.JobLocation;
import com.example.quickcash.utils.LocationHelper;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobSubmissionActivity extends AppCompatActivity {

    // Job Submission Form title
    private TextView formText;

    // Inputs for the employer
    private EditText jobTitle;
    private Spinner jobType;
    private Spinner jobUrgency;
    private EditText companyName;
    private EditText requirements;
    private EditText salary;
    private EditText location;
    private EditText expectedDuration;
    private Button startDate;

    private TextView errorJSRequirement;
    private TextView errorJSSalary;
    private TextView errorJSUrgency;
    private TextView errorJSLocation;
    private TextView errorJSDuration;

    // Button to submit
    private Button submitButton;

    private DatabaseReference databaseReference = null;
    private TextView employerIdTextView;
    private String email;

    // ProgressDialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.job_submission);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Jobs");

        // Send the email for storing as employerID
        Intent intentJobSub = getIntent();
        email = intentJobSub.getStringExtra("email");

        // Initialize the employer ID TextView
        employerIdTextView = findViewById(R.id.employerIdTextView);

        // Display employerId on the form
        employerIdTextView.setText("Employer ID: " + email);

        // Form title
        formText = findViewById(R.id.jobSub);

        // The inputs from employer
        jobTitle = findViewById(R.id.jobTitle);
        companyName = findViewById(R.id.companyName);
        jobType = findViewById(R.id.spinnerJobType);
        requirements = findViewById(R.id.requirementText);
        salary = findViewById(R.id.jobTitleText);
        jobUrgency = findViewById(R.id.spinnerUrgency);
        location = findViewById(R.id.locationJob);
        expectedDuration = findViewById(R.id.expectedDuration);
        startDate = findViewById(R.id.startDate);

        // Button to submit the job posting
        submitButton = findViewById(R.id.jobSubmissionButton);

        // Errors
        errorJSRequirement = findViewById(R.id.errorJSRequirement);
        errorJSSalary = findViewById(R.id.errorJSSalary);
        errorJSUrgency = findViewById(R.id.errorJSUrgency);
        errorJSLocation = findViewById(R.id.errorJSLocation);
        errorJSDuration = findViewById(R.id.errorJSDuration);

        // Array list for the job type
        List<String> typeList = new ArrayList<>();
        typeList.add(0, "Select job type");
        typeList.add("Full-time");
        typeList.add("Part-time");
        typeList.add("Internship");

        // Array list for the urgency
        List<String> urgencyList = new ArrayList<>();
        urgencyList.add(0, "Select urgency");
        urgencyList.add("High");
        urgencyList.add("Medium");
        urgencyList.add("Low");

        // Create ArrayAdapter for each spinner
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobType.setAdapter(typeAdapter);

        ArrayAdapter<String> urgencyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, urgencyList);
        urgencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobUrgency.setAdapter(urgencyAdapter);

        // Set up a date picker for the start date
        startDate.setOnClickListener(view -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().build();
            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            datePicker.addOnPositiveButtonClickListener(selection -> startDate.setText(datePicker.getHeaderText()));
        });

        // When button for submission is clicked, we submit the job to database
        submitButton.setOnClickListener(v -> submitJobPosting());
    }

    private void submitJobPosting() {
        if (!validateInputs()) {
            return;
        }

        // Initialize and show the ProgressDialog
        progressDialog = new ProgressDialog(JobSubmissionActivity.this);
        progressDialog.setMessage("Posting job...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Get current location
        LocationHelper.getCurrentLocation(this, new LocationHelper.CustomLocationCallback() {
            @Override
            public void onLocationReceived(double latitude, double longitude) {
                createAndSaveJob(latitude, longitude, progressDialog);
            }

            @Override
            public void onLocationError(String error) {
                // Fallback to geocoding the entered location
                LocationHelper.GeocodingResult result = LocationHelper.getCoordinates(
                        JobSubmissionActivity.this,
                        location.getText().toString().trim()
                );
                createAndSaveJob(result.getLatitude(), result.getLongitude(), progressDialog);
            }
        });
    }

    private void createAndSaveJob(double latitude, double longitude, ProgressDialog progressDialog) {
        try {
            // Generate unique job ID
            String jobId = databaseReference.push().getKey();
            if (jobId == null) {
                throw new Exception("Failed to generate job ID");
            }

            // Create job data using a HashMap to ensure proper Firebase structure
            Map<String, Object> jobData = new HashMap<>();
            jobData.put("companyName", companyName.getText().toString().trim());
            jobData.put("employerId", email.replace(".", ","));
            jobData.put("expectedDuration", expectedDuration.getText().toString().trim());
            jobData.put("jobId", jobId);
            jobData.put("jobTitle", jobTitle.getText().toString().trim());
            jobData.put("jobType", jobType.getSelectedItem().toString());
            jobData.put("requirements", requirements.getText().toString().trim());
            jobData.put("salary", Integer.parseInt(salary.getText().toString().trim()));
            jobData.put("startDate", startDate.getText().toString().trim());
            jobData.put("urgency", jobUrgency.getSelectedItem().toString());

            // Save location data directly in the job
            jobData.put("location", location.getText().toString().trim());
            jobData.put("lat", latitude);
            jobData.put("lng", longitude);

            // Save to Firebase
            databaseReference.child(jobId).setValue(jobData)
                    .addOnSuccessListener(aVoid -> {
                        progressDialog.dismiss();
                        Toast.makeText(JobSubmissionActivity.this,
                                "Job Posted Successfully!", Toast.LENGTH_SHORT).show();
                        Log.d("JobSubmission", "Job saved successfully with location: " +
                                location.getText().toString().trim() +
                                " at coordinates: " + latitude + "," + longitude);
                        resetForm();
                        navigateToEmployerHomepage();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(JobSubmissionActivity.this,
                                "Failed to post job: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("JobSubmission", "Failed to save job: " + e.getMessage());
                    });

        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("JobSubmission", "Error creating job: " + e.getMessage());
        }
    }

    private void navigateToEmployerHomepage() {
        Intent intentBackToEmployerPage = new Intent(JobSubmissionActivity.this, EmployerHomepageActivity.class);
        intentBackToEmployerPage.putExtra("employerID", email.replace(".", ","));
        intentBackToEmployerPage.putExtra("email", email);
        startActivity(intentBackToEmployerPage);
    }

    private boolean validateInputs() {
        return true;
    }

    private void resetForm() {
        jobTitle.setText("");
        companyName.setText("");
        jobType.setSelection(0);
        requirements.setText("");
        salary.setText("");
        jobUrgency.setSelection(0);
        location.setText("");
        expectedDuration.setText("");
        startDate.setText("Start Date");
    }

    public static class Job {
        public String jobTitle, companyName, jobType, requirements, urgency, location, expectedDuration, startDate, employerId, jobId;
        public int salary;
        public JobLocation jobLocation;

        public Job(String companyName, String employerId, String expectedDuration, String jobId,
                   String jobTitle, String jobType, JobLocation jobLocation, String requirements,
                   int salary, String startDate, String urgency) {
            this.companyName = companyName;
            this.employerId = employerId;
            this.expectedDuration = expectedDuration;
            this.jobId = jobId;
            this.jobTitle = jobTitle;
            this.jobType = jobType;
            this.jobLocation = jobLocation;
            this.requirements = requirements;
            this.salary = salary;
            this.startDate = startDate;
            this.urgency = urgency;
        }
    }
}
