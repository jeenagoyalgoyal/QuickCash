package com.example.quickcash;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
//import static android.support.test.uiautomator;


import android.graphics.Color;
import android.widget.TextView;

import static org.hamcrest.CoreMatchers.is;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.assertion.ViewAssertions;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.hasToString;

import android.os.IBinder;
import android.view.WindowManager;

import androidx.test.espresso.Root;

import com.example.quickcash.ui.activities.EmployerHomepageActivity;
import com.example.quickcash.ui.activities.LoginActivity;
import com.example.quickcash.utils.JobSubmission;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

@RunWith(JUnit4.class)
public class JobSubmissionUITest {


    public ActivityScenario<EmployerHomepageActivity> employerActivityScenario;
    public ActivityScenario<JobSubmission> jobSubmissionActivityScenario;
    public ActivityScenario<LoginActivity> loginActivityActivityScenario;


    public void setupRoleActivity() {
        employerActivityScenario = ActivityScenario.launch(EmployerHomepageActivity.class);
    }

    public void setupJobSubmissionActivityScenario() {
        jobSubmissionActivityScenario = ActivityScenario.launch(JobSubmission.class);
    }

    public void setupLoginActivityActivityScenario(){
        loginActivityActivityScenario = ActivityScenario.launch(LoginActivity.class);
    }

    @Test
    public void checkCreateJobButtonPresent() {

        setupRoleActivity();
        onView(withText("Create Job")).check(matches(isDisplayed()));
    }

    @Test
    public void checkJobSubmissionForm() {
        setupRoleActivity();
        onView(withId(R.id.createJobButton)).perform(click());
        setupJobSubmissionActivityScenario();
        onView(withId(R.id.jobSub)).check(matches(isDisplayed()));
    }

    @Test
    public void checkRequiredFields() {
        setupJobSubmissionActivityScenario();
        onView(withId(R.id.jobTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.companyName)).check(matches(isDisplayed()));
        onView(withId(R.id.spinnerJobType)).check(matches(isDisplayed()));
        onView(withId(R.id.requirementText)).check(matches(isDisplayed()));
        onView(withId(R.id.salaryText)).check(matches(isDisplayed()));
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

    @Test
    public void checkErrorMessageInRed() {
        setupJobSubmissionActivityScenario();
        onView(withId(R.id.jobSubmissionButton)).perform(click());

        onView(withId(R.id.errorJSJobTitle)).check((view, noViewFoundException) -> {
            if (view instanceof TextView) {
                int currentColor = ((TextView) view).getCurrentTextColor();
                assertThat("Empty Field", currentColor, is(Color.RED));
            }
        });

        onView(withId(R.id.errorJSCompany)).check((view, noViewFoundException) -> {
            if (view instanceof TextView) {
                int currentColor = ((TextView) view).getCurrentTextColor();
                assertThat("Empty Field", currentColor, is(Color.RED));
            }
        });

        onView(withId(R.id.errorJSJobType)).check((view, noViewFoundException) -> {
            if (view instanceof TextView) {
                int currentColor = ((TextView) view).getCurrentTextColor();
                assertThat("Invalid Option", currentColor, is(Color.RED));
            }
        });

        onView(withId(R.id.errorJSRequirement)).check((view, noViewFoundException) -> {
            if (view instanceof TextView) {
                int currentColor = ((TextView) view).getCurrentTextColor();
                assertThat("Empty Field", currentColor, is(Color.RED));
            }
        });

        onView(withId(R.id.errorJSSalary)).check((view, noViewFoundException) -> {
            if (view instanceof TextView) {
                int currentColor = ((TextView) view).getCurrentTextColor();
                assertThat("Empty Field", currentColor, is(Color.RED));
            }
        });

        onView(withId(R.id.errorJSUrgency)).check((view, noViewFoundException) -> {
            if (view instanceof TextView) {
                int currentColor = ((TextView) view).getCurrentTextColor();
                assertThat("Invalid Option", currentColor, is(Color.RED));
            }
        });

        onView(withId(R.id.errorJSLocation)).check((view, noViewFoundException) -> {
            if (view instanceof TextView) {
                int currentColor = ((TextView) view).getCurrentTextColor();
                assertThat("Empty Field", currentColor, is(Color.RED));
            }
        });

        onView(withId(R.id.errorJSDuration)).check((view, noViewFoundException) -> {
            if (view instanceof TextView) {
                int currentColor = ((TextView) view).getCurrentTextColor();
                assertThat("Empty Field", currentColor, is(Color.RED));
            }
        });
    }

    @Test
    public void testPreventsSubmissionIfMissingFields() {
        setupJobSubmissionActivityScenario();

        onView(withId(R.id.jobTitle)).perform(typeText("Software Developer"));
        onView(withId(R.id.companyName)).perform(typeText("Tech Company"));
        onView(withId(R.id.spinnerJobType)).perform(click());
        onData(hasToString("Full-time")).perform(click());

        onView(withId(R.id.jobSubmissionButton)).perform(click());

        onView(withId(R.id.errorJSRequirement)).check((view, noViewFoundException) -> {
            if (view instanceof TextView) {
                int currentColor = ((TextView) view).getCurrentTextColor();
                assertThat("Empty Field", currentColor, is(Color.RED));
            }
        });
        onView(withId(R.id.errorJSSalary)).check((view, noViewFoundException) -> {
            if (view instanceof TextView) {
                int currentColor = ((TextView) view).getCurrentTextColor();
                assertThat("Empty Field", currentColor, is(Color.RED));
            }
        });
        onView(withId(R.id.errorJSUrgency)).check((view, noViewFoundException) -> {
            if (view instanceof TextView) {
                int currentColor = ((TextView) view).getCurrentTextColor();
                assertThat("Invalid Option", currentColor, is(Color.RED));
            }
        });
        onView(withId(R.id.errorJSLocation)).check((view, noViewFoundException) -> {
            if (view instanceof TextView) {
                int currentColor = ((TextView) view).getCurrentTextColor();
                assertThat("Empty Field", currentColor, is(Color.RED));
            }
        });
        onView(withId(R.id.errorJSDuration)).check((view, noViewFoundException) -> {
            if (view instanceof TextView) {
                int currentColor = ((TextView) view).getCurrentTextColor();
                assertThat("Empty Field", currentColor, is(Color.RED));
            }
        });

        onView(withText("Job Submission Successful!")).check(doesNotExist());
    }

    @Test
    public void testFormSubmitsSuccessfully() {
        setupLoginActivityActivityScenario();

        onView(withId(R.id.emailBox)).perform(typeText( "test2@gmail.com"));
        onView(withId(R.id.passwordBox)).perform(typeText("TestingPassword!1"));
        onView(withId(R.id.loginButton)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        onView(withText("Welcome Employer!")).check(matches(isDisplayed()));
        onView(withId(R.id.createJobButton)).perform(click());

        onView(withId(R.id.jobTitle)).perform(typeText("Software Developer"));
        onView(withId(R.id.companyName)).perform(typeText("Tech Company"));
        onView(withId(R.id.spinnerJobType)).perform(click());
        onData(hasToString("Full-time")).perform(click());
        onView(withId(R.id.requirementText)).perform(typeText("Plumber"));
        onView(withId(R.id.salaryText)).perform(typeText("25"));
        onView(withId(R.id.spinnerUrgency)).perform(click());
        onData(hasToString("High")).perform(click());
        onView(withId(R.id.locationJob)).perform(typeText("Halifax"));
        onView(withId(R.id.expectedDuration)).perform(typeText("20"));
        onView(withId(R.id.startDate)).perform(click());
        onView(withText("31")).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.jobSubmissionButton)).perform(click());

        onView(withText("Welcome Employer!")).check(matches(isDisplayed()));
    }

    public static class ToastMatcher extends TypeSafeMatcher<Root> {
        @Override
        public void describeTo(Description description) {
            description.appendText("is toast");
        }

        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if ((type == WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY) || (type == WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG)) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                return windowToken == appToken;
            }
            return false;
        }
    }
}