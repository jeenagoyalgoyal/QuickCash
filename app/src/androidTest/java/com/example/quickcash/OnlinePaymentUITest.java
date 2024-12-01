package com.example.quickcash;
import android.content.Context;
import android.content.Intent;
import androidx.test.core.app.ApplicationProvider;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

@RunWith(JUnit4.class)
public class OnlinePaymentUITest {

    private static final int LAUNCH_TIMEOUT = 60;
    final String launcherPackageName = "com.example.quickcash";
    private UiDevice device;

    @Before
    public void setup() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Context context = ApplicationProvider.getApplicationContext();
        Intent launcherIntent = context.getPackageManager().getLaunchIntentForPackage(launcherPackageName);
        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(launcherIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackageName).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void checkPayEmployeeButton() throws UiObjectNotFoundException {
        // Log in
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        emailBox.setText("testemployer@test.com");
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        passwordBox.setText("Employer123#");
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();

        // Check if pay employee button exists
        UiObject payEmployeeButton = device.findObject(new UiSelector().text("Pay Employee"));
        assertTrue("Pay Employee button should be visible", payEmployeeButton.exists());
    }

    @Test
    public void checkFields() throws UiObjectNotFoundException {
        // Log in
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        emailBox.setText("testemployer@test.com");
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        passwordBox.setText("Employer123#");
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();

        // Navigate to the Online Payment page
        UiObject payEmployeeButton = device.findObject(new UiSelector().text("Pay Employee"));
        payEmployeeButton.clickAndWaitForNewWindow();

        //check if fields are displayed correctly
        UiObject jobTitleBox = device.findObject(new UiSelector().text("Job Title:"));
        assertTrue("job title box should be visible", jobTitleBox.exists());
        UiObject employeeNameBox = device.findObject(new UiSelector().text("Employee Name:"));
        assertTrue("employee name box should be visible", employeeNameBox.exists());
        UiObject paymentAmountBox = device.findObject(new UiSelector().text("Payment Amount:"));
        assertTrue("payment amount box should be visible", paymentAmountBox.exists());
    }

    @Test
    public void onlinePaymentTesting() throws UiObjectNotFoundException {
        // Log in
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        emailBox.setText("testemployer@test.com");
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        passwordBox.setText("Employer123#");
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();

        // Navigate to the Online Payment page
        UiObject payEmployeeButton = device.findObject(new UiSelector().text("Pay Employee"));
        payEmployeeButton.clickAndWaitForNewWindow();

        // Opens the select job popup
        UiObject selectJobButton = device.findObject(new UiSelector().text("Click to Select Job"));
        selectJobButton.longClick();

        // Selects the first job thats on the popup
        UiObject selectItemButton = device.findObject(new UiSelector().text("Select"));
        selectItemButton.click();

        // Initializes paypal payment window
        UiObject payButton = device.findObject(new UiSelector().text("Pay"));
        payButton.clickAndWaitForNewWindow();

        // choose the credit card option
        UiObject cardButton = device.findObject(new UiSelector().textContains("Card"));
        cardButton.clickAndWaitForNewWindow();

        // handle request for permissions if it pops up
        UiObject permissionButton = device.findObject(new UiSelector().textContains("While using the app"));
        if (permissionButton.exists()){
            permissionButton.longClick();
        }

        // select manual entry of card details
        UiObject keyboardButton = device.findObject(new UiSelector().textContains("Keyboard"));
        keyboardButton.click();

        // enter in card details
        UiObject cardBox = device.findObject(new UiSelector().textContains("5678"));
        cardBox.setText("4214026959287870");
        UiObject expiresBox = device.findObject(new UiSelector().textContains("MM"));
        expiresBox.setText("726");
        UiObject cvvBox = device.findObject(new UiSelector().textContains("123"));
        cvvBox.setText("123");
        UiObject doneButton = device.findObject(new UiSelector().textContains("Done"));
        doneButton.longClick();

        // checks if charge card option is visible, does not actually charge since that closes the job
        UiObject chargeButton = device.findObject(new UiSelector().textContains("Charge Card"));
        assertTrue("charge card button should be visible", chargeButton.exists());
    }
}
