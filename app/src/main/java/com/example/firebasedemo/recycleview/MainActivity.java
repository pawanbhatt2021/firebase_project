package com.example.firebasedemo.recycleview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.firebasedemo.R;
import com.example.firebasedemo.databinding.ActivityMain2Binding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    ActivityMain2Binding binding;
    RecycleViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<RecycleViewModel> options=
                new FirebaseRecyclerOptions.Builder<RecycleViewModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("students"),RecycleViewModel.class)
                        .build();

        adapter=new RecycleViewAdapter(options);
        binding.recycleView.setAdapter(adapter);

        binding.mainAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Add.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem item=menu.findItem(R.id.searchview);

        android.widget.SearchView searchView=(android.widget.SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                processSearch(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void processSearch(String query) {
        FirebaseRecyclerOptions<RecycleViewModel> model=
                new FirebaseRecyclerOptions.Builder<RecycleViewModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("students").orderByChild("course").startAt(query).endAt(query+"uf8ff"), RecycleViewModel.class)
                        .build();

        adapter=new RecycleViewAdapter(model);
        adapter.startListening();
        binding.recycleView.setAdapter(adapter);
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
}
