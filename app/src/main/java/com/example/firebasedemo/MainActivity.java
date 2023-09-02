//package com.example.firebasedemo;
//
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.Manifest;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//
//import com.example.firebasedemo.databinding.ActivityMainBinding;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.karumi.dexter.Dexter;
//import com.karumi.dexter.PermissionToken;
//import com.karumi.dexter.listener.PermissionDeniedResponse;
//import com.karumi.dexter.listener.PermissionGrantedResponse;
//import com.karumi.dexter.listener.PermissionRequest;
//import com.karumi.dexter.listener.single.PermissionListener;
//
//import java.io.InputStream;
//
//public class MainActivity extends AppCompatActivity {
//    ActivityMainBinding binding;
//    Uri filepath;
//    Bitmap bitmap;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        binding.browse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imageBrowse();
//            }
//        });
//        binding.upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imageUpload();
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode==1 && resultCode==RESULT_OK){
//            filepath=data.getData();
//            try {
//                InputStream inputStream=getContentResolver().openInputStream(filepath);
//                bitmap=BitmapFactory.decodeStream(inputStream);
//                binding.image.setImageBitmap(bitmap);
//            }
//            catch (Exception e){
//                Toast.makeText(this, "Unable to upload", Toast.LENGTH_SHORT).show();
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    private void imageBrowse() {
//        Dexter.withContext(this)
//                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                        Intent intent=new Intent(Intent.ACTION_PICK);
//                        intent.setType("image/*");
//                        startActivityForResult(Intent.createChooser(intent,"Please Select Image"),1);
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                        permissionToken.continuePermissionRequest();
//                    }
//                }).check();
//    }
//    private void imageUpload() {
//        ProgressDialog progressDialog=new ProgressDialog(this);
//        progressDialog.setTitle("Image Uploading");
//        progressDialog.show();
//        FirebaseStorage storage=FirebaseStorage.getInstance();
//        StorageReference reference=storage.getReference().child("image");
//        reference.putFile(filepath)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        progressDialog.dismiss();
//                        binding.image.setImageDrawable(null);
//                        Toast.makeText(MainActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                        long percentage=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
//                        progressDialog.setMessage("Uploading: "+percentage+"%");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progressDialog.dismiss();
//                        Toast.makeText(MainActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//}