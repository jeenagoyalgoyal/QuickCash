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

public class JobAcceptedNotification {

    private static final String CREDENTIALS_FILE_PATH = "key.json";
    private static final String FCM_ENDPOINT = "https://fcm.googleapis.com/v1/projects/quickcash-8f278/messages:send";

    private final Context context;
    private final RequestQueue requestQueue;
    public UserCrud userCrud;

    public JobAcceptedNotification(Context context, RequestQueue requestQueue) {
        this.context = context;
        this.requestQueue = requestQueue;
        userCrud = new UserCrud();
    }

    public void sendJobNotificationToEmployee(String employee) {
        // Fetch the employer's device token
        userCrud.getUserDeviceToken(employee).addOnCompleteListener(employeeTask -> {
            if (employeeTask.isSuccessful()) {
                String employeeToken = employeeTask.getResult();
                Log.d("Employee Token", "Retrieved: " + employeeToken);

                // Fetch the access token for sending notifications
                getAccessToken(new AccessTokenListener() {
                    @Override
                    public void onAccessTokenReceived(String token) {
                        // Send notifications to both employer and employee
                        sendJobNotificationToEmployee(token, employeeToken);
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

    private void sendJobNotificationToEmployee(String authToken, String deviceToken) {
        try {
            JSONObject notification = new JSONObject();
            notification.put("title", "You have been accepted!");

            notification.put("body", "Contact the employer for further details");
            JSONObject message = new JSONObject();
            message.put("token", deviceToken);
            message.put("notification", notification);

            JSONObject requestBody = new JSONObject();
            requestBody.put("message", message);

            sendFCMRequest(requestBody, authToken);
            Log.d("Employee", "Accepted notif");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendFCMRequest(JSONObject requestBody, String authToken) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, FCM_ENDPOINT, requestBody, response -> {
            Log.d("FCMResponse", "Response: " + response.toString());
            Toast.makeText(context, "Notification Sent Successfully", Toast.LENGTH_SHORT).show();
        }, error -> {
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

    // Interface for Access Token Listener
    private interface AccessTokenListener {
        void onAccessTokenReceived(String token);

        void onAccessTokenError(Exception exception);
    }
}
