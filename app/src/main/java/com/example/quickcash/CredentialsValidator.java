package com.example.quickcash;

public class CredentialsValidator {

    public static boolean isEmptyInput(String BannerID) {
        return (BannerID.matches(new String()));
    }

    public static boolean isValidName(String Name) {
        //implement soon
        return false;
    }

    public static boolean isValidEmailAddress(String emailAddress){
        return (emailAddress.matches("[[a-zA-Z]*[0-9]*]+@[a-zA-Z]+\\.[a-zA-Z]+"));
    }

    public static boolean isValidRole(String userRole) {
        return (userRole.equals("Employer") || userRole.equals("Employee"));
    }
}
