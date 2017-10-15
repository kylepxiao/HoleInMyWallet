package com.example.kpx.holeinmywallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    private static long MAX_RECOMMENDATIONS = 20L;

    private EditText numRecommendations;
    private FirebaseDatabase database;
    private FirebaseAuth authentication;
    private FirebaseUser user;

    private long firebaseValue = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        database = FirebaseDatabase.getInstance();
        authentication = FirebaseAuth.getInstance();
        user = authentication.getCurrentUser();

        numRecommendations = findViewById(R.id.numRecommendations);
        database.getReference().child("NumRecommendations").child(Utility.replaceDotsWithEquals(user.getEmail()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        numRecommendations.setText(Long.toString(dataSnapshot.getValue(Long.class)));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        numRecommendations.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = numRecommendations.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    text = "0";
                    numRecommendations.setText(text);
                }
                if (Long.parseLong(text) > MAX_RECOMMENDATIONS) {
                    text = Long.toString(MAX_RECOMMENDATIONS);
                    numRecommendations.setText(text);
                }
                database.getReference().child("NumRecommendations").child(Utility.replaceDotsWithEquals(user.getEmail()))
                        .setValue(!TextUtils.isEmpty(text) ? Long.parseLong(text) : 0L);
            }
        });
    }
}
