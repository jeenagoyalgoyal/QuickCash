package com.example.quickcash.validation;

import org.junit.Test;
import static org.junit.Assert.*;

public class JobSearchValidationTest {

    @Test
    public void testEmptyFieldsValidation() {
        String searchField = "";
        boolean isValid = validateSearchFields(searchField);
        assertFalse("Validation should fail when search fields are empty", isValid);
    }

    private boolean validateSearchFields(String searchField) {
        return searchField != null && !searchField.trim().isEmpty();
    }
}
