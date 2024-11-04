package com.example.quickcash.utils.validators;

import java.util.regex.Pattern;

public class CredentialsValidator {

    public static boolean isEmptyInput(String Input) {
        Input = Input.trim();

        return (Input.matches(new String()));
    }

    public static boolean isValidName(String Name) {
        Name = Name.trim();

        return Name.matches("[^(!@#$%&*\\-)]*") && Name.length()>=3;
    }

    public static boolean isValidEmail(String emailAddress){
        emailAddress = emailAddress.trim();
        return (Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$").matcher(emailAddress).matches());
    }

    public static boolean isValidRole(String userRole) {
        userRole = userRole.trim();

        return (userRole.equals("Employer") || userRole.equals("Employee"));
    }

    public static boolean isValidPassword(String Password) {
        Password = Password.trim();

        // Valid password is greater than or equal to 6 chars in length
        // and has at least one letter, one number and one special character
        return (Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[.*\\)\\(+@#$%&!?><{}/\\]\\[]).{6,}$").matcher(Password).matches());
    }

    public boolean isEmptyPassword(String password) {
        return password == null || password.isEmpty();
    }

    public boolean isEmptyEmailAddress(String email) {
        return email == null || email.isEmpty();
    }

}

