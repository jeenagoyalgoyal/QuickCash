package com.example.quickcash.filter;

import com.example.quickcash.model.Job;

public class JobSearchFilter {

    /**
     * Checks to see if field is valid
     * @param title
     * @return true for valid
     */
    public static boolean isValidField(String title) {
        return title != null && !title.trim().isEmpty();
    }

    /**
     * Sees if salary is in bounds
     * @param minSalary
     * @param maxSalary
     * @return true if valid
     */
    public static boolean isValidSalary(int minSalary, int maxSalary) {
        return minSalary >= 0 && maxSalary >= 0 && minSalary <= maxSalary;
    }

    /**
     * Sees if the rest of the filter input passes
     * @param job
     * @param title
     * @param company
     * @param minSalStr
     * @param maxSalStr
     * @param jobDuration
     * @param jobLocation
     * @return true if valid
     */
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
            String jobLocationStr = job.getLocation();
            if (jobLocationStr == null || !jobLocationStr.contains(jobLocation)) {
                return false;
            }
        }

        if (isValidField(jobDuration)){
            if(!jobDuration.contains(job.getExpectedDuration())) {
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
