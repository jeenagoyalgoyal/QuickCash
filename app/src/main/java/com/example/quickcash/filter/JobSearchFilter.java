package com.example.quickcash.filter;

import com.example.quickcash.model.Job;

public class JobSearchFilter {

    // Method to check if the job field is empty or noy (can be empty)
    public static boolean isValidField(String title) {
        return title != null && !title.trim().isEmpty();
    }

    // Tests salary is within boundary
    public static boolean isValidSalary(int minSalary, int maxSalary) {
        return minSalary >= 0 && maxSalary >= 0 && minSalary <= maxSalary;
    }

    public static boolean passesAdditionalJobFilters(Job job, String title, String company, String minSalStr, String maxSalStr, String jobDuration, String jobLocation){
        boolean matches = true;

        if (isValidField(title)){
            if( !title.equalsIgnoreCase(job.getJobTitle())) {
                matches = false;
            }
        }

        if (isValidField(company)){
            if(!company.equalsIgnoreCase(job.getCompanyName())) {
                matches = false;
            }
        }

        if (isValidField(jobLocation)){
            if(!jobLocation.equalsIgnoreCase(job.getLocation())) {
                matches = false;
            }
        }

        if (isValidField(jobDuration)){
            if(!jobDuration.equalsIgnoreCase(job.getExpectedDuration())) {
                matches = false;
            }
        }

        int salary = job.getSalary();
        if (!minSalStr.isEmpty() && !maxSalStr.isEmpty()) {
            int minSal = Integer.parseInt(minSalStr);
            int maxSal = Integer.parseInt(maxSalStr);

            if (salary < minSal || salary > maxSal) {
                matches = false;
            }
        }else if(!minSalStr.isEmpty()) {
            int minSal = Integer.parseInt(minSalStr);
            if (salary < minSal){
                matches = false;
            }
        }else if(!maxSalStr.isEmpty()) {
            int maxSal = Integer.parseInt(maxSalStr);
            if(salary > maxSal){
                matches = false;
            }
        }

        return matches;
    }
}
