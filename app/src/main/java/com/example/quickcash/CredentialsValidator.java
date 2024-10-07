package com.example.quickcash;

public class CredentialsValidator {

    public static boolean isEmptyInput(String BannerID) {
        return (BannerID.matches(new String()));
    }

    public static boolean isValidBannerID(String BannerID) {
        return (BannerID.matches("^B00[0-9]+") && BannerID.length() == 9);
    }

    public static boolean isValidEmailAddress(String emailAddress){
        return (emailAddress.matches("[[a-zA-Z]*[0-9]*]+@[a-zA-Z]+\\.[a-zA-Z]+"));
    }

    public static boolean isValidRole(String userRole) {
        //implement pending
        return false;
    }
}
