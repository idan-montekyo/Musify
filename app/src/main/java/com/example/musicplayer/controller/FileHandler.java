package com.example.musicplayer.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.musicplayer.model.Song;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

// Source - save ArrayList<Custom Object> to shared-preferences - https://www.youtube.com/watch?v=jcliHGR3CHo
// Source - save ArrayList<Custom Object> to local storage - https://stackoverflow.com/questions/29648630/save-arraylistcustom-object-to-local-storage
public class FileHandler {

    final static private String SP_TAG = "sp";
    final static private String SONG_LIST_TAG = "SONG_LIST";

    public static String getSpTag() { return SP_TAG; }

    // Needed for:
    //  1. After initializing songs for the first run (MainFragment)
    //  2. After deleting a song (MainActivity)
    //  3. After adding a new song (MainActivity)
    //  4. After rearranging the list's order (MainFragment)
    public static void SaveSongArrayList(Context context, ArrayList<Song> songList){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(songList);
        editor.putString(SONG_LIST_TAG, json);
        editor.apply();
    }

    // Needed for:
    //  1. when app starts running (MainFragment)
    public static ArrayList<Song> getSongArrayList(Context context){
        ArrayList<Song> mSongList;

        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SONG_LIST_TAG, null);
        Type listType = new TypeToken<ArrayList<Song>>() {}.getType();
        mSongList = gson.fromJson(json, listType);

        if (mSongList == null) {
            mSongList = new ArrayList<>();
        }

        return mSongList;
    }
}
