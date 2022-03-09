package com.example.musicplayer.model;

public class Song {

    private int songResId;
    private String imgUri;
    private String song;
    private String singer;
    private String duration;
    private String songUri;

    // Constructor.
    public Song(int songResId, String imgUri, String song, String singer, String duration, String songUri) {
        this.songResId = songResId;
        this.imgUri = imgUri;
        this.song = song;
        this.singer = singer;
        this.duration = duration;
        this.songUri = songUri;
    }

    // Getter methods.
    public int getSongResId() { return songResId; }
    public String getImgUri() { return imgUri; }
    public String getSong() { return song; }
    public String getSinger() { return singer; }
    public String getDuration() { return duration; }
    public String getSongUri() { return songUri; }

    // Setter methods.
    public void setSongResId(int songResId) { this.songResId = songResId; }
    public void setImgUri(String imgUri) { this.imgUri = imgUri; }
    public void setSong(String song) { this.song = song; }
    public void setSinger(String singer) { this.singer = singer; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setSongUri(String songUri) { this.songUri = songUri; }
}
