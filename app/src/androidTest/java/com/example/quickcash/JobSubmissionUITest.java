package com.example.quickcash;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import static org.hamcrest.Matchers.equalTo;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JobSubmissionUITest {


    public ActivityScenario<RoleActivity> roleActivityActivityScenario;
    public ActivityScenario<JobSubmission> jobSubmissionActivityScenario;


    public void setupRoleActivity() {
        roleActivityActivityScenario = ActivityScenario.launch(RoleActivity.class);
    }

    public void setupJobSubmissionActivityScenario(){
        jobSubmissionActivityScenario = ActivityScenario.launch(JobSubmission.class);
    }

    @Test
    public void checkCreateJobButtonPresent(){
        setupRoleActivity();
        onView(withText("Switch to employer")).perform(click());
        onView(withText("Create Job")).check(matches(isDisplayed()));
    }

    @Test
    public void checkJobSubmissionForm(){
        setupRoleActivity();
        onView(withText("Switch to employer")).perform(click());
        onView(withText("Create Job")).perform(click());
        onView(withText("Job Submission Form")).check(matches(isDisplayed()));
    }

    @Test
    public void checkRequiredFields(){
        setupJobSubmissionActivityScenario();
        onView(withId(R.id.jobTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.companyName)).check(matches(isDisplayed()));
        onView(withId(R.id.spinnerJobType)).check(matches(isDisplayed()));
        onView(withId(R.id.requirementText)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextNumber)).check(matches(isDisplayed()));
        onView(withId(R.id.spinnerUrgency)).check(matches(isDisplayed()));
        onView(withId(R.id.locationJob)).check(matches(isDisplayed()));
        onView(withId(R.id.expectedDuration)).check(matches(isDisplayed()));
        onView(withId(R.id.startDate)).check(matches(isDisplayed()));
    }

    @Test
    public void testJobTypeDefaultPlaceholder() {
        setupJobSubmissionActivityScenario();
        onView(withId(R.id.spinnerJobType))
                .check(ViewAssertions.matches(withSpinnerText(equalTo("Select job type"))));
    }

    @Test
    public void testUrgencyDefaultPlaceholder() {
        setupJobSubmissionActivityScenario();
        onView(withId(R.id.spinnerUrgency))
                .check(ViewAssertions.matches(withSpinnerText(equalTo("Select urgency"))));
    }

}