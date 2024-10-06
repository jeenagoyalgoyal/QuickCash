package com.example.quickcash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class UserRoleJunitTest {

    private UseRole useRole;

    @Before
    public void setUseRole(){
        useRole= UseRole.getInstance();
    }
    @Test
    public void testInitialRole(){
        assertEquals("employee", useRole.getCurrentRole());
    }

    @Test
    public void testSwitchRole(){
        useRole.switchRole();
        assertEquals("employer", useRole.getCurrentRole());
    }

    @Test
    public void testSwitchBackRole(){
        useRole.switchRole();
        useRole.switchRole();
        assertEquals("employee", useRole.getCurrentRole());
    }

    @Test
    public void testMultipleSwitch(){
        useRole.switchRole();
        assertEquals("employer", useRole.getCurrentRole());

        useRole.switchRole();
        assertEquals("employee", useRole.getCurrentRole());

        useRole.switchRole();
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
}
