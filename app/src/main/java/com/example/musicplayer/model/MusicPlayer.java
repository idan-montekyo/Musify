package com.example.musicplayer.model;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

// mp3 files saved to mailboxdrive.com. might be removed after 30 days, thus need to be re-uploaded.
public class MusicPlayer {

    private static MusicPlayer instance = null;
    private static MediaPlayer mediaPlayer = null;

    // Empty constructor.
    private MusicPlayer() {}

    // Make sure only one instance can be made for the entire program.
    public static MusicPlayer getInstance() {
        if (instance == null)
            instance = new MusicPlayer();
        return instance;
    }

    // Check if music is playing.
    public boolean isPlaying() { return mediaPlayer.isPlaying(); }

    // Start.
    public void start(Context context, String uri) throws IOException {
        if (instance != null) {
            // Stop and release current mediaPlayer.
            if (mediaPlayer != null) {
                instance.stop();
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setVolume(100, 100);

            mediaPlayer.setDataSource(uri);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.seekTo(0);
                    mediaPlayer.start();
                    System.out.println("start");
                    System.out.println(mediaPlayer.getDuration());
                }
            });
        }
    }

    // Pause.
    public void pause() {
        if (instance != null && mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    // Continue.
    public void proceed() {
        if (instance != null && mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    // Stop.
    public void stop() {
        if (instance != null && mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


}
