package com.example.musicplayer.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.activities.MainActivity;
import com.example.musicplayer.model.Song;
import com.example.musicplayer.model.SongAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class MainFragment extends Fragment implements DeleteDialogFragment.DeleteDialogListener{

    final String MAIN_FRAGMENT_TAG = "main_fragment";
    final String SONG_DISPLAY_FRAGMENT_TAG = "song_display_fragment";
    final String DELETE_DIALOG_FRAGMENT_TAG = "delete_dialog_fragment";

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int x = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = requireActivity().findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        List<Song> songs = new ArrayList<>();
        songs.add(new Song(R.drawable.musify_icon_round, "Circles", "Post Malone", "3:16"));
        songs.add(new Song(R.drawable.musify_icon_round, "Homicide", "Logic (feat. Eminem)", "4:05"));
        songs.add(new Song(R.drawable.musify_icon_round, "ISIS", "Joyner Lucas (ft Logic)", "3:56"));
        songs.add(new Song(R.drawable.musify_icon_round, "Blinding Lights", "Teddy Swims (The Weekend Cover)", "3:35"));
        songs.add(new Song(R.drawable.musify_icon_round, "Alien", "Dennis Lloyd", "2:17"));
        songs.add(new Song(R.drawable.musify_icon_round, "If You Want Love", "NF", "3:19"));
        songs.add(new Song(R.drawable.musify_icon_round, "Only", "NF, Sasha Sloan", "3:46"));
        songs.add(new Song(R.drawable.musify_icon_round, "Takin' Shots", "Post Malone", "3:15"));
        songs.add(new Song(R.drawable.musify_icon_round, "Psycho (Pt.2)", "Russ", "2:42"));
        songs.add(new Song(R.drawable.musify_icon_round, "לונדון", "אייל גולן", "3:14"));
        songs.add(new Song(R.drawable.musify_icon_round, "תרוצי אליו", "דולי ופן (עם רותם כהן ובית הבובות)", "3:27"));
        songs.add(new Song(R.drawable.musify_icon_round, "מדברים על אהבה", "טל ורסנו", "2:59"));
        songs.add(new Song(R.drawable.musify_icon_round, "רסיסים (קאבר)", "נאור כהן", "3:18"));
        songs.add(new Song(R.drawable.musify_icon_round, "צליל מיתר", "אייל גולן", "4:20"));

        SongAdapter songAdapter = new SongAdapter(songs);

        songAdapter.setListener(new SongAdapter.MySongListener() {
            @Override
            public void onSongClicked(int index, View view) {

                SongDisplayFragment songDisplayFragment = SongDisplayFragment.newInstance(songs.get(index));
                getParentFragmentManager().beginTransaction().
                        hide(getParentFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG)).
                        add(R.id.root_main_activity, songDisplayFragment, SONG_DISPLAY_FRAGMENT_TAG).
                        addToBackStack(null).commit();
            }

            @Override
            public void onSongLongClicked(int index, View view) {
//                songs.remove(index);
//                songAdapter.notifyItemRemoved(index);
            }
        });

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP|ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                Collections.swap(songs, fromPosition, toPosition);
                // TODO: fix bug - when first song is reordered - it jumps to the bottom.
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // TODO: dialog not communicating through MainFragment.
                DeleteDialogFragment myDialogFragment = new DeleteDialogFragment();
                myDialogFragment.show(getParentFragmentManager(), DELETE_DIALOG_FRAGMENT_TAG);

                String songName = songs.get(viewHolder.getAdapterPosition()).getSong();
                Toast.makeText(getContext(), songName + "\nhas been removed", Toast.LENGTH_SHORT).show();
                songs.remove(viewHolder.getAdapterPosition());
                songAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(songAdapter);

    }

    @Override
    public void onPositiveButtonClicked() {
        Toast.makeText(getContext(), "abc", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNegativeButtonClicked() {
        Toast.makeText(getContext(), "abc", Toast.LENGTH_SHORT).show();
    }
}