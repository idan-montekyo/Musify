package com.example.musicplayer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.fragments.DeleteDialogFragment;
import com.example.musicplayer.fragments.MainFragment;
import com.example.musicplayer.model.Song;
import com.example.musicplayer.model.SongAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DeleteDialogFragment.DeleteDialogListener {

    final String MAIN_FRAGMENT_TAG = "main_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Hide ActionBar.
        if (getSupportActionBar() != null) { getSupportActionBar().hide(); }

        MainFragment mainFragment = MainFragment.newInstance();
        getSupportFragmentManager().beginTransaction().
                add(R.id.root_main_activity, mainFragment, MAIN_FRAGMENT_TAG).commit();
    }

    @Override
    public void onPositiveButtonClicked() {}

    @Override
    public void onNegativeButtonClicked() {}
}