package com.example.securestreetparking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Collections;

public class DataActivity extends AppCompatActivity {

    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("data");

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);


        final ArrayList<DataClass> data = new ArrayList<>();
        final DataAdapter dataAdapter = new DataAdapter(this, 0, data);
        listView = findViewById(R.id.listview);
        listView.setAdapter(dataAdapter);

        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                data.add(dataClass);
                Collections.reverse(data);
                dataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void homeScreen(View view) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}