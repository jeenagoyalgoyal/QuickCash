package com.example.quickcash.model;

/**
 * This class represents a model for a preferred employer.
 * It holds the employer's ID and name, with corresponding getter and setter methods.
 */
public class PreferredEmployerModel {
    private String id;
    private String name;

    /**
     * Default constructor for PreferredEmployerModel.
     * Initializes a new instance of the class without setting any fields.
     */
    public PreferredEmployerModel(){
    }

    /**
     * Retrieves the ID of the employer.
     * @return A String representing the employer's ID.
     */
    public String getId() {
        return id;
    }


    /**
     * Retrieves the name of the employer.
     * @return A String representing the employer's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the ID of the employer.
     * @param id A String representing the employer's ID.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the name of the employer.
     * @param name A String representing the employer's name.
     */
    public void setName(String name) {
        this.name = name;
    }
}
