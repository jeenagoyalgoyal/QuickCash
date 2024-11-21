package com.example.quickcash;

import java.util.regex.Pattern;

/**
 * Utility class for validating user credentials such as names, email addresses, roles, and passwords.
 */
public class CredentialsValidator {

    /**
     * Checks if the given input string is empty or contains only whitespace.
     *
     * @param input the input string to check
     * @return {@code true} if the input is empty after trimming; {@code false} otherwise
     */
    public static boolean isEmptyInput(String input) {
        input = input.trim();

        return (input.matches(new String()));
    }

    /**
     * Validates if the given name is valid.
     * A valid name contains no special characters (!@#$%&*-), is trimmed, and has a minimum length of 3.
     *
     * @param name the name to validate
     * @return {@code true} if the name is valid; {@code false} otherwise
     */
    public static boolean isValidName(String name) {
        name = name.trim();

        return name.matches("[^(!@#$%&*\\-)]*") && name.length()>=3;
    }

    /**
     * Validates if the given email address is in a valid format.
     *
     * @param emailAddress the email address to validate
     * @return {@code true} if the email address is valid; {@code false} otherwise
     */
    public static boolean isValidEmail(String emailAddress){
        emailAddress = emailAddress.trim();
        return (Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$").matcher(emailAddress).matches());
    }

    /**
     * Checks if the provided role is valid.
     * A valid role is either "Employer" or "Employee".
     *
     * @param userRole the role to validate
     * @return {@code true} if the role is valid; {@code false} otherwise
     */
    public static boolean isValidRole(String userRole) {
        userRole = userRole.trim();

        return (userRole.equals("Employer") || userRole.equals("Employee"));
    }

    /**
     * Validates if the given password meets the required criteria:
     * At least 6 characters long, contains at least one letter, one number, and one special character.
     *
     * @param password the password to validate
     * @return {@code true} if the password is valid; {@code false} otherwise
     */
    public static boolean isValidPassword(String password) {
        password = password.trim();

        // Valid password is greater than or equal to 6 chars in length
        // and has at least one letter, one number and one special character
        return (Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[.*\\)\\(+@#$%&!?><{}/\\]\\[]).{6,}$").matcher(password).matches());
    }

    /**
     * Checks if the given password is null or empty.
     *
     * @param password the password to check
     * @return {@code true} if the password is null or empty; {@code false} otherwise
     */
    public boolean isEmptyPassword(String password) {
        return password == null || password.isEmpty();
    }

    /**
     * Checks if the given email address is null or empty.
     *
     * @param email the email address to check
     * @return {@code true} if the email address is null or empty; {@code false} otherwise
     */
    public boolean isEmptyEmailAddress(String email) {
        return email == null || email.isEmpty();
    }

}

