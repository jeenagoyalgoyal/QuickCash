package com.example.quickcash;

public class CredentialsValidator {

    public static boolean isValidBannerID(String BannerID) {
        return (BannerID.matches("^B00[0-9]+") && BannerID.length() == 9);
    }

    public static boolean isEmptyBannerID(String BannerID) {
        return (BannerID.matches(new String()));
    }

    public static boolean isValidEmailAddress(String emailAddress){
        //implement pending
        return false;
    }

    public static boolean isValidRole(String userRole) {
        //implement pending
        return false;
    }
}
