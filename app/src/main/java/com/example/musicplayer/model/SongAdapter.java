package com.example.musicplayer.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicplayer.R;

import java.io.IOException;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder>{

    private List<Song> songs;
    private MySongListener listener;

    // Interface for listeners.
    public interface MySongListener {
        void onSongClicked(int index, View view) throws IOException;
        void onSongLongClicked(int index, View view);
    }

    public void setListener(MySongListener listener) { this.listener = listener; }

    // Constructor.
    public SongAdapter(List<Song> songs) { this.songs = songs; }

    public class SongViewHolder extends RecyclerView.ViewHolder {

        ImageView imageIv;
        TextView songTv;
        TextView singerTv;
        TextView durationTv;

        // Constructor.
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            imageIv = itemView.findViewById(R.id.card_image);
            songTv = itemView.findViewById(R.id.card_song);
            singerTv = itemView.findViewById(R.id.card_singer);
            durationTv = itemView.findViewById(R.id.card_duration);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        try {
                            listener.onSongClicked(getAdapterPosition(), v);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        listener.onSongLongClicked(getAdapterPosition(), v);
                    }
                    return false;
                }
            });
        }
    }

    // Adds the first cards to fill up the screen.
    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_card, parent, false);
        SongViewHolder songViewHolder = new SongViewHolder(view);
        return songViewHolder;
    }

    // Loading data into cards + Recycles card when scrolling.
    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.songTv.setText(song.getSong());
        holder.singerTv.setText(song.getSinger());
        holder.durationTv.setText(song.getDuration());

        // String empty = no picture was manually taken -> load songResId
        // else = picture was taken -> load from img-URI.
        if (songs.get(position).getImgUri().equals("")) {
            Glide.with(holder.imageIv.getContext()).load(song.getSongResId()).
                    apply(RequestOptions.circleCropTransform()).into(holder.imageIv);
        } else {
            Glide.with(holder.imageIv.getContext()).
                    load(song.getImgUri()).circleCrop().into(holder.imageIv);
        }
    }

    // Returns number of songs in our list.
    @Override
    public int getItemCount() { return songs.size(); }
}
