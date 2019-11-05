package com.example.securestreetparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DataActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);


        ArrayList<DataClass> data = new ArrayList<>();
        data.add(new DataClass("DATE","TIME","URL"));
        data.add(new DataClass("DATE2","TIME2","URL2"));
        data.add(new DataClass("DATE3","TIME3","URL3"));


        DataAdapter dataAdapter = new DataAdapter(this,0,data);
        listView = findViewById(R.id.listview);
        listView.setAdapter(dataAdapter);


    }

    public void homeScreen(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}