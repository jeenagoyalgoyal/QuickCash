package com.example.quickcash;

import android.telephony.ims.RegistrationManager;

import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;

public class registrationEspressoTest {

    private activityScenario<RegistrationActivity> regActivityScenario;

    @Before
    public void setup(){
        regActivityScenario = ActivityScenario.launch(RegistrationActivity.class);
    }
    

}
