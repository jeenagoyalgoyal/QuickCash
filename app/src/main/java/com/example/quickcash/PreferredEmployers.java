package com.example.quickcash;

import java.util.ArrayList;
import java.util.List;

public class PreferredEmployers {
    private final ArrayList<String> preferredEmployersIdList = new ArrayList<>();
    private final ArrayList<String> preferredEmployersNameList = new ArrayList<>();

    public void addDetails(String id, String name){
        this.preferredEmployersIdList.add(id);
        this.preferredEmployersNameList.add(name);
    }


    // Add an employer by ID to the preferred employers list
    public boolean addEmployer(String employerID) {
        //if (employerID == null || employerID.isEmpty()) return false;
        //if (!preferredEmployers.contains(employerID)) {
        //    preferredEmployers.add(employerID);
        //    return true;
        //}
        return false;
    }

    // Retrieve the list of preferred employers
    public List<String> getPreferredEmployers() {
        //return new ArrayList<>(preferredEmployers);
        return null;
    }

    // Remove an employer by ID from the preferred employers list
    public boolean removeEmployer(String employerID) {
        //return preferredEmployers.remove(employerID);
        return false;
    }

    // Check if an employer is in the preferred list
    public boolean isEmployerPreferred(String employerID) {
        //return preferredEmployers.contains(employerID);
        return false;
    }

    public ArrayList<String> getNameList() {
        return preferredEmployersNameList;
    }
}
