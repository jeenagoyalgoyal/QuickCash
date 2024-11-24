package com.example.quickcash;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quickcash.R;
import com.example.quickcash.model.Job;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JobDetails extends AppCompatActivity {

    private TextView jobTitleText, companyNameText, locationText, jobTypeText, salaryText, durationText;
    private RatingBar jobRatingBar;
    private EditText commentInput;
    private Button addCommentButton, closeButton;

    private DatabaseReference jobRef;
    private String jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_job_details);



        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        jobRef = database.getReference("Jobs");

        // Retrieve Job ID (Passed from previous activity)
        jobId = getIntent().getStringExtra("JOB_ID");

        if (jobId == null || jobId.isEmpty()) {
            Toast.makeText(this, "No job ID provided!", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if no job ID is provided
            return;
        }

        // Bind Views
        jobTitleText = findViewById(R.id.jobTitleText);
        companyNameText = findViewById(R.id.companyNameText);
        locationText = findViewById(R.id.locationText);
        jobTypeText = findViewById(R.id.jobTypeText);
        salaryText = findViewById(R.id.salaryText);
        durationText = findViewById(R.id.durationText);
        jobRatingBar = findViewById(R.id.jobRatingBar);
        commentInput = findViewById(R.id.commentInput);
        addCommentButton = findViewById(R.id.addCommentButton);
        closeButton = findViewById(R.id.closeButton);

        // Fetch and Display Job Details
        loadJobDetails();

        // Handle Add Comment Button
        addCommentButton.setOnClickListener(v -> {
            String comment = commentInput.getText().toString().trim();
            float rating = jobRatingBar.getRating();

            if (comment.isEmpty()) {
                Toast.makeText(this, "Please add a comment!", Toast.LENGTH_SHORT).show();
                return;
            }

            saveCommentAndRating(comment, rating);
        });

        // Handle Close Button
        closeButton.setOnClickListener(v -> finish());
    }

    private void loadJobDetails() {
        // This method retrieves the job details from Firebase or the intent extras
        // and sets them into the respective TextViews.

        // Example:
        jobRef.child(jobId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                Job job = task.getResult().getValue(Job.class);

                if (job != null) {
                    jobTitleText.setText(job.getJobTitle());
                    companyNameText.setText(job.getCompanyName());
                    locationText.setText(job.getLocation());
                    jobTypeText.setText(job.getJobType());
                    salaryText.setText(String.valueOf(job.getSalary()));
                    durationText.setText(job.getExpectedDuration());
                }
            } else {
                Toast.makeText(this, "Failed to load job details!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveCommentAndRating(String comment, float rating) {
        // Save rating and comment to Firebase
        DatabaseReference commentsRef = jobRef.child(jobId).child("comments");
        String commentId = commentsRef.push().getKey();

        if (commentId != null) {
            commentsRef.child(commentId).setValue(new Comment(comment, rating))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Comment and rating added successfully!", Toast.LENGTH_SHORT).show();
                            commentInput.setText("");  // Clear input
                            jobRatingBar.setRating(0); // Reset rating bar
                        } else {
                            Toast.makeText(this, "Failed to add comment and rating!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // Comment Model Class (Inner Class for simplicity)
    public static class Comment {
        public String commentText;
        public float rating;

        public Comment() {
            // Default constructor required for Firebase
        }

        public Comment(String commentText, float rating) {
            this.commentText = commentText;
            this.rating = rating;
        }
    }
}
