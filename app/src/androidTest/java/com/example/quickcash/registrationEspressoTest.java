package com.example.quickcash;

import androidx.test.core.app.ActivityScenario;
import org.junit.Before;
import androidx.test.espresso.Espresso;
import org.junit.Test;

public class registrationEspressoTest {

    private ActivityScenario<RegistrationActivity> regActivityScenario;

    @Before
    public void setup(){
        regActivityScenario = ActivityScenario.launch(RegistrationActivity.class);
    }

    @Test
    public void nameTest() {

    }

    @Test
    public void invalidNameTest(){

    }
}
