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

    private TextView formText;
    private Spinner jobType;
    private Spinner jobUrgency;
    private EditText companyName;
    private EditText requirements;
    private EditText salary;
    private EditText location;
    private EditText expectedDuration;
    private Button startDate;
    private Button submitButton;

    private DatabaseReference databaseReference = null;
    private String employerId = null;


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.job_submission);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Jobs");

        Intent intent = getIntent();

        formText = findViewById(R.id.jobSub);
        jobType = findViewById(R.id.spinnerJobType);
        jobUrgency = findViewById(R.id.spinnerUrgency);
        companyName = findViewById(R.id.companyName);
        requirements = findViewById(R.id.requirementText);
        salary = findViewById(R.id.salaryText);
        location = findViewById(R.id.locationJob);
        expectedDuration = findViewById(R.id.expectedDuration);
        startDate = findViewById(R.id.startDate);
        submitButton = findViewById(R.id.jobSubmissionButton);


        List<String> typeList = new ArrayList<>();
        typeList.add(0, "Select job type");
        typeList.add("Full-time");
        typeList.add("Part-time");
        typeList.add("Internship");

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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitJobPosting();
            }
        });

    }

    // Submitting the job to the firebase database
    private void submitJobPosting() {
        String jobTitleText = formText.getText().toString().trim();
        String companyNameText = companyName.getText().toString().trim();
        String jobTypeText = jobType.getSelectedItem().toString();
        String requirementsText = requirements.getText().toString().trim();
        int salaryValue = Integer.parseInt(salary.getText().toString().trim());
        String urgencyText = jobUrgency.getSelectedItem().toString();
        String locationText = location.getText().toString().trim();
        String durationText = expectedDuration.getText().toString().trim();
        String startDateText = startDate.getText().toString().trim();
        // ID for database
        String jobId = databaseReference.push().getKey();

        Job job = new Job(jobTitleText, companyNameText, jobTypeText, requirementsText,
                salaryValue, urgencyText, locationText, durationText, startDateText,
                employerId, jobId);

        if (jobId != null) {
            databaseReference.child(jobId).setValue(job)
                    .addOnCompleteListener(task -> {
                        // Job is posted to database, gives user the message
                        if (task.isSuccessful()) {
                            Toast.makeText(JobSubmission.this, "Job posted successfully!", Toast.LENGTH_SHORT).show();
                        }
                        // Else, failed to post
                        else {
                            Toast.makeText(JobSubmission.this, "Failed to post job.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
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


