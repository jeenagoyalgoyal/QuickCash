package com.example.quickcash.model;


/**
 * Represents the geographical location of a job, including latitude, longitude, and address.
 */
public class JobLocation {
    private double lat;
    private double lng;
    private String address;

    /**
     * Default constructor required for Firebase serialization.
     */
    public JobLocation() {
    }

    /**
     * Constructs a {@code JobLocation} object with the specified latitude, longitude, and address.
     *
     * @param lat     the latitude of the job location
     * @param lng     the longitude of the job location
     * @param address the address of the job location
     */
    public JobLocation(double lat, double lng, String address) {
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    /**
     * Returns the latitude of the job location.
     *
     * @return the latitude
     */
    public double getLat() {
        return lat;
    }

    /**
     * Sets the latitude of the job location.
     *
     * @param lat the latitude to set
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * Returns the longitude of the job location.
     *
     * @return the longitude
     */
    public double getLng() {
        return lng;
    }

    /**
     * Sets the longitude of the job location.
     *
     * @param lng the longitude to set
     */
    public void setLng(double lng) {
        this.lng = lng;
    }

    /**
     * Returns the address of the job location.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the job location.
     *
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
}