package com.example.firebasedemo.pdfreader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.firebasedemo.databinding.ActivityUploadFileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class UploadFile extends AppCompatActivity {
    ActivityUploadFileBinding binding;
    Uri filepath;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadFileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("mydocuments");

        binding.pdfFile.setVisibility(View.INVISIBLE);
        binding.pdfCancel.setVisibility(View.INVISIBLE);

        binding.pdfCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.pdfFile.setVisibility(View.INVISIBLE);
                binding.pdfCancel.setVisibility(View.INVISIBLE);
                binding.pdfBrowse.setVisibility(View.VISIBLE);
            }
        });

        binding.pdfBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(UploadFile.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select PDF File"), 101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        binding.pdfUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processUpload();
            }
        });
    }

    private void processUpload() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File Uploading...!");
        progressDialog.show();
        final StorageReference reference = storageReference.child("uploads/" + System.currentTimeMillis() + ".pdf");
        reference.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                FileInfoModel fileInfoModel = new FileInfoModel(binding.pdfTitle.getText().toString(), uri.toString(),120,500,1000);
                                databaseReference.child(databaseReference.push().getKey()).setValue(fileInfoModel);
                                progressDialog.dismiss();
                                Toast.makeText(UploadFile.this, "File Uploaded", Toast.LENGTH_LONG).show();
                                binding.pdfFile.setVisibility(View.INVISIBLE);
                                binding.pdfBrowse.setVisibility(View.VISIBLE);
                                binding.pdfCancel.setVisibility(View.INVISIBLE);
                                binding.pdfTitle.setText("");

                            }
                        });

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        long percent=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded : "+percent+"%");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadFile.this, "Uploading Failed...?", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            filepath = data.getData();
            binding.pdfFile.setVisibility(View.VISIBLE);
            binding.pdfCancel.setVisibility(View.VISIBLE);
            binding.pdfBrowse.setVisibility(View.INVISIBLE);
        }
    }

}