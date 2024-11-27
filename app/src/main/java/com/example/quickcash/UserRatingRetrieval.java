package com.example.quickcash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRatingRetrieval implements UserRatingRetrievalHelper{
    private Map<String, String> commentsDatabase = new HashMap<>();
    private Map<String, Float> ratingsDatabase = new HashMap<>();// Simulate database

    public UserRatingRetrieval() {
        // Pre-fill with mock data
        commentsDatabase.put("testcommentid1", "Great job on the project!");
        ratingsDatabase.put("testcommentid1", 4.5f);

        commentsDatabase.put("testcommentid2", "Needs improvement in communication.");
        ratingsDatabase.put("testcommentid2", 3.0f);
    }

    @Override
    public boolean commentExists(String commentId) {
        return commentsDatabase.containsKey(commentId);
    }


}
