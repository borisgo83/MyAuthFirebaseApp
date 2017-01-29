package com.example.borisiando.myauthfirebaseapp;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class UserProfile extends AppCompatActivity {

    private static final String TAG = "UserProfile";

    private int myEyesCounter;

    private EditText etID;
    private Button bOpenEye;
    private TextView tvUsername, tvEmail, tvOpenedEyesOnMe, tvMyOpenedEyes;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference databaseRef;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        etID = (EditText) findViewById(R.id.etWhammyName);
        bOpenEye = (Button) findViewById(R.id.bOpenEye);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvOpenedEyesOnMe = (TextView) findViewById(R.id.tvOpenedEyesOnMe);
        tvMyOpenedEyes = (TextView) findViewById(R.id.tvMyOpenedEyes);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser == null) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("no user")
                    .create()
                    .show();
        }
        databaseRef = FirebaseDatabase.getInstance().getReference("users");
        databaseRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null) {
                    User tempUser = dataSnapshot.getValue(User.class);

                    tvUsername.setText(tempUser.getUserName());
                    tvEmail.setText(tempUser.geteMail());
                    tvOpenedEyesOnMe.setText(String.valueOf(tempUser.getOpenedEyesOnMe()));
                    tvMyOpenedEyes.setText(String.valueOf(tempUser.getMyOpenedEyes()));
                    myEyesCounter = tempUser.getMyOpenedEyes();
                }else{
                    Log.w(TAG, "dataSnapshot = NULL");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadData:onCancelled", databaseError.toException());
            }
        });


        bOpenEye.setOnClickListener(new View.OnClickListener() {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users");
            int tatgetEyesCounter;
            int myOpenedEyes;
            @Override
            public void onClick(View view) {


                Query query = databaseRef.orderByChild("userName").equalTo(etID.getText().toString());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snap: dataSnapshot.getChildren()) {
                            Log.v("children", snap.getChildren().toString());
                            User user = snap.getValue(User.class);
                            user.setOpenedEyesOnMe(user.getOpenedEyesOnMe()+1);
                            databaseRef.child(snap.getKey()).setValue(user);

                            myEyesCounter = myEyesCounter - 1;
                            databaseRef.child(mAuth.getCurrentUser().getUid()).child("myOpenedEyes").setValue(myEyesCounter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                etID.setText("");
                /*
                final String userId = "ienB8FlM52Zb3jJLMT21Fy0C2Lm1";

                databaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int eyesOnTarget, myOpenedEyes;
                        User user = dataSnapshot.getValue(User.class);
                       // eyesOnTarget = user.getOpenedEyesOnMe();
                       // eyesOnTarget++;
                        //tatgetEyesCounter = eyesOnTarget;
                        user.setOpenedEyesOnMe(user.getOpenedEyesOnMe()+1);
                        databaseRef.child(userId).setValue(user);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "loadTargetEyesCounter:onCancelled", databaseError.toException());

                    }
                }); */

/*
                databaseRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        user.setMyOpenedEyes(user.getMyOpenedEyes()-1);
                        databaseRef.child(mAuth.getCurrentUser().getUid()).setValue(user);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "change myOpenedEyes:onCancelled", databaseError.toException());
                    }
                });
*/
                //databaseRef.child(userId).child("openedEyesOnMe").setValue(tatgetEyesCounter);
                //databaseRef.child()
                //myOpenedEyes = Integer.parseInt(tvMyOpenedEyes.getText().toString());
                //databaseRef.child(mAuth.getCurrentUser().getUid()).child("myOpenedEyes").setValue(tvMyOpenedEyes.getText());

                //String key = mFirebaseDatabase.child("users/" + userId + "/openedEyesOnMe/").push().getKey();
                //mFirebaseDatabase.child(userId).updateChildren(1);
               // databaseRef.orderByChild("email").equalTo(etID.getText().toString());
            }
        });

    }
}
