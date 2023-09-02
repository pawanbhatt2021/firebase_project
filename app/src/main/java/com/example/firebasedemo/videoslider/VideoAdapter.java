package com.example.firebasedemo.videoslider;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedemo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class VideoAdapter extends FirebaseRecyclerAdapter<VideoModel,VideoAdapter.VideoHolder> {

    public VideoAdapter(@NonNull FirebaseRecyclerOptions<VideoModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull VideoHolder holder, int position, @NonNull VideoModel model) {
        holder.setData(model);
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_video,parent,false);
        return new VideoHolder(view);
    }

    class VideoHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        TextView title,desc;
        ProgressBar progressBar;
        public VideoHolder(@NonNull View itemView) {
            super(itemView);

            videoView=itemView.findViewById(R.id.videoView);
            title=itemView.findViewById(R.id.videoTitle);
            desc=itemView.findViewById(R.id.videoDesc);
            progressBar=itemView.findViewById(R.id.videoProgressBar);
        }
        void setData(VideoModel obj){
            videoView.setVideoPath(obj.getUrl());
            title.setText(obj.getTitle());
            desc.setText(obj.getDesc());

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    progressBar.setVisibility(View.GONE);
                    mp.start();
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });
        }
    }
}
