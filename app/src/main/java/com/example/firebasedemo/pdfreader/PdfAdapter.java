package com.example.firebasedemo.pdfreader;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedemo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class PdfAdapter extends FirebaseRecyclerAdapter<FileInfoModel,PdfAdapter.PdfViewHolder> {

    public PdfAdapter(@NonNull FirebaseRecyclerOptions<FileInfoModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PdfViewHolder holder, int position, @NonNull FileInfoModel model) {
        holder.pdfHeader.setText(model.getFilename());
        holder.pdfBookViews.setText(String.valueOf(model.getNov()));
        holder.pdfLikeCount.setText(String.valueOf(model.getNol()));
        holder.pdfDislikeCount.setText(String.valueOf(model.getNod()));

        holder.pdfImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.pdfBook.getContext(),ViewPdf.class);
                intent.putExtra("filename",model.getFilename());
                intent.putExtra("fileurl",model.getFileurl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.pdfImg.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_single_row,parent,false);
        return new PdfViewHolder(view);
    }

    class PdfViewHolder extends RecyclerView.ViewHolder {
        ImageView pdfImg,pdfBook,pdfLike,pdfDislike;
        TextView pdfHeader,pdfBookViews,pdfLikeCount,pdfDislikeCount;
        public PdfViewHolder(@NonNull View itemView) {
            super(itemView);

            pdfImg=itemView.findViewById(R.id.pdfImg);
            pdfBook=itemView.findViewById(R.id.pdfBook);
            pdfLike=itemView.findViewById(R.id.pdfLike);
            pdfDislike=itemView.findViewById(R.id.pdfDislike);


            pdfHeader=itemView.findViewById(R.id.pdfHeader);
            pdfBookViews=itemView.findViewById(R.id.pdfBookViews);
            pdfLikeCount=itemView.findViewById(R.id.pdfLikeCount);
            pdfDislikeCount=itemView.findViewById(R.id.pdfDislikeCount);


        }
    }
}
