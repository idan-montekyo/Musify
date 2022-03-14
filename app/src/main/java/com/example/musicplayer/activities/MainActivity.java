package com.example.musicplayer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.controller.FileHandler;
import com.example.musicplayer.fragments.AddSongFragment;
import com.example.musicplayer.fragments.DeleteDialogFragment;
import com.example.musicplayer.fragments.MainFragment;
import com.example.musicplayer.model.Song;

public class MainActivity extends AppCompatActivity implements DeleteDialogFragment.DeleteDialogListener,
        AddSongFragment.AddSongListener {

    final String MAIN_FRAGMENT_TAG = "main_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Hide ActionBar.
        if (getSupportActionBar() != null) { getSupportActionBar().hide(); }

        // Add Main-Fragment.
        getSupportFragmentManager().beginTransaction().
                add(R.id.root_main_activity, new MainFragment(), MAIN_FRAGMENT_TAG).commit();
    }

    @Override
    public void onPositiveButtonClicked(int position) {
        String songName = MainFragment.songs.get(position).getSong();
        Toast.makeText(MainActivity.this, songName + getResources().getString(R.string._removed), Toast.LENGTH_SHORT).show();
        MainFragment.songs.remove(position);
        MainFragment.songAdapter.notifyItemRemoved(position);
        FileHandler.SaveSongArrayList(getApplicationContext(), MainFragment.songs);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onNegativeButtonClicked() {
        MainFragment.songAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onAddButtonClicked(Song song) {
        String songName = song.getSong();
        Toast.makeText(this, songName + getResources().getString(R.string._added), Toast.LENGTH_SHORT).show();
        MainFragment.songs.add(0, song);
        MainFragment.songAdapter.notifyDataSetChanged();
        FileHandler.SaveSongArrayList(getApplicationContext(), MainFragment.songs);
    }
}