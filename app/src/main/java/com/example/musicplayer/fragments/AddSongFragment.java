package com.example.musicplayer.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.model.Song;


public class AddSongFragment extends Fragment implements HelpFragment.OnSongSuggestionSelected {

    final String ADD_SONG_FRAGMENT_TAG = "add_song_fragment";
    final String HELP_FRAGMENT_TAG = "help_fragment";

    EditText songEt, singerEt, minutesEt, secondsEt, urlLinkEt;
    Button takePictureBtn, addSongBtn, needHelpBtn;
    ImageView exitIv;

    private Song newSong;
    private String song, singer, minutes, seconds, urlLink, duration;

    public interface AddSongListener {
        void onAddButtonClicked(Song song);
    }

    AddSongListener callBack;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            callBack = (AddSongListener)context;
        } catch (ClassCastException ex) {
            throw new ClassCastException("Activity/Fragment must implement AddSongListener interface.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_song, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        songEt = view.findViewById(R.id.edittext_add_song);
        singerEt = view.findViewById(R.id.edittext_add_singer);
        minutesEt = view.findViewById(R.id.edittext_add_minutes);
        secondsEt = view.findViewById(R.id.edittext_add_seconds);
        urlLinkEt = view.findViewById(R.id.edittext_add_link);

        exitIv = view.findViewById(R.id.imageview_add_exit);
        exitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        takePictureBtn = view.findViewById(R.id.button_add_picture);
        takePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add methodology.
            }
        });

        addSongBtn = view.findViewById(R.id.button_add_song);
        addSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                song = songEt.getText().toString();
                singer = singerEt.getText().toString();
                minutes = minutesEt.getText().toString();
                seconds = secondsEt.getText().toString();
                urlLink = urlLinkEt.getText().toString();

                if (song.equals("")) {
                    Toast.makeText(getContext(), "Please fill in song name", Toast.LENGTH_SHORT).show();
                } else if (singer.equals("")) {
                    Toast.makeText(getContext(), "Please fill in singer name", Toast.LENGTH_SHORT).show();
                } else if (minutes.equals("") || seconds.equals("")) {
                    Toast.makeText(getContext(), "Please fill in duration", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(seconds) > 59) {
                    Toast.makeText(getContext(), "Seconds field may be 0-59", Toast.LENGTH_SHORT).show();
                } else if (urlLink.equals("")) {
                    Toast.makeText(getContext(), "Please add Url Link to a song from YouTube", Toast.LENGTH_SHORT).show();
                } else {
                    if (Integer.parseInt(seconds) >= 10) {
                        duration = minutes + ":" + seconds;
                    } else {
                        duration = minutes + ":0" + seconds;
                    }
                    newSong = new Song(R.drawable.musify_icon_round, song, singer, duration, urlLink);
                    getParentFragmentManager().popBackStack();
                    callBack.onAddButtonClicked(newSong);
                }
            }
        });

        needHelpBtn = view.findViewById(R.id.button_add_need_help);
        needHelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpFragment helpFragment = new HelpFragment();
                helpFragment.setTargetFragment(AddSongFragment.this, 1);
                getParentFragmentManager().beginTransaction().
                        hide(getParentFragmentManager().findFragmentByTag(ADD_SONG_FRAGMENT_TAG)).
                        add(R.id.root_main_activity, helpFragment, HELP_FRAGMENT_TAG).
                        addToBackStack(null).commit();
            }
        });
    }

    // Implementation of interface HelpFragment.OnSongSuggestionSelected
    @Override
    public void sendSelection(int selection) {

        switch (selection) {
            case 1:
                songEt.setText("Human");
                singerEt.setText("Rag'n'Bone Man");
                minutesEt.setText("3");
                secondsEt.setText("17");
                urlLinkEt.setText("https://www.mboxdrive.com/human.mp3");
                break;
            case 2:
                songEt.setText("Is This Love");
                singerEt.setText("Bob Marley");
                minutesEt.setText("3");
                secondsEt.setText("51");
                urlLinkEt.setText("https://www.mboxdrive.com/is_this_love.mp3");
                break;
            case 3:
                songEt.setText("In The End");
                singerEt.setText("Linkin Park");
                minutesEt.setText("3");
                secondsEt.setText("38");
                urlLinkEt.setText("https://www.mboxdrive.com/in_the_end.mp3");
                break;
            case 4:
                songEt.setText("Song #1");
                singerEt.setText("Unknown");
                minutesEt.setText("3");
                secondsEt.setText("47");
                urlLinkEt.setText("https://www.syntax.org.il/xtra/bob.m4a");
                break;
            case 5:
                songEt.setText("Song #2");
                singerEt.setText("UnknownUnknown");
                minutesEt.setText("5");
                secondsEt.setText("31");
                urlLinkEt.setText("https://www.syntax.org.il/xtra/bob1.m4a");
                break;
            case 6:
                songEt.setText("Song #3");
                singerEt.setText("Unknown");
                minutesEt.setText("3");
                secondsEt.setText("6");
                urlLinkEt.setText("https://www.syntax.org.il/xtra/bob2.mp3");
                break;
        }
    }

}