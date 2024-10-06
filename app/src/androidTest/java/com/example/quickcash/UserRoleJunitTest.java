package com.example.quickcash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.FirebaseDatabaseKtxRegistrar;

import org.junit.Before;
import org.junit.Test;

public class UserRoleJunitTest {

    private UseRole useRole;
    private DatabaseReference db;

    @Before
    public void setUseRole(){
        db = FirebaseDatabase.getInstance().getReference("testUsers");
        useRole =UseRole.getInstance();
    }
    @Test
    public void testInitialRole(){
        assertEquals("employee", useRole.getCurrentRole());
    }

    @Test
    public void testSwitchRole(){
        String id = "testUser";
        useRole.switchRole(id);
        assertEquals("employer", useRole.getCurrentRole());
    }

    @Test
    public void testSwitchBackRole(){
        String id = "testUser";
        useRole.switchRole(id);
        useRole.switchRole(id);
        assertEquals("employee", useRole.getCurrentRole());
    }

    @Test
    public void testMultipleSwitch(){
        String id = "testUser";
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
        String id = "testUser";
        useRole.switchRole(id);

        db.child(id).child("role").get().addOnCompleteListener(event ->{
            if(event.isSuccessful()){
                String role = event.getResult().getValue(String.class);
                assertEquals("employer", role);
            }
        });
    }
}
