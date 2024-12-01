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

public class PayPalPaymentProcessor {
    private static final String TAG = "PayPalPaymentProcessor";
    final String CLIENT_ID ="AUJVGR_8i6AOY0_cXuzLZgNNTO08R65CC9gGbBJ3BMJOGHOjJDs-fbFBELR7COakrnECzR1S6cKD-1Ur"; //note: working paypal ClientID
    PayPalConfiguration payPalConfiguration;
    String payID;
    String state;

    public PayPalPaymentProcessor(){
        payPalConfiguration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(CLIENT_ID);
    }

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

    public String getPayID() {
        return payID;
    }

    public String getState() {
        return state;
    }

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
                Log.e(TAG, "Error occurred with JSON Data: ", e);
            }
        }
        return false;
    }
}
