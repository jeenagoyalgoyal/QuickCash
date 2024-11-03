package com.example.quickcash.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quickcash.R;
<<<<<<< HEAD
=======
import com.example.quickcash.ui.models.Job;
>>>>>>> c8452335f077f68a71191e31cd517c40ec38c3f0
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;

=======
>>>>>>> c8452335f077f68a71191e31cd517c40ec38c3f0
public class JobSubmissionActivity extends AppCompatActivity {

    private TextView formText;
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
    private TextView employerIdTextView;
    private DatabaseReference databaseReference;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.job_submission);

        databaseReference = FirebaseDatabase.getInstance().getReference("Jobs");

        Intent intentJobSub = getIntent();
        email = intentJobSub.getStringExtra("email");

        initializeViews();
        setupSpinners();
        setupDatePicker();
        setupSubmitButton();
    }

    private void initializeViews() {
        employerIdTextView = findViewById(R.id.employerIdTextView);
        employerIdTextView.setText("Employer ID: " + email);

        formText = findViewById(R.id.jobSub);
        jobTitle = findViewById(R.id.jobTitle);
        companyName = findViewById(R.id.companyName);
        jobType = findViewById(R.id.spinnerJobType);
        requirements = findViewById(R.id.requirementText);
        salary = findViewById(R.id.jobTitleText);
        jobUrgency = findViewById(R.id.spinnerUrgency);
        location = findViewById(R.id.locationJob);
        expectedDuration = findViewById(R.id.expectedDuration);
        startDate = findViewById(R.id.startDate);
        submitButton = findViewById(R.id.jobSubmissionButton);
    }

    private void setupSpinners() {
        // Job Type Spinner
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        typeAdapter.add("Select job type");
        typeAdapter.add("Full-time");
        typeAdapter.add("Part-time");
        typeAdapter.add("Internship");
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobType.setAdapter(typeAdapter);

        // Urgency Spinner
        ArrayAdapter<String> urgencyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        urgencyAdapter.add("Select urgency");
        urgencyAdapter.add("High");
        urgencyAdapter.add("Medium");
        urgencyAdapter.add("Low");
        urgencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobUrgency.setAdapter(urgencyAdapter);
    }

    private void setupDatePicker() {
        startDate.setOnClickListener(view -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().build();
            datePicker.addOnPositiveButtonClickListener(selection ->
                    startDate.setText(datePicker.getHeaderText()));
            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        });
    }

    private void setupSubmitButton() {
        submitButton.setOnClickListener(v -> submitJobPosting());
    }

    private void submitJobPosting() {
        if (!validateInputs()) {
            return;
        }

        String jobId = databaseReference.push().getKey();
        if (jobId == null) {
            Toast.makeText(this, "Error generating job ID", Toast.LENGTH_SHORT).show();
            return;
        }

        Job job = createJobObject(jobId);

        databaseReference.child(jobId).setValue(job)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Job posted successfully!", Toast.LENGTH_SHORT).show();
                        resetForm();
                        navigateToEmployerHomepage();
                    } else {
                        Toast.makeText(this, "Failed to post job: " +
                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateInputs() {
        if (jobTitle.getText().toString().trim().isEmpty()) {
            showError(jobTitle, "Job Title is required");
            return false;
        }

        if (companyName.getText().toString().trim().isEmpty()) {
            showError(companyName, "Company Name is required");
            return false;
        }

        if (jobType.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a Job Type", Toast.LENGTH_SHORT).show();
            return false;
        }

        String salaryText = salary.getText().toString().trim();
        if (salaryText.isEmpty()) {
            showError(salary, "Salary is required");
            return false;
        }
        try {
            int salaryValue = Integer.parseInt(salaryText);
            if (salaryValue <= 0) {
                showError(salary, "Salary must be positive");
                return false;
            }
        } catch (NumberFormatException e) {
            showError(salary, "Invalid salary format");
            return false;
        }

        if (jobUrgency.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select Urgency", Toast.LENGTH_SHORT).show();
            return false;
        }

        String locationText = location.getText().toString().trim();
        if (locationText.isEmpty()) {
            showError(location, "Location is required");
            return false;
        }
        if (locationText.split("\\s+").length < 3) {
            showError(location, "Location must be more specific (at least 3 words)");
            return false;
        }

        if (expectedDuration.getText().toString().trim().isEmpty()) {
            showError(expectedDuration, "Duration is required");
            return false;
        }

        if (startDate.getText().toString().equals("Start Date")) {
            Toast.makeText(this, "Please select a Start Date", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void showError(EditText field, String message) {
        field.setError(message);
        field.requestFocus();
    }

    private Job createJobObject(String jobId) {
        String employerId = email.replace(".", ",");

        Job job = new Job();
        job.setJobId(jobId);
        job.setJobTitle(jobTitle.getText().toString().trim());
        job.setCompanyName(companyName.getText().toString().trim());
        job.setJobType(jobType.getSelectedItem().toString());
        job.setRequirements(requirements.getText().toString().trim());
        job.setSalary(Integer.parseInt(salary.getText().toString().trim()));
        job.setUrgency(jobUrgency.getSelectedItem().toString());
        job.setLocation(location.getText().toString().trim());
        job.setDuration(expectedDuration.getText().toString().trim());
        job.setStartDate(startDate.getText().toString());
        job.setEmployerId(employerId);

        return job;
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

    private void navigateToEmployerHomepage() {
        Intent intent = new Intent(this, EmployerHomepageActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }
}