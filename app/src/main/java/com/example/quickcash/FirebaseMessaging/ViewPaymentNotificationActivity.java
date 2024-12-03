package com.example.quickcash.FirebaseMessaging;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quickcash.EmployeeHomepageActivity;
import com.example.quickcash.EmployerHomepageActivity;
import com.example.quickcash.R;

/**
 * This activity handles the display of payment notifications.
 * When a notification is clicked, this activity is launched to show the notification details.
 */
public class ViewPaymentNotificationActivity extends AppCompatActivity {
    private TextView titleTV;
    private TextView bodyTV;
    private Button homeButton;

    /**
     * Called when the activity is first created.
     * Sets the content view and initializes the UI components.
     *
     * @param savedInstanceState Saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_payment_notification);
        init();
        setData();
    }

    /**
     * Initializes the UI components and sets up event listeners.
     */
    private void init() {
        //binding the views with the variables
        titleTV = findViewById(R.id.titleTV);
        bodyTV = findViewById(R.id.bodyTV);

        homeButton = findViewById(R.id.paymentNotificationButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(titleTV.getText()=="Payment Received") {
                    Intent intent = new Intent(ViewPaymentNotificationActivity.this, EmployeeHomepageActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(ViewPaymentNotificationActivity.this, EmployerHomepageActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Retrieves the notification data passed through the intent and displays it.
     */
    private void setData() {
        //whatever data is received in the push notification, the variables are being set to that
        final Bundle extras = getIntent().getExtras();
        final String title = extras.getString("title");
        final String body = extras.getString("body");

        titleTV.setText(title);
        bodyTV.setText(body);

    }
}

