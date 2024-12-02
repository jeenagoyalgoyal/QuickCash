package com.example.quickcash;

public class UserRatingSubmissionHelp implements UserRatingSubmissionHelper{

    private float rating;
    private String comment;
    private boolean isButtonEnabled = false;
    private boolean isFormattedForFirebase = false;

    @Override
    public float getRating() {
        return rating;
    }

    @Override
    public void setRating(float rating) {
        this.rating = rating;
//        updateButtonState(); // Dynamically update button state
    }

    @Override
    public String getComment() {
        return comment;
    }
    @Override
    public void setComment(String comment) {
        this.comment = comment != null ? comment.trim() : "";
        updateButtonState(); // Dynamically update button state
    }

    @Override
    public boolean addCommentButtonIsEnabled() {
        return isButtonEnabled;
    }

    @Override
    public void formatForFirebase() {
        if (!comment.isEmpty() && rating > 0) {
            isFormattedForFirebase = true; // Mark as formatted
        } else {
            isFormattedForFirebase = false; // Invalid state
        }
    }

    @Override
    public boolean formatIsValid() {
        return isFormattedForFirebase;
    }
    // Helper method to update button state
    private void updateButtonState() {
        isButtonEnabled = !comment.isEmpty() && rating > 0;
    }
}
