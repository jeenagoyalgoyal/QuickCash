package com.example.quickcash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.quickcash.ui.activities.EmployeeHomepageActivity;
import com.example.quickcash.ui.activities.EmployerHomepageActivity;
import com.example.quickcash.utils.UseRole;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class UserRoleJunitTest {

    private UseRole useRole;
    private DatabaseReference db;

    @Rule
    public ActivityScenarioRule<EmployerHomepageActivity> employerActivityRule = new ActivityScenarioRule<>(EmployerHomepageActivity.class);

    @Rule
    public ActivityScenarioRule<EmployeeHomepageActivity> employeeActivityRule = new ActivityScenarioRule<>(EmployeeHomepageActivity.class);

    @Before
    public void setUseRole(){
        db = FirebaseDatabase.getInstance().getReference("Users");
        int id = 123;
        useRole =UseRole.getInstance();
        useRole.setCurrentRole(id,"employee");

    }
    @Test
    public void testInitialRole(){
        assertEquals("employee", useRole.getCurrentRole());
    }

    @Test
    public void testSwitchRole(){
        int id = 123;
        useRole.switchRole(id);
        assertEquals("employer", useRole.getCurrentRole());
    }

    @Test
    public void testSwitchBackRole(){
        int id = 123;
        useRole.switchRole(id);
        useRole.switchRole(id);
        assertEquals("employee", useRole.getCurrentRole());
    }

    @Test
    public void testMultipleSwitch(){
        int id = 123;
        useRole.switchRole(id);
        assertEquals("employer", useRole.getCurrentRole());

        useRole.switchRole(id);
        assertEquals("employee", useRole.getCurrentRole());

        useRole.switchRole(id);
        assertEquals("employer", useRole.getCurrentRole());
    }

    @Test
    public void testSingleInstance(){
        UseRole other = UseRole.getInstance();
        assertEquals(useRole,other);
    }

    @Test
    public void testForNotNull(){
        assertNotNull("UseRole should not be null", useRole);
        assertNotNull("Current Role is not null", useRole.getCurrentRole());
    }

    @Test
    public void testRoleUpdateInDatabase(){
        int id = 123;
        useRole.switchRole(id);

        db.child(String.valueOf(id)).child("role").get().addOnCompleteListener(event ->{
            if(event.isSuccessful()){
                String role = event.getResult().getValue(String.class);
                assertEquals("employer", role);
            }
        });
    }

}
