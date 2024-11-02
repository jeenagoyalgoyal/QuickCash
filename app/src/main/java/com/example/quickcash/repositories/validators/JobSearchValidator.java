package com.example.quickcash.repositories.validators;

public class JobSearchValidator {

    public static boolean isValidJobTitle(String jobTitle) {
        return jobTitle != null && !jobTitle.trim().isEmpty();
    }

    public static boolean isValidSalaryString(String minSalary, String maxSalary) {
        //If both empty return true
        if(minSalary.isEmpty() && maxSalary.isEmpty()) {
            return true;
            //If minSalary is empty and maxSalary is more than 0
        } else if (minSalary.isEmpty()) {
            if(Integer.parseInt(maxSalary) > 0) {
                return true;
            }
            //If maxSalary is empty and minSalary is more than 0
        } else if (maxSalary.isEmpty()) {
            if(Integer.parseInt(minSalary) > 0) {
                return true;
            }
        }

        //If both not empty, then check for validity.
        return isValidSalaryRange(Integer.parseInt(minSalary), Integer.parseInt(maxSalary));
    }

    public static boolean isValidSalaryRange(int minSalary, int maxSalary) {
        return (minSalary >= 0 && maxSalary >= 0 && minSalary <= maxSalary );
    }

    public static boolean isValidDuration(String duration) {
        return duration != null && !duration.trim().isEmpty();
    }

    public static boolean isValidLocation(String location) {
        // Location must be non-null, non-empty, and contain actual text
        return location != null &&
                !location.trim().isEmpty() &&
                location.trim().length() >= 3; // Minimum 3 chars for a valid location
    }

    public static boolean isValidCoordinates(double latitude, double longitude) {
        return latitude >= -90 && latitude <= 90 &&
                longitude >= -180 && longitude <= 180;
    }

    public static boolean allEmptyFields(String jobTitle, String minSalary,
                                         String maxSalary, String duration, String location) {
        return (jobTitle == null || jobTitle.trim().isEmpty()) &&
                (minSalary == null || minSalary.trim().isEmpty()) &&
                (maxSalary == null || maxSalary.trim().isEmpty()) &&
                (duration == null || duration.trim().isEmpty()) &&
                (location == null || location.trim().isEmpty());
    }
}