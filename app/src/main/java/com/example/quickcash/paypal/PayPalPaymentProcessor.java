package com.example.quickcash.paypal;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.quickcash.OnlinePaymentActivity;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Handles PayPal payment processing, including initiating payments and processing confirmations.
 */
public class PayPalPaymentProcessor {
    private static final String TAG = "PayPalPaymentProcessor";
    //ID usually not included in prod in industry, but for purposes of course project, is it kept here to avoid complications.
    final String CLIENT_ID ="AUJVGR_8i6AOY0_cXuzLZgNNTO08R65CC9gGbBJ3BMJOGHOjJDs-fbFBELR7COakrnECzR1S6cKD-1Ur"; //note: working paypal ClientID
    PayPalConfiguration payPalConfiguration;
    String payID;
    String state;

    /**
     * Constructs a new PayPalPaymentProcessor and sets up the PayPal configuration.
     */
    public PayPalPaymentProcessor(){
        payPalConfiguration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(CLIENT_ID);
    }

    /**
     * Initiates a payment request using PayPal.
     *
     * @param context       The context from which the payment is initiated (usually an activity).
     * @param employeeName  The name of the employee for whom the payment is being made.
     * @param amount        The payment amount in CAD.
     * @return {@code true} if the payment request is successfully initiated, {@code false} otherwise.
     */
    public boolean handlePayment(Context context, String employeeName, int amount) {
        if (amount <= 0) {
            Log.e(TAG, "Invalid payment amount");
            return false;
        }
        // Create PayPalPayment
        PayPalPayment payment = new PayPalPayment(
                new BigDecimal(amount),
                "CAD",
                "Payment for " + employeeName,
                PayPalPayment.PAYMENT_INTENT_SALE
        );

        // Create intent to launch PayPal PaymentActivity
        Intent intent = new Intent(context, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        // Start the PaymentActivity over OnlinePaymentActivity
        ((OnlinePaymentActivity) context).activityResultLauncher.launch(intent);
        return true;
    }

    /**
     * Retrieves the payment ID from the most recent PayPal transaction.
     *
     * @return The payment ID.
     */
    public String getPayID() {
        return payID;
    }

    /**
     * Retrieves the state of the most recent PayPal transaction.
     *
     * @return The payment state (e.g., approved, pending).
     */
    public String getState() {
        return state;
    }

    /**
     * Handles the PayPal payment confirmation after the PaymentActivity returns a result.
     *
     * @param confirmation The payment confirmation object from PayPal.
     * @return true if the confirmation was successfully processed, false otherwise.
     */
    public boolean handlePaymentConfirmation(PaymentConfirmation confirmation){
        if (confirmation!=null){
            try{
                String paymentDetails = confirmation.toJSONObject().toString(4);
                JSONObject payObj = new JSONObject(paymentDetails);
                this.payID = payObj.getJSONObject("response").getString("id");
                this.state = payObj.getJSONObject("response").getString("state");
                Log.e(TAG, String.format("Payment %s%n with payment id is %s", state, payID));
                return true;
            } catch (JSONException e) {
                Log.e(TAG, "Error occurred with JSON data parsing: ", e);
            }
        }
        return false;
    }

    /**
     * Processes a payment response represented as a JSONObject.
     *
     * @param confirmationJson A JSON object containing the PayPal response.
     * @return true if the response was successfully processed, false otherwise.
     */
    public boolean handleResponse(JSONObject confirmationJson){
        try{
            this.payID = confirmationJson.getJSONObject("response").getString("id");
            this.state = confirmationJson.getJSONObject("response").getString("state");
            return true;
        } catch (JSONException e) {
            Log.e(TAG, "Error occurred with JSON data parsing: ", e);
        }
        return false;
    }
}
