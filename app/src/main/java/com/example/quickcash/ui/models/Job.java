package com.example.quickcash.ui.models;

import com.google.android.gms.maps.model.LatLng;

public class Job {
    private String jobTitle;
    private double salary;
    private String duration;
    private double latitude;
    private double longitude;

    protected final double MIN_LATITUDE = -90.0;
    protected final double MAX_LATITUDE = 90.0;
    protected final double MIN_LONGITUDE = -180.0;
    protected final double MAX_LONGITUDE = 180.0;

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
    public void setLatitude(double latitude) {
        if (latitude > MAX_LATITUDE) {
            this.latitude = MAX_LATITUDE;
        } else if (latitude < MIN_LATITUDE){
            this.latitude = MIN_LATITUDE;
        } else {
            this.latitude = latitude;
        }
    }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) {
        if (longitude > MAX_LONGITUDE) {
            this.longitude = MAX_LONGITUDE;
        } else if (longitude < MIN_LONGITUDE){
            this.longitude = MIN_LONGITUDE;
        } else {
            this.longitude = longitude;
        }
    }

}
