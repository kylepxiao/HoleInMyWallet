package com.example.kpx.holeinmywallet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.okhttp.internal.Util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText emailField, passwordField, confirmPasswordField, accountIdField;
    private FirebaseAuth authentication;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //Fields
        emailField = (EditText)findViewById(R.id.create_email_field);
        passwordField = (EditText)findViewById(R.id.create_password_field);
        confirmPasswordField = (EditText)findViewById(R.id.create_password_confirm_field);
        accountIdField = (EditText)findViewById(R.id.create_account_id_field);

        authentication = FirebaseAuth.getInstance();
        authentication.signOut();

        database = FirebaseDatabase.getInstance();
    }

    public void createAccount(View view) {

        if (verifyFields()) {
            Log.d("Login", "Fields verified");
            authentication.createUserWithEmailAndPassword(emailField.getText().toString(), passwordField.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("Login", "Success");

                                //Add account ID to database
                                database.getReference().child(Utility.replaceDotsWithEquals(
                                        emailField.getText().toString())).setValue(
                                        accountIdField.getText().toString()
                                );
                                returnToLogin(1);
                            } else {
                                Log.d("Login", "Failure: " + task.getException().getMessage());
                                returnToLogin(0);
                            }
                        }
                    });
        }
    }

    private void returnToLogin(int resultCode) {
        setResult(resultCode);
        finish();
    }

    private boolean verifyFields() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if (!email.contains("@") || email.equals("")) {
            valid = false;
            emailField.setError("Please enter valid email address");
        }

        String password = passwordField.getText().toString();
        String confirmPassword = passwordField.getText().toString();
        if (password.length() < 8) {
            valid = false;
            passwordField.setError("Must be 8 characters or longer");
        }
        if (!password.equals(confirmPassword)) {
            valid = false;
            confirmPasswordField.setError("Passwords must match");
        }

        String accountId = accountIdField.getText().toString();
        if (!TextUtils.isDigitsOnly(accountId)) {
            valid = false;
            accountIdField.setError("ID must consist of numbers only");
        }
        if (accountId.length() != 24) {
            valid = false;
            accountIdField.setError("ID must be 24 digits long");
        }
        return valid;
    }

    private void addUserRecommendationToQueue(){
        RequestQueue mRequestQueue = MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();

        // Start the queue
        mRequestQueue.start();

        String url = "https://westus.api.cognitive.microsoft.com/recommendations/v4.0/models/864912de-b987-4f45-975b-4eadf4d35ae9/recommend/user?userId=85526&numberOfResults=1";

        // Request a string response from the provided URL.
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("includeMetaData", "false");
        parameters.put("buildId", "1660942");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, new JSONObject(parameters), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("asdf", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", "Error with JSON Volley response");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Ocp-Apim-Subscription-Key", "586a2994e7cb4523beb170e60f0954c3");
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }
}
