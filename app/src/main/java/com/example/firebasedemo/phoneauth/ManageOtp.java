package com.example.firebasedemo.phoneauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.firebasedemo.databinding.ActivityManageOtpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ManageOtp extends AppCompatActivity {

    ActivityManageOtpBinding binding;
    String phno;
    FirebaseAuth auth;
    String otpId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityManageOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        phno=getIntent().getStringExtra("mobile");
        Toast.makeText(this, phno, Toast.LENGTH_SHORT).show();
        auth=FirebaseAuth.getInstance();
        
        initiateOtp();

        binding.verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otpId,binding.verifyotp.getText().toString());
                signInWithPhoneAuthCredential(credential);
            }
        });
    }

    private void initiateOtp() {
//        PhoneAuthOptions options=PhoneAuthOptions.newBuilder(auth)
//                .setPhoneNumber(phno)
//                .setTimeout(60L, TimeUnit.SECONDS)
//                .setActivity(this)
//                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                    @Override
//                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                        otpId=s;
//                    }
//                    @Override
//                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                        signInWithPhoneAuthCredential(phoneAuthCredential);
//                    }
//
//                    @Override
//                    public void onVerificationFailed(@NonNull FirebaseException e) {
//                        Toast.makeText(ManageOtp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }).build();
//
//        PhoneAuthProvider.verifyPhoneNumber(options);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phno,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        otpId=s;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(ManageOtp.this, e.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        auth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(ManageOtp.this,Dashboard.class));
                        }
                        else {
                            Toast.makeText(ManageOtp.this, "Error in singing", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}