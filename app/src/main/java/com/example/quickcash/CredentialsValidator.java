package com.example.quickcash;

public class CredentialsValidator {

    public static boolean isEmptyInput(String Input) {
        Input = Input.trim();

        return (Input.matches(new String()));
    }

    public static boolean isValidName(String Name) {
        Name = Name.trim();

        return Name.matches("([A-Za-z]+ [A-Za-z]+)| [A-Za-z]+");
    }

    public static boolean isValidEmail(String emailAddress){
        emailAddress = emailAddress.trim();

        return (emailAddress.matches("[[a-zA-Z]*[0-9]*]+@([a-zA-Z]+\\.[a-zA-Z]+\\.*)+"));
    }

    public static boolean isValidRole(String userRole) {
        userRole = userRole.trim();

        return (userRole.equals("Employer") || userRole.equals("Employee"));
    }

    public static boolean isValidPassword(String Password, String Name, String emailAddress) {
        Password = Password.trim();
        Name = Name.trim();
        emailAddress = emailAddress.trim();

        return ( (!Password.contains(Name.trim()) && !Password.contains(emailAddress.trim()) ) && Password.length() >= 8);
    }
}
