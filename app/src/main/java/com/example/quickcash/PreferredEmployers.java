package com.example.quickcash;

import java.util.ArrayList;

/**
 * This class represents a collection of preferred employers, storing their IDs and names.
 * It provides methods to add employer details and retrieve the stored lists of IDs and names.
 */
public class PreferredEmployers {
    private final ArrayList<String> preferredEmployersIdList = new ArrayList<>();
    private final ArrayList<String> preferredEmployersNameList = new ArrayList<>();

    /**
     * Adds the details of a preferred employer to the lists.
     * Ensures that the employer is not already in the lists before adding.
     * @param id The ID of the employer to add.
     * @param name The name of the employer to add.
     * @return true if the employer details were added successfully, false if the employer is already in the lists.
     */
    public boolean addDetails(String id, String name){
        if (!this.preferredEmployersIdList.contains(id) && !this.preferredEmployersNameList.contains(name)){
            this.preferredEmployersIdList.add(id);
            this.preferredEmployersNameList.add(name);
            return true;
        }
        return false;
    }

    /**
     * Retrieves the list of preferred employer names.
     * @return An ArrayList containing the names of preferred employers.
     */
    public ArrayList<String> getNameList() {
        return preferredEmployersNameList;
    }

    /**
     * Retrieves the list of preferred employer IDs.
     * @return An ArrayList containing the IDs of preferred employers.
     */
    public ArrayList<String> getIdList() {
        return preferredEmployersIdList;
    }
}
