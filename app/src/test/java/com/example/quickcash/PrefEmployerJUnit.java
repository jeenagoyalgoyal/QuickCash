package com.example.quickcash;

import static org.junit.Assert.*;

import com.example.quickcash.repositories.PreferredEmployers;

import org.junit.Test;

import java.util.ArrayList;

public class PrefEmployerJUnit {
    private PreferredEmployers preferredEmployers;

    private String employerId1 = "employer1@test,com";
    private String employerName1 = "employer1";
    private String employerId2 = "employer2@test,com";
    private String employerName2 = "employer2";



    @Test
    public void testAddEmployerSuccessfully() {
        preferredEmployers = new PreferredEmployers();
        assertTrue(preferredEmployers.addDetails(employerId1,employerName1));
    }

    @Test
    public void testAddDuplicateEmployer() {
        preferredEmployers = new PreferredEmployers();
        preferredEmployers.addDetails(employerId1,employerName1);
        assertFalse(preferredEmployers.addDetails(employerId1,employerName1));
    }

    @Test
    public void testGetPreferredEmployers() {
        preferredEmployers = new PreferredEmployers();
        preferredEmployers.addDetails(employerId1,employerName1);
        ArrayList<String> ids = preferredEmployers.getIdList();
        ArrayList<String> names = preferredEmployers.getNameList();
        assertEquals(employerId1,ids.get(0));
        assertEquals(employerName1,names.get(0));
    }

    @Test
    public void testAddMultipleEmployers() {
        preferredEmployers = new PreferredEmployers();
        preferredEmployers.addDetails(employerId1,employerName1);
        preferredEmployers.addDetails(employerId2,employerName2);
        ArrayList<String> ids = preferredEmployers.getIdList();
        ArrayList<String> names = preferredEmployers.getNameList();
        assertEquals(employerId1,ids.get(0));
        assertEquals(employerName1,names.get(0));
        assertEquals(employerId2,ids.get(1));
        assertEquals(employerName2,names.get(1));
    }
}
