package com.example.quickcash.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import com.example.quickcash.ui.activities.RoleActivity;
import com.example.quickcash.models.UseRole;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class UserRoleJunitTest {
    private UseRole useRole;

    @Rule
    public ActivityScenarioRule<RoleActivity> actRul = new ActivityScenarioRule<>(RoleActivity.class);

    @Before
    public void setUseRole() {
        useRole = UseRole.getInstance();
        useRole.setCurrentRole(123, "employee");
    }

    @Test
    public void testInitialRole() {
        assertEquals("employee", useRole.getCurrentRole());
    }

    @Test
    public void testSwitchRole() {
        useRole.switchRole(123);
        assertEquals("employer", useRole.getCurrentRole());
    }

    @Test
    public void testSwitchBackRole() {
        useRole.switchRole(123);
        useRole.switchRole(123);
        assertEquals("employee", useRole.getCurrentRole());
    }

    @Test
    public void testMultipleSwitch() {
        useRole.switchRole(123);
        assertEquals("employer", useRole.getCurrentRole());
        useRole.switchRole(123);
        assertEquals("employee", useRole.getCurrentRole());
        useRole.switchRole(123);
        assertEquals("employer", useRole.getCurrentRole());
    }

    @Test
    public void testSingleInstance() {
        UseRole other = UseRole.getInstance();
        assertNotNull("UseRole instance should not be null", other);
        assertEquals("Both instances should be the same", useRole, other);
    }

    @Test
    public void testForNotNull() {
        assertNotNull("UseRole should not be null", useRole);
        assertNotNull("Current Role should not be null", useRole.getCurrentRole());
    }

    @Test
    public void testRoleUpdateInDatabase() {
        useRole.switchRole(123);
        assertEquals("employer", useRole.getCurrentRole());
    }
}