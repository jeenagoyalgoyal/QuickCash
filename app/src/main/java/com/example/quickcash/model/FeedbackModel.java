package com.example.quickcash.model;

public class FeedbackModel {
    private double rating;
    private String feedback;

    public FeedbackModel() {
        // Default constructor required for Firebase
    }

    public FeedbackModel(double rating, String feedback) {
        this.rating = rating;
        this.feedback = feedback;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
