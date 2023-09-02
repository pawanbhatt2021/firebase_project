package com.example.firebasedemo.recycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.firebasedemo.databinding.ActivityAddBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Add extends AppCompatActivity {
    ActivityAddBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Add.this,MainActivity.class));
                finish();
            }
        });
        binding.addDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processInset();
            }
        });
    }

    private void processInset() {
        Map<String,Object> map=new HashMap<>();
        map.put("name",binding.addName.getText().toString());
        map.put("course",binding.addCourse.getText().toString());
        map.put("email",binding.addEmail.getText().toString());
        map.put("purl",binding.addPurl.getText().toString());

        FirebaseDatabase.getInstance().getReference("students").push()
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        binding.addName.setText("");
                        binding.addCourse.setText("");
                        binding.addEmail.setText("");
                        binding.addPurl.setText("");
                        Toast.makeText(Add.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Add.this, "Failed Data Adding", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}