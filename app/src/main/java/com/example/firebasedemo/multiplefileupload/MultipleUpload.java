package com.example.firebasedemo.multiplefileupload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;

import com.example.firebasedemo.databinding.ActivityMultipleUploadBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class MultipleUpload extends AppCompatActivity {
    ActivityMultipleUploadBinding binding;
    StorageReference reference;
    List<String> files,status;
    MultipleUploadAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMultipleUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        files=new ArrayList<>();
        status=new ArrayList<>();
        reference= FirebaseStorage.getInstance().getReference();
        binding.recylcleUpload.setLayoutManager(new LinearLayoutManager(this));

        adapter=new MultipleUploadAdapter(files,status);
        binding.recylcleUpload.setAdapter(adapter);

        binding.uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(MultipleUpload.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent();
                                intent.setType("image/*");
                                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Select Multiple File"),101);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101 && resultCode==RESULT_OK){
         if (data.getClipData()!=null){
             for (int i=0;i<data.getClipData().getItemCount();i++){ 
                 Uri fileUri=data.getClipData().getItemAt(i).getUri();
                 String fileName=getFileNameFromUrl(fileUri);
                 files.add(fileName);
                 status.add("Loading");
                 adapter.notifyDataSetChanged();

                 final int index=i;
                 StorageReference uploader=reference.child("/multiuploads").child(fileName);
                 uploader.putFile(fileUri)
                         .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                             @Override
                             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                 status.remove(index);
                                 status.add(index,"Done");
                                 adapter.notifyDataSetChanged();
                             }
                         })
                         .addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {

                             }
                         });
             }
         }
        }
    }

    @SuppressLint("Range")
    private String getFileNameFromUrl(Uri fileUri) {
        String result=null;
        if (fileUri.getScheme().equals("content")){
            Cursor cursor=getContentResolver().query(fileUri,null,null,null,null);
            try {
                if (cursor!=null && cursor.moveToFirst()){
                    result=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
            finally {
                cursor.close();
            }
        }
        if (result==null){
            result=fileUri.getPath();
            int cut=result.lastIndexOf("/");
            if (cut!=-1){
                result=result.substring(cut+1);
            }
        }
        return result;
    }
}