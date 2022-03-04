package com.example.musicplayer.model;

import android.net.Uri;

public class Song {

    private int songResId;
    private String imgAsStringBase64;
    private String song;
    private String singer;
    private String duration;
    private String uri;

    // Constructor.
    public Song(int songResId, String imgAsStringBase64, String song, String singer, String duration, String uri) {
        this.songResId = songResId;
        this.imgAsStringBase64 = imgAsStringBase64;
        this.song = song;
        this.singer = singer;
        this.duration = duration;
        this.uri = uri;
    }

    // Getter methods.
    public int getSongResId() { return songResId; }
    public String getImgAsStringBase64() { return imgAsStringBase64; }
    public String getSong() { return song; }
    public String getSinger() { return singer; }
    public String getDuration() { return duration; }
    public String getUri() { return uri; }

    // Setter methods.
    public void setSongResId(int songResId) { this.songResId = songResId; }
    public void setImgAsStringBase64(String imgAsStringBase64) { this.imgAsStringBase64 = imgAsStringBase64; }
    public void setSong(String song) { this.song = song; }
    public void setSinger(String singer) { this.singer = singer; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setUri(String uri) { this.uri = uri; }
}
