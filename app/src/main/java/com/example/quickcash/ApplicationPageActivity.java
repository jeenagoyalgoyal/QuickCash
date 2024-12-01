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

import com.google.firebase.auth.FirebaseAuth;
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
    private FirebaseAuth mAuth;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_page);

        // Retrieve job details from the intent
        String jobId = getIntent().getStringExtra("jobId");
        String jobTitle = getIntent().getStringExtra("jobTitle");
        String companyName = getIntent().getStringExtra("companyName");
        String userId = getIntent().getStringExtra("userEmail");



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
        String email2 = editTextEmail.getText().toString().trim();
        String message = editTextMessage.getText().toString().trim();
        //ID is retrieved
        this.mAuth = FirebaseAuth.getInstance();
        this.email = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;
        String empID = email.replace(".", ",");

        // Validate input
        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email is required");
            return;
        }
        if (!isValidEmail(email)) {
            editTextEmail.setError("Invalid email format");
            return;
        }
        if (TextUtils.isEmpty(message)) {
            editTextMessage.setError("Message is required");
            return;
        }

        // Create a map to store application data
        Map<String, Object> applicationData = new HashMap<>();
        applicationData.put("Applicant Name", name);
        applicationData.put("Applicant Email", email2);
        applicationData.put("Cover Letter", message);
        applicationData.put("Status", "Applied"); // Add the status variable
        applicationData.put("employeeID", empID);
        String applicationId = databaseReference.push().getKey();

        if (applicationId != null) {
            applicationData.put("applicationId", applicationId); // Store the applicationId in the data

            // Push application data to Firebase under the generated applicationId
            databaseReference.child(applicationId).setValue(applicationData)
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
                                saveJobUnderUser(userId, jobTitle, companyName, applicationId, empID); // Pass applicationId here
                            }

                            // Navigate back to EmployeeHomepageActivity
                            Intent intent = new Intent(ApplicationPageActivity.this, JobSearchParameterActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ApplicationPageActivity.this, "Failed to submit application", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void saveJobUnderUser(String userId, String jobTitle, String companyName, String applicationId, String empID) {
        DatabaseReference userReference = FirebaseDatabase.getInstance()
                .getReference("Users").child(userId).child("appliedJobs");

        // Generate a unique key for each application under the user
        String uniqueApplicationKey = userReference.push().getKey();


        // Create a map for the job details
        Map<String, Object> jobData = new HashMap<>();
        jobData.put("jobTitle", jobTitle);
        jobData.put("companyName", companyName);
        jobData.put("Status", "Applied");
        jobData.put("employeeId", empID);
        jobData.put("applicationId", applicationId);


        if (uniqueApplicationKey != null) {
            // Save the job application under the unique key
            userReference.child(uniqueApplicationKey).setValue(jobData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.i("ApplicationPageActivity", "Job successfully added to applied jobs");
                        } else {
                            Log.e("ApplicationPageActivity", "Failed to add job to applied jobs");
                        }
                    });
        } else {
            Log.e("ApplicationPageActivity", "Failed to generate unique key for job application");
        }
    }


    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
}
