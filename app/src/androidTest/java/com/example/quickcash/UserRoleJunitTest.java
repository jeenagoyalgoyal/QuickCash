package com.example.quickcash;

import static org.junit.Assert.assertEquals;

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
}
