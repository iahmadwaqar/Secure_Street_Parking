package com.example.securestreetparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 1;
    String value;
    Button monitoring, viewData, logOut, retrieveImageButton;
    ImageView background, retrieveImageView;
    boolean STATUS;
    private FirebaseDatabase database;
    private DatabaseReference myRef, retrieveImage, cameraStatus;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ValueEventListener listener, statusListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Internet Connection is Required", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        }

        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    setContentView(R.layout.activity_main);
                    FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference("message");
                    retrieveImage = database.getReference("retrieve");
                    cameraStatus = database.getReference("status");
                    monitorButton();
                    statusListener = cameraStatus.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            Glide.with(background.getContext())
                                    .load(dataSnapshot.getValue().toString())
                                    .thumbnail(Glide.with(retrieveImageView.getContext()).load(R.drawable.camera_indicator))
                                    .into(background);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    // User is signed

                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build());

                    AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                            .Builder(R.layout.sign)
                            .setGoogleButtonId(R.id.Google)
                            .setEmailButtonId(R.id.Email)
                            .build();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setTheme(R.style.GreenTheme)
                                    .setLogo(R.drawable.main_icon)
                                    .setAvailableProviders(providers)
                                    .setAuthMethodPickerLayout(customLayout)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };


    }

    public void click(View view) {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Internet Connection is Required", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            return;
        }
        STATUS = !STATUS;
        myRef.setValue(STATUS);

    }


    public void logOut(View view) {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Internet Connection is Required", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            return;
        }
        AuthUI.getInstance()
                .signOut(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listener != null)
            retrieveImage.removeEventListener(listener);
        if (mAuthStateListener != null)
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        if (statusListener != null)
            cameraStatus.removeEventListener(statusListener);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (listener != null)
            retrieveImage.removeEventListener(listener);
        if (mAuthStateListener != null)
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        if (statusListener != null)
           cameraStatus.removeEventListener(statusListener);
    }


    private void monitorButton() {

        retrieveImageView = findViewById(R.id.retrieveImageView);
        background = findViewById(R.id.background);
        monitoring = findViewById(R.id.surveillance);
        logOut = findViewById(R.id.log_out);
        viewData = findViewById(R.id.data);
        retrieveImageButton = findViewById(R.id.retrieve);
        retrieveImageView.setClipToOutline(true);
        background.setClipToOutline(true);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                value = dataSnapshot.getValue().toString();
                if (value == "true") {
                    monitoring.setBackground(getResources().getDrawable(R.drawable.button, getTheme()));
                    monitoring.setText("Switch Camera Off");
                    viewData.setBackground(getResources().getDrawable(R.drawable.button, getTheme()));
                    logOut.setBackground(getResources().getDrawable(R.drawable.button, getTheme()));
                    retrieveImageButton.setBackground(getResources().getDrawable(R.drawable.button, getTheme()));
                    Glide.with(getApplicationContext())
                            .load(R.drawable.camera_indicator)
                            .into(background);
                    retrieveImageView.setImageResource(R.drawable.place_holder);
                    STATUS = true;

                } else {
                    monitoring.setBackground(getResources().getDrawable(R.drawable.button_off, getTheme()));
                    monitoring.setText("Switch Camera On");
                    viewData.setBackground(getResources().getDrawable(R.drawable.button_off, getTheme()));
                    logOut.setBackground(getResources().getDrawable(R.drawable.button_off, getTheme()));
                    retrieveImageButton.setBackground(getResources().getDrawable(R.drawable.button_off, getTheme()));
                    Glide.with(getApplicationContext())
                            .load(R.drawable.camera_indicator)
                            .into(background);
                    STATUS = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

//                Toast.makeText(getApplicationContext(),"Please Connect to Internet", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void viewData(View view) {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Internet Connection is Required", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            return;
        }
        finish();
        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);
    }

    public void retrieveImage(View view) {

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Internet Connection is Required", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            return;
        }
        if (value == "true")
            retrieveImage.setValue("True");
        else {
            Glide.with(retrieveImageView.getContext())
                    .load(R.drawable.camera_offline_error)
                    .into(retrieveImageView);
            return;
        }

        listener = retrieveImage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Glide.with(retrieveImageView.getContext())
                        .load(dataSnapshot.getValue().toString())
//                        .placeholder(retrieveImageSource)
                        .thumbnail(Glide.with(retrieveImageView.getContext()).load(R.drawable.loading_data))
                        .into(retrieveImageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
