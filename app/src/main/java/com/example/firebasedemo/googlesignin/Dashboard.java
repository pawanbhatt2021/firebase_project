package com.example.firebasedemo.googlesignin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.firebasedemo.databinding.ActivityDashboard2Binding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {
    ActivityDashboard2Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDashboard2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(this);
        binding.email.setText(account.getEmail());
        binding.name.setText(account.getDisplayName());
        Glide.with(this).load(account.getPhotoUrl()).into(binding.pimage);

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Dashboard.this, com.example.firebasedemo.googlesignin.GoogleSignIn.class));
                finish();
            }
        });
    }
}