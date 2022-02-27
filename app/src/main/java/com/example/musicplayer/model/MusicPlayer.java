package com.example.musicplayer.model;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

// TODO: make sure how to create music - save in R.raw(resid) ? from youtube(uri) [service?] ?
public class MusicPlayer {

    private final String EXAMPLE_SONG_URI = "https://www.youtube.com/watch?v=0sca9FP6zl8";

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

    // TODO: replace Object with resid / uri.
    //  remove EXAMPLE.
    // Start.
    public void start(Context context, Object obj) {
        if (instance != null) {
            if (mediaPlayer != null) { // Stop and release current mediaPlayer.
                instance.stop();
            }
            mediaPlayer = MediaPlayer.create(context, Uri.parse(EXAMPLE_SONG_URI));
            mediaPlayer.setLooping(false);
            mediaPlayer.setVolume(100, 100);
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
