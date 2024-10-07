package com.example.quickcash;

public class CredentialsValidator {

    public static boolean isEmptyInput(String Input) {
        return (Input.matches(new String()));
    }

    public static boolean isValidName(String Name) {
        return Name.matches("([A-Za-z]+ [A-Za-z]+)| [A-Za-z]+");
    }

    public static boolean isValidEmail(String emailAddress){
        return (emailAddress.matches("[[a-zA-Z]*[0-9]*]+@[a-zA-Z]+\\.[a-zA-Z]+"));
    }

    public static boolean isValidRole(String userRole) {
        return (userRole.equals("Employer") || userRole.equals("Employee"));
    }

    public static boolean isValidPassword(String Password, String Name, String emailAddress) {
        return ((!Password.contains(Name) && !Password.contains(emailAddress)) && Password.length() >= 8);
    }
}
