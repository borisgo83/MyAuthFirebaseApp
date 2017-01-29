package com.example.borisiando.myauthfirebaseapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    private EditText etUsername, etEmail, etPassword;
    private Button bRegister;

    private ProgressDialog dialogMsg;

    //Initializing Firebase objects
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (Button) findViewById(R.id.bRegister);

        dialogMsg = new ProgressDialog(RegisterActivity.this);


        mAuth = FirebaseAuth.getInstance();

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUsername = etUsername.getText().toString();
                String newEmail = etEmail.getText().toString();
                String newPassword = etPassword.getText().toString();

                createAccount( newEmail, newPassword);

            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }
    public void createAccount(String email, String password) {


        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        dialogMsg.setMessage("creating account...");
        dialogMsg.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            //creating new user in database
                            createNewUserInDatabase(etUsername.getText().toString());

                            Toast.makeText(RegisterActivity.this, "Account successfuly created.",
                                    Toast.LENGTH_SHORT).show();



                            Intent intent = new Intent(RegisterActivity.this, UserProfile.class);
                            RegisterActivity.this.startActivity(intent);
                        }

                        dialogMsg.hide();

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String username = etUsername.getText().toString();
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Required.");
            valid = false;
        }else{
            etUsername.setError(null);
        }
        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required.");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required.");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }

    private void createNewUserInDatabase(String username) {
        String userID;
        if(mAuth.getCurrentUser().getUid() != null) {
            userID = mAuth.getCurrentUser().getUid();


            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");



            // creating user object
            User user = new User(mAuth.getCurrentUser().getUid(), username, mAuth.getCurrentUser().getEmail());
            //User user = new User()

            // pushing user to 'users' node using the userId
            mDatabase.child(userID).setValue(user);
        }
    }
}
