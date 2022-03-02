package com.example.musicplayer.model;

import android.net.Uri;

// TODO: add uri (as String?)
public class Song {

    private int songResId;
    private String song;
    private String singer;
    private String duration;
    private String uri;

    // Constructor.
    public Song(int songResId, String song, String singer, String duration, String uri) {
        this.songResId = songResId;
        this.song = song;
        this.singer = singer;
        this.duration = duration;
        this.uri = uri;
    }

    // Getter methods.
    public int getSongResId() { return songResId; }
    public String getSong() { return song; }
    public String getSinger() { return singer; }
    public String getDuration() { return duration; }
    public String getUri() { return uri; }

    // Setter methods.
    public void setSongResId(int songResId) { this.songResId = songResId; }
    public void setSong(String song) { this.song = song; }
    public void setSinger(String singer) { this.singer = singer; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setUri(String uri) { this.uri = uri; }
}
