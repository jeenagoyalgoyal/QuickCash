package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import android.util.Log;

public class ApplicationPageActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextMessage;
    private Button buttonApply;
    private DatabaseReference databaseReference;
    private TextView textViewJobDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_page);

        // Retrieve job details from the intent
        String jobId = getIntent().getStringExtra("jobId");
        String jobTitle = getIntent().getStringExtra("jobTitle");
        String companyName = getIntent().getStringExtra("companyName");
        String userId = getIntent().getStringExtra("userEmail");

        // Use this userId wherever required
        if (userId != null) {
            userId = userId.replace(".", ","); // Sanitize email for Firebase if necessary
        }

        // Initialize UI components
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonApply = findViewById(R.id.buttonApply);
        textViewJobDetails = findViewById(R.id.textViewJobDetails);

        // Set the job title and company name in the TextView
        if (jobTitle != null && companyName != null) {
            textViewJobDetails.setText(String.format("Applying for: %s at %s", jobTitle, companyName));
        } else {
            textViewJobDetails.setText("Job details not available");
        }

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Jobs").child(jobId).child("applications");

        // Set click listener for the Apply button
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitApplication();
            }
        });
    }

    private void submitApplication() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String message = editTextMessage.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(message)) {
            editTextMessage.setError("Message is required");
            return;
        }

        // Create a map to store application data
        Map<String, Object> applicationData = new HashMap<>();
        applicationData.put("Applicant Name", name);
        applicationData.put("Applicant Email", email);
        applicationData.put("Cover Letter", message);

        // Save the application data in the database
        databaseReference.push().setValue(applicationData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ApplicationPageActivity.this, "Application submitted successfully", Toast.LENGTH_SHORT).show();

                        // Clear input fields
                        editTextName.setText("");
                        editTextEmail.setText("");
                        editTextMessage.setText("");

                        // Also save the job under the user's applied jobs
                        String userId = getIntent().getStringExtra("userEmail");
                        String jobTitle = getIntent().getStringExtra("jobTitle");
                        String companyName = getIntent().getStringExtra("companyName");

                        if (userId != null) {
                            userId = userId.replace(".", ","); // Sanitize email for Firebase
                            saveJobUnderUser(userId, jobTitle, companyName);
                        }

                        // Navigate back to EmployeeHomepageActivity
                        Intent intent = new Intent(ApplicationPageActivity.this, EmployeeHomepageActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ApplicationPageActivity.this, "Failed to submit application", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveJobUnderUser(String userId, String jobTitle, String companyName) {
        DatabaseReference userReference = FirebaseDatabase.getInstance()
                .getReference("Users").child(userId).child("appliedJobs");

        // Sanitize the company name to be a valid Firebase key
        String sanitizedCompanyName = companyName.replace(".", ",").replace("#", "").replace("$", "").replace("[", "").replace("]", "");

        // Create a map for the job details
        Map<String, Object> jobData = new HashMap<>();
        jobData.put("jobTitle", jobTitle);
        jobData.put("companyName", companyName);

        // Use the sanitized company name as the ID
        userReference.child(sanitizedCompanyName).setValue(jobData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i("ApplicationPageActivity", "Job successfully added to applied jobs");
                    } else {
                        Log.e("ApplicationPageActivity", "Failed to add job to applied jobs");
                    }
                });
    }


}
