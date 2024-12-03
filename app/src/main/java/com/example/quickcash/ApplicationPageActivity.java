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

/**
 * The {@code ApplicationPageActivity} class handles the application submission process for a job.
 * It allows users to view job details, input their information, and submit an application
 * to Firebase while ensuring input validation and saving the application under the user's profile.
 */
public class ApplicationPageActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextMessage;
    private Button buttonApply;
    private DatabaseReference databaseReference;
    private TextView textViewJobDetails;
    private FirebaseAuth mAuth;
    private String email;
    String uniqueApplicationKey;

    /**
     * Initializes the activity, sets up the user interface components, and prepares
     * Firebase database references.
     *
     * @param savedInstanceState If the activity is being re-initialized, this contains the saved state data.
     */
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
                submitApplication(userId);
            }
        });
    }

    /**
     * Handles the submission of a job application. Validates user input, creates application data,
     * and saves it to Firebase.
     */
    private void submitApplication(String userId) {
        String name = editTextName.getText().toString().trim();
        String email2 = editTextEmail.getText().toString().trim();
        String message = editTextMessage.getText().toString().trim();


        // Validate input
        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(email2)) {
            editTextEmail.setError("Email is required");
            return;
        }
        if (!isValidEmail(email2)) {
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
        applicationData.put("employeeID", userId);
        uniqueApplicationKey = databaseReference.push().getKey();

        if (uniqueApplicationKey != null) {
            applicationData.put("applicationId", uniqueApplicationKey); // Store the applicationId in the data

            // Push application data to Firebase under the generated applicationId
            databaseReference.child(uniqueApplicationKey).setValue(applicationData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ApplicationPageActivity.this, "Application submitted successfully", Toast.LENGTH_SHORT).show();

                            // Clear input fields
                            editTextName.setText("");
                            editTextEmail.setText("");
                            editTextMessage.setText("");

                            // Also save the job under the user's applied jobs

                            String jobTitle = getIntent().getStringExtra("jobTitle");
                            String companyName = getIntent().getStringExtra("companyName");
                            final String userId2 = userId.replace(".", ",");

                            saveJobUnderUser(userId2, jobTitle, companyName, userId2); // Pass applicationId here


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

    /**
     * Saves the applied job details under the user's profile in Firebase.
     *
     * @param userId              The ID of the user.
     * @param jobTitle            The title of the job.
     * @param companyName         The name of the company.
     * @param empID               The employee ID of the user.
     */
    private void saveJobUnderUser(String userId, String jobTitle, String companyName, String empID) {
        DatabaseReference userReference = FirebaseDatabase.getInstance()
                .getReference("Users").child(userId).child("appliedJobs");


        // Create a map for the job details
        Map<String, Object> jobData = new HashMap<>();
        jobData.put("jobTitle", jobTitle);
        jobData.put("companyName", companyName);
        jobData.put("Status", "Applied");
        jobData.put("employeeId", empID);
        jobData.put("applicationId", uniqueApplicationKey);


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

    /**
     * Validates an email address format.
     *
     * @param email The email address to validate.
     * @return {@code true} if the email format is valid; {@code false} otherwise.
     */
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
}

