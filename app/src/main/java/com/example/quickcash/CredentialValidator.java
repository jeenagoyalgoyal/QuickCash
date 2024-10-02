package com.example.quickcash;

import androidx.core.util.PatternsCompat;

public class CredentialValidator {
    protected boolean isEmptyPassword(String Password) {
        return Password.isEmpty();
    }

    protected boolean isValidPassword(String Password) {
        return false;
    }

    protected boolean isEmptyEmailAddress(String Password) {
        return Password.isEmpty();
    }

    protected boolean isValidEmailAddress(String emailAddress) {
        return false;
    }

}
