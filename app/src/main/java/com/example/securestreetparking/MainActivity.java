package com.example.securestreetparking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    Button monitoring;
    Button viewData;
    Button logOut;

    public static final int RC_SIGN_IN = 1;

    boolean STATUS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        mFirebaseAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        monitorButton();




        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    backgroundVideo();
//                    Toast.makeText(MainActivity.this, "Welcome to Secure Tech.", Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out

                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build());

//                    AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
//                            .Builder(R.layout.activity_main)
//                            .setGoogleButtonId(R.id.btn_holder)
//                            .setEmailButtonId(R.id.email_button)
//                            // ...
//                            .build();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setTheme(R.style.GreenTheme)
                                    .setLogo(R.drawable.car1)
                                    .setAvailableProviders(providers)
//                                    .setAuthMethodPickerLayout(customLayout)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };


    }









    public void click(View view) {
//        Toast.makeText(getApplicationContext(), "HELLO" + STATUS, Toast.LENGTH_SHORT).show();
        STATUS = !STATUS;
        myRef.setValue(STATUS);

    }


    public void logOut(View view){
        Toast.makeText(getApplicationContext(),"HELLO",Toast.LENGTH_SHORT).show();
        AuthUI.getInstance()
                .signOut(this);

    }



    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }


    private void monitorButton(){


        monitoring = findViewById(R.id.surveillance);
        logOut = findViewById(R.id.log_out);
        viewData = findViewById(R.id.data);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                if(value == "true"){
                    monitoring.setBackground(getResources().getDrawable(R.drawable.button, getTheme()));
                    viewData.setBackground(getResources().getDrawable(R.drawable.button, getTheme()));
                    logOut.setBackground(getResources().getDrawable(R.drawable.button, getTheme()));

                    STATUS = true;
                }
                else {
                    monitoring.setBackground(getResources().getDrawable(R.drawable.button_off, getTheme()));
                    viewData.setBackground(getResources().getDrawable(R.drawable.button_off, getTheme()));
                    logOut.setBackground(getResources().getDrawable(R.drawable.button_off, getTheme()));
                    STATUS = false;
                } }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

//                Toast.makeText(getApplicationContext(),"Please Connect to Internet", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void viewData(View view){
        Intent intent = new Intent(this,DataActivity.class);
        startActivity(intent);
    }

    private void backgroundVideo(){

        final VideoView videoview =  findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video1);
        videoview.setVideoURI(uri);
        videoview.start();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0,0);
                mp.setLooping(true);
            }
        });
    }
}
