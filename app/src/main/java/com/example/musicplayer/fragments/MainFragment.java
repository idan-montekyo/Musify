package com.example.musicplayer.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;

import com.example.musicplayer.R;
import com.example.musicplayer.controller.FileHandler;
import com.example.musicplayer.model.Song;
import com.example.musicplayer.model.SongAdapter;
import com.example.musicplayer.services.MusicPlayerService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

// TODO:
//  2. Replace deprecated set/getTargetFragment in AddSongFragment & HelpFragment - https://stackoverflow.com/questions/64869501/how-to-replace-settargetfragment-now-that-it-is-deprecated
//  3. Make DeleteDialogFragment communicate directly to MainFragment instead of MainActivity.
public class MainFragment extends Fragment {

    public static ArrayList<Song> songs;
    public static SongAdapter songAdapter;

    final String MAIN_FRAGMENT_TAG = "main_fragment";
    final String SONG_DISPLAY_FRAGMENT_TAG = "song_display_fragment";
    final String ADD_SONG_FRAGMENT_TAG = "add_song_fragment";
    final String DELETE_DIALOG_FRAGMENT_TAG = "delete_dialog_fragment";

    SharedPreferences sharedPreferences;

    MusicPlayerService musicPlayerService;
    Button playAllBtn, addSongBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = Objects.requireNonNull(getContext()).
                getSharedPreferences(FileHandler.getSpTag(), Context.MODE_PRIVATE);

        musicPlayerService = new MusicPlayerService();

        RecyclerView recyclerView = requireActivity().findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        songs = FileHandler.getSongArrayList(Objects.requireNonNull(getContext()));
        // Source - Check if the app is being launched for the first time - https://stackoverflow.com/questions/4636141/determine-if-android-app-is-being-used-for-the-first-time
        if (sharedPreferences.getBoolean("is_first_run", true)) {
            addInitialSongs();
            FileHandler.SaveSongArrayList(getContext(), songs);
            sharedPreferences.edit().putBoolean("is_first_run", false).commit();
        }

        songAdapter = new SongAdapter(songs);

        songAdapter.setListener(new SongAdapter.MySongListener() {
            @Override
            public void onSongClicked(int index, View view) {

                SongDisplayFragment songDisplayFragment = SongDisplayFragment.newInstance(songs.get(index));
                getParentFragmentManager().beginTransaction().
                        hide(getParentFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG)).
                        add(R.id.root_main_activity, songDisplayFragment, SONG_DISPLAY_FRAGMENT_TAG).
                        addToBackStack(null).commit();

                // Launch MusicPlayerService.
                Intent musicPlayerServiceIntent = new Intent(getContext(), MusicPlayerService.class);
                musicPlayerServiceIntent.putExtra(MusicPlayerService.INDEX_TAG, index);
                musicPlayerServiceIntent.putExtra(MusicPlayerService.CASE_TAG, MusicPlayerService.LAUNCH_TAG);
                Objects.requireNonNull(getActivity()).startService(musicPlayerServiceIntent);
            }

            @Override
            public void onSongLongClicked(int index, View view) {
                // Might delete method.
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
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
                FileHandler.SaveSongArrayList(Objects.requireNonNull(getContext()), songs);

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                // TODO: dialog not communicating through MainFragment - do like in HelpFrag in AddSongFrag.
                DeleteDialogFragment myDialogFragment = DeleteDialogFragment.newInstance(viewHolder.getAdapterPosition());
                myDialogFragment.show(getParentFragmentManager(), DELETE_DIALOG_FRAGMENT_TAG);

                // moved to main activity
//                String songName = songs.get(viewHolder.getAdapterPosition()).getSong();
//                Toast.makeText(getContext(), "Removed " + songName, Toast.LENGTH_SHORT).show();
//                songs.remove(viewHolder.getAdapterPosition());
//                songAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

//                // in case nothing was deleted
//                songAdapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(songAdapter);

        playAllBtn = view.findViewById(R.id.button_play_all);
        playAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch MusicPlayerService.
                Intent musicPlayerServiceIntent = new Intent(getContext(), MusicPlayerService.class);
                musicPlayerServiceIntent.putExtra(MusicPlayerService.INDEX_TAG, 0);
                musicPlayerServiceIntent.putExtra(MusicPlayerService.CASE_TAG, MusicPlayerService.LAUNCH_TAG);
                Objects.requireNonNull(getActivity()).startService(musicPlayerServiceIntent);
            }
        });

        addSongBtn = view.findViewById(R.id.button_main_add_song);
        addSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().
                        hide(getParentFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG)).
                        add(R.id.root_main_activity, new AddSongFragment(), ADD_SONG_FRAGMENT_TAG).
                        addToBackStack(null).commit();
            }
        });


    }

    private void addInitialSongs() {
        songs.add(new Song(R.drawable.img_circles, "", "Circles", "Post Malone", "3:36",
                "https://www.mboxdrive.com/circles.mp3"));
        songs.add(new Song(R.drawable.img_homicide, "", "Homicide", "Logic (feat. Eminem)", "4:05",
                "https://www.mboxdrive.com/homicide.mp3"));
        songs.add(new Song(R.drawable.img_isis, "", "ISIS", "Joyner Lucas (ft Logic)", "3:56",
                "https://www.mboxdrive.com/isis.mp3"));
        songs.add(new Song(R.drawable.img_blinding_lights, "", "Blinding Lights", "Teddy Swims (The Weekend Cover)",
                "3:35", "https://www.mboxdrive.com/blinding_lights.mp3"));
        songs.add(new Song(R.drawable.img_alien, "", "Alien", "Dennis Lloyd", "2:17",
                "https://www.mboxdrive.com/alien.mp3"));
        songs.add(new Song(R.drawable.img_if_you_want_love, "", "If You Want Love", "NF", "3:19",
                "https://www.mboxdrive.com/if_you_want_love.mp3"));
        songs.add(new Song(R.drawable.img_only, "", "Only", "NF, Sasha Sloan", "3:46",
                "https://www.mboxdrive.com/only.mp3"));
        songs.add(new Song(R.drawable.img_takin_shots, "", "Takin' Shots", "Post Malone", "3:36",
                "https://www.mboxdrive.com/takin_shots.mp3"));
        songs.add(new Song(R.drawable.img_psycho, "", "Psycho (Pt.2)", "Russ", "2:42",
                "https://www.mboxdrive.com/psycho.mp3"));
        songs.add(new Song(R.drawable.img_london, "", "לונדון", "אייל גולן", "3:14",
                "https://www.mboxdrive.com/london.mp3"));
        songs.add(new Song(R.drawable.img_taruzi_elav, "", "תרוצי אליו", "דולי ופן (עם רותם כהן ובית הבובות)",
                "3:27", "https://www.mboxdrive.com/taruzi_elav.mp3"));
        songs.add(new Song(R.drawable.img_medabrim_al_ahava, "", "מדברים על אהבה", "טל ורסנו", "2:59",
                "https://www.mboxdrive.com/medabrim_al_ahava.mp3"));
        songs.add(new Song(R.drawable.img_resisim, "", "רסיסים (קאבר)", "נאור כהן", "3:18",
                "https://www.mboxdrive.com/resisim.mp3"));
        songs.add(new Song(R.drawable.img_tslil_meitar, "", "צליל מיתר", "אייל גולן", "4:20",
                "https://www.mboxdrive.com/tslil_meitar.mp3"));
    }
}