package com.example.firebasedemo.recycleview;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebasedemo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleViewAdapter extends FirebaseRecyclerAdapter<RecycleViewModel, RecycleViewAdapter.MyHolder> {

    public RecycleViewAdapter(@NonNull FirebaseRecyclerOptions<RecycleViewModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull final RecycleViewModel model) {
        holder.name.setText(model.getName());
        holder.course.setText(model.getCourse());
        holder.email.setText(model.getEmail());
        Log.d("PURL", model.getPurl());
//        Glide.with(holder.imageView1.getContext()).load(model.getPurl()).into(holder.imageView1);
        Glide.with(holder.imageView1.getContext()).load(model.getPurl()).into(holder.imageView1);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialogPlus = DialogPlus.newDialog(holder.imageView1.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialog_content))
                        .setExpanded(true, 1100)
                        .create();

                View view = dialogPlus.getHolderView();
                EditText purl = view.findViewById(R.id.dialogpurl);
                EditText name = view.findViewById(R.id.dialogname);
                EditText course = view.findViewById(R.id.dialogcourse);
                EditText email = view.findViewById(R.id.dialogemail);
                Button update = view.findViewById(R.id.dialogupdate);

                purl.setText(model.getPurl());
                name.setText(model.getName());
                course.setText(model.getCourse());
                email.setText(model.getEmail());

                dialogPlus.show();

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("purl", purl.getText().toString());
                        map.put("name", name.getText().toString());
                        map.put("course", course.getText().toString());
                        map.put("eamil", email.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("students")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });


            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(holder.imageView1.getContext(),"Delete" , Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.imageView1.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Delete...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference("students").child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, parent, false);
        return new MyHolder(view);
    }

    class MyHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView1;
        TextView name, course, email;
        ImageView edit, delete;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imageView1 = itemView.findViewById(R.id.imageCircle);
            name = itemView.findViewById(R.id.name);
            course = itemView.findViewById(R.id.course);
            email = itemView.findViewById(R.id.email);

            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);

        }
    }
}
