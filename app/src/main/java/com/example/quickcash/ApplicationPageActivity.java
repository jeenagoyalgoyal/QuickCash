package com.example.quickcash;

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

        // Generate a unique ID for the application
        String applicationId = databaseReference.push().getKey();

        // Create a map to store application data
        Map<String, Object> applicationData = new HashMap<>();

        applicationData.put("Applicant Name", name);
        applicationData.put("Applicant Email", email);
        applicationData.put("Cover Letter", message);

        // Save the application data in the database
        if (applicationId != null) {
            databaseReference.child(applicationId).setValue(applicationData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ApplicationPageActivity.this, "Application submitted successfully", Toast.LENGTH_SHORT).show();
                            // Clear input fields
                            editTextName.setText("");
                            editTextEmail.setText("");
                            editTextMessage.setText("");
                        } else {
                            Toast.makeText(ApplicationPageActivity.this, "Failed to submit application", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
