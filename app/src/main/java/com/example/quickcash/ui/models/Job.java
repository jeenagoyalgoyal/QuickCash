package com.example.quickcash.ui.models;

public class Job {
    private String jobTitle;
    private double salary;
    private String duration;
    private String location;  // Human readable location (e.g., "Halifax, NS")
    private double latitude;
    private double longitude;

    // Empty constructor required for Firebase
    public Job() {}

    // Constructor
    public Job(String jobTitle, double salary, String duration, String location, double latitude, double longitude) {
        this.jobTitle = jobTitle;
        this.salary = salary;
        this.duration = duration;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}