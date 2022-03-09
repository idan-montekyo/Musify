package com.example.musicplayer.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.model.MusicPlayer;
import com.example.musicplayer.model.Song;


public class SongDisplayFragment extends Fragment {

    private static final String ARG_SONG_RES_ID = "song_res_id";
    private static final String ARG_IMG_URI = "img_uri";
    private static final String ARG_SONG = "song";
    private static final String ARG_SINGER = "singer";
    private static final String ARG_DURATION = "duration";
    private static final String ARG_SONG_URI = "uri";

    MusicPlayer musicPlayer = MusicPlayer.getInstance();

    ImageView imageIv, exitIv;
    TextView songTv, singerTv;
    CheckBox playPauseCheckBox;

    private int mSongResId;
    private String mImgUri,mSong, mSinger, mDuration, mSongUri;

    public static SongDisplayFragment newInstance(Song song) {

        SongDisplayFragment fragment = new SongDisplayFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_SONG_RES_ID, song.getSongResId());
        args.putString(ARG_IMG_URI, song.getImgUri());
        args.putString(ARG_SONG, song.getSong());
        args.putString(ARG_SINGER, song.getSinger());
        args.putString(ARG_DURATION, song.getDuration());
        args.putString(ARG_SONG_URI, song.getSongUri());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSongResId = getArguments().getInt(ARG_SONG_RES_ID);
            mImgUri = getArguments().getString(ARG_IMG_URI);
            mSong = getArguments().getString(ARG_SONG);
            mSinger = getArguments().getString(ARG_SINGER);
            mDuration = getArguments().getString(ARG_DURATION);
            mSongUri = getArguments().getString(ARG_SONG_URI);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song_display, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageIv = view.findViewById(R.id.imageview_display_image);

        // String empty = no picture was manually taken -> load songResId.
        // else = picture was taken -> load img-Uri.
        if (mImgUri.equals("")) {
            imageIv.setImageResource(mSongResId);
        } else {
            Glide.with(this).load(mImgUri).into(imageIv);
        }

        songTv = view.findViewById(R.id.textview_display_song);
        songTv.setText(mSong);

        singerTv = view.findViewById(R.id.textview_display_singer);
        singerTv.setText(mSinger);

        exitIv = view.findViewById(R.id.imageview_display_exit);
        exitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        playPauseCheckBox = view.findViewById(R.id.imageview_display_play_pause);
        playPauseCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playPauseCheckBox.isChecked()) {
                    musicPlayer.proceed();
                } else {
                    musicPlayer.pause();
                }
            }
        });
    }
}