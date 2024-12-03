package com.example.quickcash;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.quickcash.Firebase.JobCRUD;
import com.example.quickcash.Firebase.OnlinePaymentCRUD;
import com.example.quickcash.FirebaseMessaging.PaymentNotification;
import com.example.quickcash.adapter.PaymentJobAdapter;
import com.example.quickcash.model.Job;
import com.example.quickcash.model.PaymentEmployeeModel;
import com.example.quickcash.model.PaymentTransactionModel;
import com.example.quickcash.paypal.PayPalPaymentProcessor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity responsible for handling online payments using PayPal.
 * It handles job selection, payment processing, and transaction recording
 * in Firebase.
 */
public class OnlinePaymentActivity extends AppCompatActivity {
    private static final String TAG = "OnlinePaymentActivity";

    //UI components
    private TextView jobTitleText;
    private TextView employeeNameText;
    private TextView paymentAmountText;
    private Button paymentButton;
    private Button selectJobButton;
    private Dialog dialog;
    private ImageButton crossButton;

    //Firebase
    private FirebaseAuth mAuth;
    private String email;
    private String userID;

    //Paypal and related
    private PayPalPaymentProcessor payPalPaymentProcessor;
    public ActivityResultLauncher<Intent> activityResultLauncher;
    private JobCRUD jobCRUD;
    private OnlinePaymentCRUD onlinePaymentCRUD;
    private PaymentEmployeeModel selectedEmployee;
    private PaymentNotification paymentNotification;

    /**
     * Called when the activity is first created. Initializes the activity's components
     * and sets up required configurations.
     *
     * @param savedInstanceState Bundle containing the activity's previously saved state.
     */
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_online_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.onlinePaymentLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Create notification channel
        createNotificationChannel();


        // Setup UI
        //setup methods
        setupOnlinePaymentCrud();
        setupJobCrud();
        setupPayPalLauncher();
        setCurrentUserID();
        setupTextViews();
        setupPaymentButton();
        setupCancelButton();
        setupSelectJobButton();
        setupDialog();
    }

    /**
     * Initializes the PaymentNotification and sets up the notification channel.
     * Uses a Volley RequestQueue for sending payment notifications.
     */
    private void createNotificationChannel(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        paymentNotification = new PaymentNotification(this, requestQueue);
    }

    /**
     * Initializes the OnlinePaymentCRUD instance for interacting with the database.
     */
    private void setupOnlinePaymentCrud() {
        this.onlinePaymentCRUD = new OnlinePaymentCRUD();
    }

    /**
     * Initializes the JobCRUD instance for retrieving and managing jobs in the database.
     */
    private void setupJobCrud() {
        this.jobCRUD = new JobCRUD(FirebaseDatabase.getInstance());
    }

    /**
     * Sets up the PayPal payment processor and result handling for the payment activity.
     */
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
                        PaymentTransactionModel transaction = new PaymentTransactionModel();
                        transaction.setTransactionDetails(payID, selectedEmployee.getEmployeeId(), selectedEmployee.getEmployerId(), selectedEmployee.getJobId(),selectedEmployee.getPaymentAmount(), state);
                        onlinePaymentCRUD.pushTransaction(transaction);
                        clearEmployeeDetails();

                        //Set status of job to completed
                        jobCRUD.setStatusOfJobToCompleted(transaction.getJobId());

                        paymentNotification.sendPaymentNotifications(transaction.getEmployerId(), transaction.getEmployeeId());
                        Intent intent =new Intent(this, EmployerHomepageActivity.class);
                        startActivity(intent);
                    } else if (result.getResultCode() == PaymentActivity.RESULT_EXTRAS_INVALID) {
                        Log.e(TAG, "Payment failed due to invalid extra data (check result code)");
                        Toast.makeText(this, "Payment Failed!", Toast.LENGTH_SHORT).show();
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Toast.makeText(this, "Payment Cancelled!", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Payment canceled (did user cancel payment?)");
                    }
                }
                );
    }

    /**
     * Sets the current user's ID using Firebase Authentication.
     * Converts the email to a sanitized form for database usage.
     */
    private void setCurrentUserID(){
        this.mAuth = FirebaseAuth.getInstance();
        this.email = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;
        this.userID = sanitizeEmail(email);
    }

    /**
     * Configures the text views for displaying job and payment details.
     */
    private void setupTextViews() {
        this.jobTitleText = findViewById(R.id.paymentWindowJobTitleText);
        this.employeeNameText = findViewById(R.id.paymentWindowEmployeeNameText);
        this.paymentAmountText = findViewById(R.id.paymentWindowAmountText);
    }


    /**
     * Sets up the job selection button, allowing the user to choose a job for payment.
     */
    private void setupSelectJobButton() {
        selectJobButton = findViewById(R.id.jobSelectButton);
        selectJobButton.setOnClickListener(view -> launchJobSelectDialog());
    }

    /**
     * Configures the payment button to initiate PayPal payment when clicked.
     */
    protected void setupPaymentButton(){
        paymentButton = findViewById(R.id.paymentButton);
        paymentButton.setOnClickListener(view -> paypalPayment());
    }

    /**
     * Configures the cancel button to clear selected employee details when clicked.
     */
    protected void setupCancelButton(){
        paymentButton = findViewById(R.id.paymentCancelButton);
        paymentButton.setOnClickListener(view -> clearEmployeeDetails());
    }

    /**
     * Clears all details of the currently selected employee from the UI and stored employee object.
     */
    public void clearEmployeeDetails(){
        this.selectedEmployee = null;
        this.jobTitleText.setText("");
        this.employeeNameText.setText("");
        this.paymentAmountText.setText("");
    }

    /**
     * Configures a dialog for displaying available jobs in a RecyclerView.
     */
    private void setupDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.payment_jobs_select_dialog);
        crossButton = dialog.findViewById(R.id.crossButton);
        crossButton.setOnClickListener(v -> dialog.dismiss());
    }


    /**
     * Displays the job selection popup dialog, allowing the user to choose a job for payment.
     */
    private void launchJobSelectDialog() {
        RecyclerView recyclerView = dialog.findViewById(R.id.paymentJobsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<PaymentEmployeeModel> employeeList = new ArrayList<>();
        //listener is placed in recycler view in dialog box
        PaymentJobAdapter adapter = new PaymentJobAdapter(employeeList, view -> {
            PaymentEmployeeModel selectedItem = (PaymentEmployeeModel) view.getTag(); //get selected item from recyclerview using Tag
            dialog.dismiss();
            handleSelectedItem(selectedItem);
        });
        recyclerView.setAdapter(adapter);
        //Fetch the jobs and put them in recycler view
        jobCRUD.getInProgressJobs(userID).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (Job j : task.getResult()) {
                    if (j.getEmployeeId() != null) {
                        employeeList.add(new PaymentEmployeeModel(j.getJobId(), j.getJobTitle(), j.getEmployeeName(), j.getEmployeeId(), j.getEmployerId(), j.getSalary()));
                    }
                }
                if (employeeList.isEmpty()) {
                    Toast.makeText(this, "No in-progress jobs found!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "No In-progress jobs detected (check firebase?)");
                }
                adapter.notifyDataSetChanged(); //reflect data set change in recycler view
            } else {
                Toast.makeText(this, "Failed to fetch jobs!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "(is network connection ok?) Failed to fetch jobs: ", task.getException());
            }
        });
        //show dialog box once data is loaded into it
        dialog.show();
    }

    /**
     * Handles the job selected by the user from the job selection dialog.
     *
     * @param selectedItem The selected job's details.
     */
    private void handleSelectedItem(PaymentEmployeeModel selectedItem) {
        //update UI with details
        this.selectedEmployee = selectedItem;
        this.jobTitleText.setText(selectedEmployee.getJobTitle());
        this.employeeNameText.setText(selectedEmployee.getEmployeeName());
        this.paymentAmountText.setText(String.valueOf(selectedEmployee.getPaymentAmount()));
    }

    /**
     * Initiates the PayPal payment process for the selected job and employee.
     */
    protected void paypalPayment() {
        if (this.selectedEmployee != null) {
            String jobTitle = this.selectedEmployee.getJobTitle();
            String employeeName = this.selectedEmployee.getEmployeeName();
            int paymentAmount = this.selectedEmployee.getPaymentAmount();
            boolean isPaymentInitiated = payPalPaymentProcessor.handlePayment(this, employeeName, paymentAmount);
            if (!isPaymentInitiated) {
                Log.e(TAG, "Failed to initiate payment");
                Toast.makeText(this, "Payment initialization failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e(TAG, "No employee selected");
            Toast.makeText(this, "No employee selected!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sanitizes an email address by replacing '.' with ',' for Firebase compatibility.
     *
     * @param email The email address to be sanitized.
     * @return A sanitized email string.
     */
    private String sanitizeEmail(String email) {
        return email.replace(".", ",");
    }
}