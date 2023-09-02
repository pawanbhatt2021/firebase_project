//package com.example.firebasedemo;
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
//import com.example.firebasedemo.databinding.ActivitySignupBinding;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.OnProgressListener;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.karumi.dexter.Dexter;
//import com.karumi.dexter.PermissionToken;
//import com.karumi.dexter.listener.PermissionDeniedResponse;
//import com.karumi.dexter.listener.PermissionGrantedResponse;
//import com.karumi.dexter.listener.PermissionRequest;
//import com.karumi.dexter.listener.single.PermissionListener;
//
//import java.io.InputStream;
//import java.util.Date;
//import java.util.Random;
//
//public class Signup extends AppCompatActivity {
//    ActivitySignupBinding binding;
//    Uri filepath;
//    Bitmap bitmap;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding=ActivitySignupBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        binding.browse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                permission();
//            }
//        });
//        binding.signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signup();
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode==1 && resultCode==RESULT_OK){
//            filepath=data.getData();
//
//            try {
//                InputStream inputStream=getContentResolver().openInputStream(filepath);
//                bitmap= BitmapFactory.decodeStream(inputStream);
//                binding.imageView.setImageBitmap(bitmap);
//            }
//            catch (Exception e){
//
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    private void permission() {
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
//                        Toast.makeText(Signup.this, "Permission Denied", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                        permissionToken.continuePermissionRequest();
//                    }
//                }).check();
//    }
//    private void signup() {
//
//        ProgressDialog progressDialog=new ProgressDialog(this);
//        progressDialog.setTitle("Image Uploading ");
//        progressDialog.show();
//
//        FirebaseStorage storage=FirebaseStorage.getInstance();
//        StorageReference reference=storage.getReference("image"+new Date());
//        reference.putFile(filepath)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                FirebaseDatabase db=FirebaseDatabase.getInstance();
//                                DatabaseReference root=db.getReference("users");
//                                Signupmodel signupmodel=new Signupmodel(binding.name.getText().toString(),
//                                        binding.contact.getText().toString(),
//                                        binding.course.getText().toString(),
//                                        uri.toString());
//
//                                root.child(binding.rollno.getText().toString())
//                                        .setValue(signupmodel);
//
//                                binding.rollno.setText("");
//                                binding.name.setText("");
//                                binding.contact.setText("");
//                                binding.course.setText("");
//                                binding.imageView.setImageDrawable(null);
//                                progressDialog.dismiss();
//                                Toast.makeText(Signup.this, "Signup Successfully", Toast.LENGTH_SHORT).show();
//                            }
//                        });
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
//                        Toast.makeText(Signup.this, "Singup Failed", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//}
