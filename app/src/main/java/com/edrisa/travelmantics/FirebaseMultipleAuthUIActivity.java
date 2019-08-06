package com.edrisa.travelmantics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.edrisa.travelmantics.models.Users;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class FirebaseMultipleAuthUIActivity extends AppCompatActivity {

    private static final String TAG = FirebaseMultipleAuthUIActivity.class.getName();

    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth m_auth;
    private FirebaseUser current_user;
    private DatabaseReference mDatabase;

    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreateFired");
        configureFirebase();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart Fired");
        checkIfLoggedIn();
        launchFirebaseSignInIntent();
    }

    private void configureFirebase(){
        Log.d(TAG, "configureFirebase Fired");
        m_auth = FirebaseAuth.getInstance();
        current_user = m_auth.getCurrentUser();
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    private void checkIfLoggedIn(){
        Log.d(TAG, "checkIfLoggedIn Fired");
        if(current_user != null){
            gotoMainActivity();
        }
    }

    private void launchFirebaseSignInIntent(){
        Log.d(TAG, "Launching firebase intent");
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult Fired");
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                current_user = FirebaseAuth.getInstance().getCurrentUser();
//                writeNewUser(user);
                gotoMainActivity();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Toast.makeText(FirebaseMultipleAuthUIActivity.this, "Login failed, please try again.", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Login Error: " + ((response.getError().getMessage() != null) ? response.getError().getMessage() : "" ));
            }
        }
    }

    private void writeNewUser(FirebaseUser user){
        Log.d(TAG, "writeNewUser Fired");
        Users users = new Users(user.getDisplayName(), user.getEmail());
        mDatabase.child("users").child(user.getUid()).setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "writeNewUser Success");
                gotoMainActivity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "writeNewUser Failed");
                signOut();
            }
        });
    }


    private void gotoMainActivity(){
        Log.d(TAG, "gotoMainActivity Fired");
        startActivity(new Intent(FirebaseMultipleAuthUIActivity.this, ListActivity.class));
        finish();
    }

    private void signOut(){
        Log.d(TAG, "signOut Fired");
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(FirebaseMultipleAuthUIActivity.this, "Closing app. please try again.", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
    }
}
