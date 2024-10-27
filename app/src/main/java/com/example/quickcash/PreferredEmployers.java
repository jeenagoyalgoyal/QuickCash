package com.example.quickcash;

import java.util.ArrayList;
import java.util.List;

public class PreferredEmployers {
    private final List<String> preferredEmployers = new ArrayList<>();

    // Add an employer by ID to the preferred employers list
    public boolean addEmployer(String employerID) {
        if (employerID == null || employerID.isEmpty()) return false;
        if (!preferredEmployers.contains(employerID)) {
            preferredEmployers.add(employerID);
            return true;
        }
        return false;
    }

    // Retrieve the list of preferred employers
    public List<String> getPreferredEmployers() {
        return new ArrayList<>(preferredEmployers);
    }

    // Remove an employer by ID from the preferred employers list
    public boolean removeEmployer(String employerID) {
        return preferredEmployers.remove(employerID);
    }

    // Check if an employer is in the preferred list
    public boolean isEmployerPreferred(String employerID) {
        return preferredEmployers.contains(employerID);
    }
}
