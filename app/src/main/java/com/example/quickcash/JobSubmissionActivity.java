package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quickcash.Firebase.JobCRUD;
import com.example.quickcash.model.Job;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to submit jobs, posting them in the database for users
 */
public class JobSubmissionActivity extends AppCompatActivity {

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
    private Button submitButton;

    private FirebaseDatabase databaseReference = null;
    private JobCRUD jobCRUD;

    private String email;

    /**
     * On create, initialize the job submission form
     * @param savedInstance
     */
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.job_submission);

        init();
        initDropdowns();

        // Set up a date picker for the start date
        startDate.setOnClickListener(new View.OnClickListener() {
            /**
             * Creates the date picker for the start date of the job
             * @param view
             */
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

    /**
     * Initializes the input fields and database
     */
    public void init(){
        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance();
        jobCRUD = new JobCRUD(databaseReference);

        // Send the email for storing as employerID
        Intent intentJobSub = getIntent();
        email = intentJobSub.getStringExtra("email");

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
    }

    /**
     * Initializes the spinners fields
     */
    public void initDropdowns(){
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
    }

    /**
     * Method used to submit jobs to the database
     */
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

        if(!checkFields(jobTitleText, companyNameText, jobTypeText, requirementsText,
                salaryText, urgencyText, locationText, durationText,
                startDateText)){
            return;
        }

        int salaryValue = Integer.parseInt(salaryText);

        // EmployerID is the user email
        String employerId = email.replace(".", ",");

        String jobId = null;

        Job job = new Job();

        job.setAllField(jobTitleText, companyNameText, jobTypeText, requirementsText,
                        salaryValue, urgencyText, locationText, durationText, startDateText,
                        employerId, jobId);

        jobCRUD.submitJob(job).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Job Submission Successful!", Toast.LENGTH_SHORT).show();
                        resetForm();

                        Intent intentBackToEmployerPage = new Intent(JobSubmissionActivity.this, EmployerHomepageActivity.class);
                        intentBackToEmployerPage.putExtra("employerID", employerId);
                        intentBackToEmployerPage.putExtra("email",email);
                        startActivity(intentBackToEmployerPage);
                    } else {
                        Toast.makeText(this, "Failed to post job.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Checks the fields to see if any of the fields are not filled out
     * @param jobTitleText
     * @param companyNameText
     * @param jobTypeText
     * @param requirementsText
     * @param salaryText
     * @param urgencyText
     * @param locationText
     * @param durationText
     * @param startDateText
     * @return
     */
    private boolean checkFields(String jobTitleText, String companyNameText, String jobTypeText, String requirementsText,
                             String salaryText, String urgencyText, String locationText, String durationText,
                             String startDateText){
        boolean match = true;

        // Job Title
        if (jobTitleText.isEmpty()) {
            jobTitle.setError("Job Title is required.");
            jobTitle.requestFocus();
            match = false;
        }

        // Company Name
        if (companyNameText.isEmpty()) {
            companyName.setError("Company Name is required.");
            companyName.requestFocus();
            match = false;
        }

        // Job Type
        if (jobTypeText.equals("Select job type")) {
            Toast.makeText(this, "Please select a Job Type.", Toast.LENGTH_SHORT).show();
            jobType.requestFocus();
            match = false;
        }

        //Company Name
        if (requirementsText.isEmpty()) {
            requirements.setError("Enter Requirements");
            requirements.requestFocus();
            match = false;
        }

        // Salary - Check if empty and validate as a positive integer
        if (salaryText.isEmpty()) {
            salary.setError("Salary is required.");
            salary.requestFocus();
            match = false;
        }else {
            int salaryValue = Integer.parseInt(salaryText);
            try {
                salaryValue = Integer.parseInt(salaryText);
                if (salaryValue <= 0) {
                    salary.setError("Salary must be a positive number.");
                    salary.requestFocus();
                    match = false;
                }
            } catch (NumberFormatException e) {
                salary.setError("Please enter a valid integer for Salary.");
                salary.requestFocus();
                match = false;
            }
        }

        // Urgency
        if (urgencyText.equals("Select urgency")) {
            Toast.makeText(this, "Please select Urgency.", Toast.LENGTH_SHORT).show();
            jobUrgency.requestFocus();
            match = false;
        }

        // Location
        if (locationText.isEmpty()) {
            location.setError("Location is required.");
            location.requestFocus();
            match = false;
        }

        // Expected Duration
        if (durationText.isEmpty()) {
            expectedDuration.setError("Expected Duration is required.");
            expectedDuration.requestFocus();
            match = false;
        }

        if (startDateText.isEmpty() || startDateText.equals("Start Date")) {
            Toast.makeText(this, "Please select a Start Date.", Toast.LENGTH_SHORT).show();
            startDate.requestFocus();
            match = false;
        }

        return match;
    }

    /**
     * Used to reset the form, clearing all input
     */
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
}


