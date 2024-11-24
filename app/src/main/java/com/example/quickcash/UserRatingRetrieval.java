package com.example.quickcash;

import java.util.HashMap;
import java.util.Map;

public class UserRatingRetrieval implements UserRatingRetrievalHelper{
    private Map<String, String> commentsDatabase = new HashMap<>(); // Simulate database

    public UserRatingRetrieval() {
        // Pre-fill with mock data
        commentsDatabase.put("testcommentid", "test comment");
    }

    @Override
    public boolean commentExists(String commentId) {
        return commentsDatabase.containsKey(commentId);
    }
}
