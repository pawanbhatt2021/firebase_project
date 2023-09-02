package com.example.firebasedemo.multiplefileupload;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedemo.R;

import java.util.List;

public class MultipleUploadAdapter extends RecyclerView.Adapter<MultipleUploadAdapter.MultipleUploadViewHolder>{

    List<String> files,status;
    public MultipleUploadAdapter(List<String> files,List<String> status) {
        this.files=files;
        this.status=status;
    }

    @NonNull
    @Override
    public MultipleUploadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.multiple_upload_single_row,parent,false);
        return new MultipleUploadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultipleUploadViewHolder holder, int position) {
        String filename=files.get(position);
        if (filename.length()>15)
            filename=filename.substring(0,15)+"...";

        holder.fileName.setText(filename);

        String fileStatus=status.get(position);

        if (fileStatus.equals("Loading"))
            holder.pBar.setImageResource(R.drawable.progress_bar);
        else
            holder.pBar.setImageResource(R.drawable.done);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    class MultipleUploadViewHolder extends RecyclerView.ViewHolder {
        ImageView fileIcon,pBar;
        TextView fileName;
        public MultipleUploadViewHolder(@NonNull View itemView) {
            super(itemView);

            fileIcon=itemView.findViewById(R.id.fileIcon);
            pBar=itemView.findViewById(R.id.pBar);
            fileName=itemView.findViewById(R.id.fileName);
        }
    }
}
