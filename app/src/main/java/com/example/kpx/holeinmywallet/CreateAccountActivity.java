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
                                database.getReference().child("IDs").child(Utility.replaceDotsWithEquals(
                                        emailField.getText().toString())).setValue(
                                        accountIdField.getText().toString()
                                );
                                //Add default preferences to database
                                database.getReference().child("NumRecommendations").child(Utility.replaceDotsWithEquals(
                                        emailField.getText().toString())).setValue(20L);
                                returnToLogin(1);
                            } else {
                                Log.d("Login", "Failure: " + task.getException().getMessage());
                                returnToLogin(2);
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
        if (accountId.length() != 24) {
            valid = false;
            accountIdField.setError("ID must be 24 digits long");
        }
        return valid;
    }
}
