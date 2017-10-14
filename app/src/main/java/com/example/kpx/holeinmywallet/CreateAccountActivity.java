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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountActivity extends AppCompatActivity {

    /*
    TODO: Setup firebase authentication and database
    TODO: Validate entered information
     */

    private EditText emailField, passwordField, confirmPasswordField, accountIdField;
    private FirebaseAuth authentication;

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
    }

    public void createAccount(View view) {
        if (verifyFields()) {
            Log.d("Login", "Fields verified");
            authentication.createUserWithEmailAndPassword(emailField.toString(), passwordField.toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("Login", "Success");
                                setResult(1);
                            } else {
                                setResult(0);
                                Log.d("Login", "Failed");
                            }
                        }
                    });
            finish();
        }
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
        if (accountId.length() < -1) {
            valid = false;
            accountIdField.setError("ID must be 24 digits long");
        }
        return valid;
    }

}
