package com.example.quickcash;

import androidx.core.util.PatternsCompat;

/**
 * CredentialValidator provides methods to validate user credentials.
 */
public class CredentialValidator {

    public boolean isEmptyPassword(String password) {
        return password == null || password.isEmpty();
    }

    public boolean isValidPassword(String password) {
        // Example validation: password must be at least 6 characters
        return password != null && password.length() >= 6;
    }

    public boolean isEmptyEmailAddress(String email) {
        return email == null || email.isEmpty();
    }

    public boolean isValidEmailAddress(String email) {
        return email != null && PatternsCompat.EMAIL_ADDRESS.matcher(email).matches();
    }
}
