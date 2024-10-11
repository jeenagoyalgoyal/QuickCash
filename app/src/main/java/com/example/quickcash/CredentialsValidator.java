package com.example.quickcash;

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

        return (emailAddress.matches("[^@]+@([a-zA-Z]+\\.[a-zA-Z]+)(\\.([a-zA-Z]+\\.[a-zA-Z]+))*"));
    }

    public static boolean isValidRole(String userRole) {
        userRole = userRole.trim();

        return (userRole.equals("Employer") || userRole.equals("Employee"));
    }

    public static boolean isValidPassword(String Password) {
        Password = Password.trim();

        return (Password.length() >= 8 && Password.matches(".*[.*)(+@#$%&!?><{}/\\]\\[]+.*"));
    }
}

