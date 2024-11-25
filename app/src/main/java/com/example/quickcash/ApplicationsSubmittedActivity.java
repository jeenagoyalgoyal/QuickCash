package com.example.quickcash;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ApplicationsSubmittedActivity extends AppCompatActivity {
    private final static String TAG = "Employer Applications";
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String email;
    private String emailKey;
    private ImageButton backButton;

    //TODO: Implement page functionality
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_application_submissions);

        // Retrieve Employer details

        this.mAuth = FirebaseAuth.getInstance();
        this.email = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;
        this.emailKey = email.replace(".", ",");

        if (email != null && !email.isEmpty()) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Jobs");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                        String employerID = jobSnapshot.child("employerID").getValue(String.class);
                        if (emailKey.equals(employerID)) {
                            // Match found: Fetch applications for this job
                            DataSnapshot applicationsSnapshot = jobSnapshot.child("applications");
                            for (DataSnapshot application : applicationsSnapshot.getChildren()) {
                                // Process each application
                                String applicationDetails = application.getValue(String.class);
                                Log.d("Application", "Details: " + applicationDetails);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ApplicationsSubmittedActivity.this, "Job Submission Successful!", Toast.LENGTH_SHORT).show();
                }
            });

            init();

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });


        } else {
            Toast.makeText(ApplicationsSubmittedActivity.this, "Email not found for user. Sign in first.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void init() {
        backButton = findViewById(R.id.backButton);
    }
}
