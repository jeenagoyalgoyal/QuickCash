package com.example.quickcash;

import java.util.ArrayList;
import java.util.List;

public class PreferredEmployers {
    private final ArrayList<String> preferredEmployersIdList = new ArrayList<>();
    private final ArrayList<String> preferredEmployersNameList = new ArrayList<>();

    public boolean addDetails(String id, String name){
        if (!this.preferredEmployersIdList.contains(id) && !this.preferredEmployersNameList.contains(name)){
            this.preferredEmployersIdList.add(id);
            this.preferredEmployersNameList.add(name);
            return true;
        }
        return false;
    }

    public ArrayList<String> getNameList() {
        return preferredEmployersNameList;
    }

    public ArrayList<String> getIdList() {
        return preferredEmployersIdList;
    }
}
