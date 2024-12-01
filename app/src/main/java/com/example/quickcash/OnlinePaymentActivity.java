package com.example.quickcash;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.adapter.PaymentJobAdapter;
import com.example.quickcash.model.PaymentEmployeeModel;
import com.example.quickcash.paypal.PayPalPaymentProcessor;
import com.google.firebase.auth.FirebaseAuth;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.util.ArrayList;
import java.util.List;

public class OnlinePaymentActivity extends AppCompatActivity {
    private static final String TAG = "OnlinePaymentActivity";
    private TextView jobTitleText;
    private TextView employeeNameText;
    private TextView paymentAmountText;
    private Button paymentButton;
    private Button selectJobButton;
    private Dialog dialog;
    private ImageButton crossButton;
    private FirebaseAuth mAuth;
    private String email;
    private String userID;
    private PayPalPaymentProcessor payPalPaymentProcessor;
    public ActivityResultLauncher<Intent> activityResultLauncher;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_online_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.onlinePaymentLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupPayPalLauncher();
        setCurrentUserID();
        //setup UI
        setupTextViews();
        setupPaymentButton();
        setupSelectJobButton();
        setupDialog();
    }

    private void setupPayPalLauncher() {
        payPalPaymentProcessor = new PayPalPaymentProcessor();
        // Setup ActivityResultLauncher
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        final PaymentConfirmation confirmation = result.getData().getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                        payPalPaymentProcessor.handlePaymentConfirmation(confirmation);
                        String payID = payPalPaymentProcessor.getPayID();
                        String state = payPalPaymentProcessor.getState();
                        Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, String.format("Payment %s%n with payment id is %s", state, payID));
                    } else if (result.getResultCode() == PaymentActivity.RESULT_EXTRAS_INVALID) {
                        Log.e(TAG, "Payment failed due to invalid extra data (check result code)");
                        Toast.makeText(this, "Payment Failed!", Toast.LENGTH_SHORT).show();
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Toast.makeText(this, "Payment Cancelled!", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Payment canceled (did user cancel payment?)");

                    }
                });
    }

    private void setCurrentUserID(){
        this.mAuth = FirebaseAuth.getInstance();
        this.email = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;
        this.userID = sanitizeEmail(email);
    }

    private void setupTextViews() {
        this.jobTitleText = findViewById(R.id.paymentWindowJobTitleText);
        this.employeeNameText = findViewById(R.id.paymentWindowEmployeeNameText);
        this.paymentAmountText = findViewById(R.id.paymentWindowAmountText);
    }

    private void setupSelectJobButton() {
        selectJobButton = findViewById(R.id.jobSelectButton);
        selectJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchJobSelectDialog();
            }
        });
    }

    protected void setupPaymentButton(){
        paymentButton = findViewById(R.id.paymentButton);
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paypalPayment();
            }
        });
    }

    private void setupDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.payment_jobs_select_dialog);
        crossButton = dialog.findViewById(R.id.crossButton);
        crossButton.setOnClickListener(v -> dialog.dismiss());
    }

    private void launchJobSelectDialog() {
        // Initialize dummy data TEMPORARY
        List<PaymentEmployeeModel> employeeList = new ArrayList<>();
        employeeList.add(new PaymentEmployeeModel("Software Developer", "Alice", 5000));
        employeeList.add(new PaymentEmployeeModel("UI/UX Designer", "Bob", 4500));
        employeeList.add(new PaymentEmployeeModel("Project Manager", "Charlie", 7000));

        // Set up recyclerView in the popup dialog
        RecyclerView recyclerView = dialog.findViewById(R.id.paymentJobsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and set the adapter with callback mechanism
        PaymentJobAdapter adapter = new PaymentJobAdapter(employeeList, view -> {
            PaymentEmployeeModel selectedItem = (PaymentEmployeeModel) view.getTag();
            dialog.dismiss(); // Close the dialog once item selected
            handleSelectedItem(selectedItem); // Handle the selected item in parent activity
        });

        recyclerView.setAdapter(adapter); //set data in recycler view
        dialog.show(); // Show the popup dialog
    }

    private void handleSelectedItem(PaymentEmployeeModel selectedItem) {
        // Update the UI or perform actions based on the selected item
        this.jobTitleText.setText(selectedItem.getJobTitle());
        this.employeeNameText.setText(selectedItem.getEmployeeName());
        this.paymentAmountText.setText(String.valueOf(selectedItem.getPaymentAmount()));
        Toast.makeText(this, "Selected: " + selectedItem.getJobTitle(), Toast.LENGTH_SHORT).show();//temp
    }


    protected void paypalPayment() {
        String jobTitle = this.jobTitleText.getText().toString();
        String employeeName = this.employeeNameText.getText().toString();
        int paymentAmount = Integer.parseInt(this.paymentAmountText.getText().toString());

        boolean isPaymentInitiated = payPalPaymentProcessor.handlePayment(this, employeeName, paymentAmount);
        if (!isPaymentInitiated) {
            Log.e(TAG, "Failed to initiate payment");
            Toast.makeText(this, "Payment initialization failed", Toast.LENGTH_SHORT).show();
        }
    }

    private String sanitizeEmail(String email) {
        return email.replace(".", ",");
    }
}