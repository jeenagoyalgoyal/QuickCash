package com.example.quickcash;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quickcash.R;
import com.example.quickcash.model.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JobDetails extends AppCompatActivity {

    private TextView jobTitleText, companyNameText, locationText, jobTypeText, salaryText, durationText;
    private RatingBar jobRatingBar;
    private EditText commentInput;
    private Button addCommentButton, closeButton;
    private UserRatingSubmissionHelp userRatingSubmissionHelper;


    private DatabaseReference jobRef;
    private String jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_job_details);

        userRatingSubmissionHelper = new UserRatingSubmissionHelp();

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
        addCommentButton.setEnabled(false);

        // Add listeners to validate comment and rating
        jobRatingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            userRatingSubmissionHelper.setRating(rating);
            updateAddCommentButtonState();
        });

        commentInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                userRatingSubmissionHelper.setComment(s.toString());
                updateAddCommentButtonState();
            }
        });

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

    private void validateCommentAndRating() {
        String comment = commentInput.getText().toString().trim();
        float rating = jobRatingBar.getRating();

        // Enable the button only if both a rating and a comment are provided
        addCommentButton.setEnabled(!comment.isEmpty() && rating > 0);
    }

    private void updateAddCommentButtonState() {
        addCommentButton.setEnabled(userRatingSubmissionHelper.addCommentButtonIsEnabled());
    }

    private void loadJobDetails() {
        jobRef.child(jobId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Job job = snapshot.getValue(Job.class);

                    if (job != null) {
                        jobTitleText.setText(job.getJobTitle() != null ? job.getJobTitle() : "Not available");
                        companyNameText.setText(job.getCompanyName() != null ? job.getCompanyName() : "Not available");
                        locationText.setText(job.getLocation() != null ? job.getLocation() : "Not specified");
                        jobTypeText.setText(job.getJobType() != null ? job.getJobType() : "Not available");
                        salaryText.setText(job.getSalary() > 0 ? "$" + job.getSalary() : "Not specified");
                        durationText.setText(job.getExpectedDuration() != null ? job.getExpectedDuration() : "Not specified");
                    }
                } else {
                    Toast.makeText(JobDetails.this, "Job not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(JobDetails.this, "Failed to load data! " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            userRatingSubmissionHelper.setRating(0);
                            userRatingSubmissionHelper.setComment("");
                            updateAddCommentButtonState();
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
