package com.example.quickcash;

public class JobSearchValidator {

    public static boolean isValidJobTitle(String jobTitle) {
        return jobTitle != null && !jobTitle.trim().isEmpty();
    }

    public static boolean isValidSalaryString(String minSalary, String maxSalary) {
        // If both empty return true
        if (minSalary.isEmpty() && maxSalary.isEmpty()) {
            return true;
        }

        try {
            // If minSalary is empty and maxSalary is valid
            if (minSalary.isEmpty()) {
                return Integer.parseInt(maxSalary) > 0;
            }
            // If maxSalary is empty and minSalary is valid
            if (maxSalary.isEmpty()) {
                return Integer.parseInt(minSalary) > 0;
            }
            // If both present, validate range
            return isValidSalaryRange(Integer.parseInt(minSalary), Integer.parseInt(maxSalary));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidSalaryRange(int minSalary, int maxSalary) {
        return minSalary >= 0 && maxSalary >= 0 && minSalary <= maxSalary;
    }

    public static boolean isValidDuration(String duration) {
        return duration != null && !duration.trim().isEmpty();
    }

    public static boolean isValidLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            return false;
        }
        // Location must be at least 3 characters long for meaningful search
        return location.trim().length() >= 3;
    }

    public static boolean isValidCoordinates(double latitude, double longitude) {
        return latitude >= -90 && latitude <= 90 &&
                longitude >= -180 && longitude <= 180;
    }

    public static boolean allEmptyFields(String jobTitle, String minSalary,
                                         String maxSalary, String duration, String location) {
        return isEmpty(jobTitle) &&
                isEmpty(minSalary) &&
                isEmpty(maxSalary) &&
                isEmpty(duration) &&
                isEmpty(location);
    }

    private static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}