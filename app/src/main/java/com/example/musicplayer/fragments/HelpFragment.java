package com.example.musicplayer.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.musicplayer.R;


public class HelpFragment extends DialogFragment {

    public interface OnSongSuggestionSelected {
        void songSuggestionSelected(int selection);
    }

    public OnSongSuggestionSelected callBack;

    ImageView exitIv;
    Button humanBtn, isThisLoveBtn, inTheEndBtn, song1Btn, song2Btn, song3Btn;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            // TODO: replace deprecated.
            // Source - Communicate between fragments - https://www.youtube.com/watch?v=LUV_djRHSEY
            callBack = (OnSongSuggestionSelected)getTargetFragment();
        } catch (ClassCastException ex) {
            throw new ClassCastException("Fragment must implement DeleteDialogListener interface.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        exitIv = view.findViewById(R.id.imageview_help_exit);
        exitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        humanBtn = view.findViewById(R.id.button_help_human);
        humanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.songSuggestionSelected(1);
                getParentFragmentManager().popBackStack();
            }
        });

        isThisLoveBtn = view.findViewById(R.id.button_help_is_this_love);
        isThisLoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.songSuggestionSelected(2);
                getParentFragmentManager().popBackStack();
            }
        });

        inTheEndBtn = view.findViewById(R.id.button_help_in_the_end);
        inTheEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.songSuggestionSelected(3);
                getParentFragmentManager().popBackStack();
            }
        });

        song1Btn = view.findViewById(R.id.button_help_song_1);
        song1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.songSuggestionSelected(4);
                getParentFragmentManager().popBackStack();
            }
        });

        song2Btn = view.findViewById(R.id.button_help_song_2);
        song2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.songSuggestionSelected(5);
                getParentFragmentManager().popBackStack();
            }
        });

        song3Btn = view.findViewById(R.id.button_help_song_3);
        song3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.songSuggestionSelected(6);
                getParentFragmentManager().popBackStack();
            }
        });

    }
}