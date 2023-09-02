package com.example.firebasedemo.pdfreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.firebasedemo.databinding.ActivityPdfReaderBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class PdfReader extends AppCompatActivity {
    ActivityPdfReaderBinding binding;
    PdfAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPdfReaderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PdfReader.this,UploadFile.class));
            }
        });

        binding.pdfRecycleView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<FileInfoModel> options=
                new FirebaseRecyclerOptions.Builder<FileInfoModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("mydocuments"), FileInfoModel.class)
                        .build();
        adapter=new PdfAdapter(options);
        binding.pdfRecycleView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }
}