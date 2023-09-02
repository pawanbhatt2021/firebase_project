package com.example.firebasedemo.phoneauth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.firebasedemo.databinding.ActivityPhoneAuthBinding;
import com.hbb20.CountryCodePicker;

public class PhoneAuth extends AppCompatActivity {
    ActivityPhoneAuthBinding binding;
    CountryCodePicker ccp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPhoneAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ccp=binding.ccp;
        ccp.registerCarrierNumberEditText(binding.PersonName);

        binding.sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PhoneAuth.this,ManageOtp.class);
                intent.putExtra("mobile",ccp.getFullNumberWithPlus().replace(" ",""));
                startActivity(intent);
            }
        });
    }
}