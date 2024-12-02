package com.example.quickcash.deprecated;

//Class made initially to test Data transfer to map! replaced with Job class
public class JobToMap {
    private String jobTitle;
    private int salary;
    private String duration;
    private String location;  // Human readable location (e.g., "Halifax, NS")
    private double latitude;
    private double longitude;
    private String companyName;

    // Empty constructor required for Firebase
    public JobToMap() {}

    // Constructor
    public JobToMap(String jobTitle, String companyName, int salary, String duration, String location, double latitude, double longitude) {
        this.jobTitle = jobTitle;
        this.salary = salary;
        this.duration = duration;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.companyName = companyName;
    }

    // Getters and Setters
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public void setCompanyName(String companyName){this.companyName = companyName;}
    public String getCompanyName(){return companyName;}

    public int getSalary() { return salary; }
    public void setSalary(int salary) {this.salary = salary; }

    public String getDuration() {return this.duration;}
    public void setDuration(String duration) { this.duration = duration; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}