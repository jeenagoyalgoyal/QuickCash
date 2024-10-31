package com.example.quickcash.ui.models;

import com.google.android.gms.maps.model.LatLng;

public class Job {
    private String jobTitle;
    private double salary;
    private String duration;
    private double latitude;
    private double longitude;

    // Empty constructor required for Firebase
    public Job() {}

    // Getters and Setters
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

}
