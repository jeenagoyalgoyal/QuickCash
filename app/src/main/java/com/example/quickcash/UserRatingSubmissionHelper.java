package com.example.quickcash;

public interface UserRatingSubmissionHelper {
    float getRating();

    void setRating(float i);

    String getComment();

    void setComment(String testing);

    boolean addCommentButtonIsEnabled();

    boolean formatIsValid();

    void formatForFirebase();
}
