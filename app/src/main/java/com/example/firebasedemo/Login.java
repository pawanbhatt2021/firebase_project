package com.example.firebasedemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auth=FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword("pawanbhatt8755@gmail.com","pawan@123")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent=new Intent(Login.this,Dashboard.class);
                            intent.putExtra("email",auth.getCurrentUser().getEmail());
                            intent.putExtra("uid",auth.getCurrentUser().getUid());
                            Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(Login.this, "Wrong Credential", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
