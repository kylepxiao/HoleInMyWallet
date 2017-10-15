package com.example.kpx.holeinmywallet;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    static final int REQUEST_CODE = 11;

    EditText emailField, passwordField;

    FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = (EditText)findViewById(R.id.login_email_field);
        passwordField = (EditText)findViewById(R.id.login_password_field);

        authentication = FirebaseAuth.getInstance();
        authentication.signOut();
    }

    public void launchCreateAccount(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void signIn(View view) {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        authentication.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            launchMainActivity();
                        } else {
                            Toast.makeText(getApplicationContext(), "Could not sign in", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void launchMainActivity() {
        Toast.makeText(getApplicationContext(), "Successfully signed in user: " + authentication.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MapVisualization.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == 1) {
                //Success
                Toast.makeText(getApplicationContext(), "Successfully created account", Toast.LENGTH_LONG).show();
            } else if (resultCode == 2) {
                Toast.makeText(getApplicationContext(), "Could not create account", Toast.LENGTH_LONG).show();
            }
        }
    }
}