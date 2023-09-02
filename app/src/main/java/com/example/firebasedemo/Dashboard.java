package com.example.firebasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {
String email,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        email=getIntent().getStringExtra("email");
        uid=getIntent().getStringExtra("uid");

        Toast.makeText(this, "Email: "+email+"  uid:  "+uid, Toast.LENGTH_SHORT).show();
    }
}