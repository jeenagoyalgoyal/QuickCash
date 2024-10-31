package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.List;

public class JobSubmission extends AppCompatActivity {

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

    // Button to submit
    private Button submitButton;

    private DatabaseReference databaseReference = null;

    private TextView employerIdTextView;


    private String email;

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
        salary = findViewById(R.id.salaryText);
        jobUrgency = findViewById(R.id.spinnerUrgency);
        location = findViewById(R.id.locationJob);
        expectedDuration = findViewById(R.id.expectedDuration);
        startDate = findViewById(R.id.startDate);

        // Button to submit the job posting
        submitButton = findViewById(R.id.jobSubmissionButton);


        // Array list for the job type
        List<String> typeList = new ArrayList<>();
        typeList.add(0, "Select job type");
        typeList.add("Full-time");
        typeList.add("Part-time");
        typeList.add("Internship");

        // Array list for the urgency
        List <String> urgencyList = new ArrayList<>();
        urgencyList.add(0,"Select urgency");
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
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().build();

                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
                datePicker.addOnPositiveButtonClickListener(selection -> {
                    startDate.setText(datePicker.getHeaderText());
                });
            }
        });

        // When button for submission is clicked, we submit the job to database
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitJobPosting();
            }
        });

    }

    // Submitting the job to the firebase database
    private void submitJobPosting() {
        // Get text from form inputs
        String jobTitleText = jobTitle.getText().toString().trim();
        String companyNameText = companyName.getText().toString().trim();
        String jobTypeText = jobType.getSelectedItem().toString();
        String requirementsText = requirements.getText().toString().trim();
        String salaryText = salary.getText().toString().trim();
        String urgencyText = jobUrgency.getSelectedItem().toString();
        String locationText = location.getText().toString().trim();
        String durationText = expectedDuration.getText().toString().trim();
        String startDateText = startDate.getText().toString().trim();

        // Job Title
        if (jobTitleText.isEmpty()) {
            jobTitle.setError("Job Title is required.");
            jobTitle.requestFocus();
            return;
        }

        // Company Name
        if (companyNameText.isEmpty()) {
            companyName.setError("Company Name is required.");
            companyName.requestFocus();
            return;
        }

        // Job Type
        if (jobTypeText.equals("Select job type")) {
            Toast.makeText(this, "Please select a Job Type.", Toast.LENGTH_SHORT).show();
            jobType.requestFocus();
            return;
        }

        // Salary - Check if empty and validate as a positive integer
        if (salaryText.isEmpty()) {
            salary.setError("Salary is required.");
            salary.requestFocus();
            return;
        }
        int salaryValue;
        try {
            salaryValue = Integer.parseInt(salaryText);
            if (salaryValue <= 0) {
                salary.setError("Salary must be a positive number.");
                salary.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            salary.setError("Please enter a valid integer for Salary.");
            salary.requestFocus();
            return;
        }

        // Urgency
        if (urgencyText.equals("Select urgency")) {
            Toast.makeText(this, "Please select Urgency.", Toast.LENGTH_SHORT).show();
            jobUrgency.requestFocus();
            return;
        }

        // Location
        if (locationText.isEmpty()) {
            location.setError("Location is required.");
            location.requestFocus();
            return;
        }

        // Expected Duration
        if (durationText.isEmpty()) {
            expectedDuration.setError("Expected Duration is required.");
            expectedDuration.requestFocus();
            return;
        }

        if (startDateText.isEmpty() || startDateText.equals("Start Date")) {
            Toast.makeText(this, "Please select a Start Date.", Toast.LENGTH_SHORT).show();
            startDate.requestFocus();
            return;
        }

        // EmployerID is the user email
        String employerId = email.replace(".", ",");

        String jobId = databaseReference.push().getKey();

        Job job = new Job(jobTitleText, companyNameText, jobTypeText, requirementsText,
                salaryValue, urgencyText, locationText, durationText, startDateText,
                employerId, jobId);

        if (jobId != null) {
            databaseReference.child(jobId).setValue(job)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(JobSubmission.this, "Job Submission Successful!", Toast.LENGTH_SHORT).show();

                            // Reset the input fields when the job is posted
                            resetForm();

                            // Send the user back to the homepage after submitting the job posting
                            Intent intentBackToEmployerPage = new Intent(JobSubmission.this, EmployerHomepageActivity.class);
                            intentBackToEmployerPage.putExtra("employerID", employerId);
                            intentBackToEmployerPage.putExtra("email",email);
                            startActivity(intentBackToEmployerPage);
                        }
                        
                        else {
                            Toast.makeText(JobSubmission.this, "Failed to post job.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // This is a function to clear the data input, set back to default
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

    // Class for the jobs
    public static class Job {
        public String jobTitle, companyName, jobType, requirements, urgency, location, expectedDuration, startDate, employerId, jobId;
        public int salary;

        // Job function for database activity
        public Job(String jobTitle, String companyName, String jobType, String requirements,
                   int salary, String urgency, String location, String expectedDuration,
                   String startDate, String employerId, String jobId) {
            this.jobTitle = jobTitle;
            this.companyName = companyName;
            this.jobType = jobType;
            this.requirements = requirements;
            this.salary = salary;
            this.urgency = urgency;
            this.location = location;
            this.expectedDuration = expectedDuration;
            this.startDate = startDate;
            this.employerId = employerId;
            this.jobId = jobId;
        }
    }
}


