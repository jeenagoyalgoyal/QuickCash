package com.example.quickcash.FirebaseMessaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.quickcash.Firebase.UserCrud;
import com.example.quickcash.FirebaseMessaging.MyFirebaseMessagingService;
import com.google.auth.oauth2.GoogleCredentials;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Manages payment-related notifications using Firebase Cloud Messaging (FCM).
 */
public class PaymentNotification {

    private static final String CREDENTIALS_FILE_PATH = "key.json";
    private static final String FCM_ENDPOINT = "https://fcm.googleapis.com/v1/projects/quickcash-8f278/messages:send";

    private final Context context;
    private final RequestQueue requestQueue;
    public UserCrud userCrud;

    /**
     * Initializes the PaymentNotification instance with a context and request queue.
     *
     * @param context      The application context.
     * @param requestQueue The Volley RequestQueue for handling network requests.
     */
    public PaymentNotification(Context context, RequestQueue requestQueue) {
        this.context = context;
        this.requestQueue = requestQueue;
        userCrud= new UserCrud();
    }

    /**
     * Sends payment notifications to the employer and employee.
     *
     * @param employer The email ID of the employer.
     * @param employee The email ID of the employee.
     */
    public void sendPaymentNotifications(String employer, String employee) {
        // Fetch the employer's device token
        userCrud.getUserDeviceToken(employer).addOnCompleteListener(employerTask -> {
            if (employerTask.isSuccessful()) {
                String employerToken = employerTask.getResult();
                Log.d("Employer Token", "Retrieved: " + employerToken);

                // Fetch the access token for sending notifications
                getAccessToken(new AccessTokenListener() {
                    @Override
                    public void onAccessTokenReceived(String token) {
                        // Send notifications to both employer and employee
                        sendNotificationToEmployer(token, employerToken);
                    }

                    @Override
                    public void onAccessTokenError(Exception exception) {
                        Log.e("Access Token Error", "Error retrieving access token", exception);
                        Toast.makeText(context, "Error getting access token: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Log.e("Employer Token", "Failed to retrieve employer token", employerTask.getException());
                Toast.makeText(context, "Failed to retrieve employer token", Toast.LENGTH_LONG).show();
            }
        });

        // Fetch the employee's device token
        userCrud.getUserDeviceToken(employee).addOnCompleteListener(employeeTask -> {
            if (employeeTask.isSuccessful()) {
                String employeeToken = employeeTask.getResult();
                Log.d("Employee Token", "Retrieved: " + employeeToken);

                // Fetch the access token for sending notifications
                getAccessToken(new AccessTokenListener() {
                    @Override
                    public void onAccessTokenReceived(String token) {
                        // Send notifications to both employer and employee
                        sendNotificationToEmployee(token, employeeToken);
                    }

                    @Override
                    public void onAccessTokenError(Exception exception) {
                        Log.e("Access Token Error", "Error retrieving access token", exception);
                        Toast.makeText(context, "Error getting access token: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Log.e("Employee Token", "Failed to retrieve employee token", employeeTask.getException());
                Toast.makeText(context, "Failed to retrieve employee token", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Fetches the Firebase access token for sending FCM notifications.
     *
     * @param listener The callback to handle success or failure.
     */
    private void getAccessToken(AccessTokenListener listener) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                InputStream serviceAccountStream = context.getAssets().open(CREDENTIALS_FILE_PATH);
                GoogleCredentials googleCredentials = GoogleCredentials
                        .fromStream(serviceAccountStream)
                        .createScoped(Collections.singletonList("https://www.googleapis.com/auth/firebase.messaging"));

                googleCredentials.refreshIfExpired();
                String token = googleCredentials.getAccessToken().getTokenValue();
                listener.onAccessTokenReceived(token);
                Log.d("AccessToken", "Token: " + token);
            } catch (IOException e) {
                listener.onAccessTokenError(e);
            }
        });
        executorService.shutdown();
    }

    /**
     * Sends a notification to the employer.
     *
     * @param authToken  The Firebase access token.
     * @param deviceToken The FCM device token of the employer.
     */
    private void sendNotificationToEmployer(String authToken, String deviceToken) {
        try {
            JSONObject notification = new JSONObject();
            notification.put("title", "Payment Successful");
            notification.put("body", "You have successfully paid the employee.");

            JSONObject message = new JSONObject();
            message.put("token", deviceToken);
            message.put("notification", notification);

            JSONObject requestBody = new JSONObject();
            requestBody.put("message", message);

            sendFCMRequest(requestBody, authToken);
            Log.d("EmployerN", "Sent");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Sends a notification to the employee.
     *
     * @param authToken  The Firebase access token.
     * @param deviceToken The FCM device token of the employee.
     */
    private void sendNotificationToEmployee(String authToken, String deviceToken) {
        try {
            JSONObject notification = new JSONObject();
            notification.put("title", "Payment Received");
            notification.put("body", "You have received payment from the employer.");

            JSONObject message = new JSONObject();
            message.put("token", deviceToken);
            message.put("notification", notification);

            JSONObject requestBody = new JSONObject();
            requestBody.put("message", message);

            sendFCMRequest(requestBody, authToken);
            Log.d("EmployeeN", "Sent");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an FCM notification request to the Firebase server.
     *
     * @param requestBody The notification payload.
     * @param authToken   The Firebase access token.
     */
    private void sendFCMRequest(JSONObject requestBody, String authToken) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                FCM_ENDPOINT,
                requestBody,
                response -> {
                    Log.d("FCMResponse", "Response: " + response.toString());
                    Toast.makeText(context, "Notification Sent Successfully", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Log.e("FCMError", "Error Response: " + error.toString());
                    if (error.networkResponse != null) {
                        Log.e("FCMError", "Status Code: " + error.networkResponse.statusCode);
                        Log.e("FCMError", "Error Data: " + new String(error.networkResponse.data));
                    }
                    Toast.makeText(context, "Failed to Send Notification", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authToken);
                headers.put("Content-Type", "application/json; UTF-8");
                return headers;
            }
        };

        requestQueue.add(request);
    }

    /**
     * Interface for handling Firebase access token callbacks.
     */
    private interface AccessTokenListener {
        void onAccessTokenReceived(String token);

        void onAccessTokenError(Exception exception);
    }
}
