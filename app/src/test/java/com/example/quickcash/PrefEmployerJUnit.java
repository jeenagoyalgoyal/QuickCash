package com.example.quickcash;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PrefEmployerJUnit {
    private PreferredEmployers preferredEmployers;

    @Before
    public void setUp() {
        preferredEmployers = new PreferredEmployers();
    }

    @Test
    public void testAddEmployerSuccessfully() {
        boolean result = preferredEmployers.addEmployer("employer123");
        assertTrue("Employer should be added successfully", result);
        assertTrue("Employer should be in preferred list", preferredEmployers.isEmployerPreferred("employer123"));
    }

    @Test
    public void testAddEmployerAlreadyInList() {
        preferredEmployers.addEmployer("employer123");
        boolean result = preferredEmployers.addEmployer("employer123");
        assertFalse("Adding an already existing employer should return false", result);
    }

    @Test
    public void testAddEmployerInvalidId() {
        boolean result = preferredEmployers.addEmployer("");
        assertFalse("Adding an empty employer ID should return false", result);
        result = preferredEmployers.addEmployer(null);
        assertFalse("Adding a null employer ID should return false", result);
    }

    @Test
    public void testGetPreferredEmployers() {
        preferredEmployers.addEmployer("employer123");
        preferredEmployers.addEmployer("employer456");
        assertEquals("There should be 2 employers in the list", 2, preferredEmployers.getPreferredEmployers().size());
        assertTrue("List should contain employer123", preferredEmployers.getPreferredEmployers().contains("employer123"));
        assertTrue("List should contain employer456", preferredEmployers.getPreferredEmployers().contains("employer456"));
    }

    @Test
    public void testRemoveEmployerSuccessfully() {
        preferredEmployers.addEmployer("employer123");
        boolean result = preferredEmployers.removeEmployer("employer123");
        assertTrue("Employer should be removed successfully", result);
        assertFalse("Employer should no longer be in the list", preferredEmployers.isEmployerPreferred("employer123"));
    }

    @Test
    public void testRemoveEmployerNotInList() {
        boolean result = preferredEmployers.removeEmployer("nonexistentEmployer");
        assertFalse("Removing a non-existent employer should return false", result);
    }

    @Test
    public void testIsEmployerPreferred() {
        preferredEmployers.addEmployer("employer123");
        assertTrue("Employer should be in preferred list", preferredEmployers.isEmployerPreferred("employer123"));
        assertFalse("Non-existent employer should not be in the list", preferredEmployers.isEmployerPreferred("employer456"));
    }
}
