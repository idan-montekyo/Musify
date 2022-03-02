package com.example.musicplayer.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.musicplayer.R;


public class HelpFragment extends DialogFragment {

    public interface OnSongSuggestionSelected {
        void sendSelection(int selection);
    }

    public OnSongSuggestionSelected callBack;

    EditText songEt, singerEt, minutesEt, secondsEt, urlLinkEt;

    ImageView exitIv;
    Button humanBtn, isThisLoveBtn, inTheEndBtn, song1Btn, song2Btn, song3Btn;

//    public static HelpFragment newInstance(String param1, String param2) {
//        HelpFragment fragment = new HelpFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            callBack = (OnSongSuggestionSelected)getTargetFragment();
        } catch (ClassCastException ex) {
            throw new ClassCastException("implement...");
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

        songEt = view.findViewById(R.id.edittext_add_song);
        singerEt = view.findViewById(R.id.edittext_add_singer);
        minutesEt = view.findViewById(R.id.edittext_add_minutes);
        secondsEt = view.findViewById(R.id.edittext_add_seconds);
        urlLinkEt = view.findViewById(R.id.edittext_add_link);

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
                callBack.sendSelection(1);
                getParentFragmentManager().popBackStack();
            }
        });

        isThisLoveBtn = view.findViewById(R.id.button_help_is_this_love);
        isThisLoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.sendSelection(2);
                getParentFragmentManager().popBackStack();
            }
        });

        inTheEndBtn = view.findViewById(R.id.button_help_in_the_end);
        inTheEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.sendSelection(3);
                getParentFragmentManager().popBackStack();
            }
        });

        song1Btn = view.findViewById(R.id.button_help_song_1);
        song1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.sendSelection(4);
                getParentFragmentManager().popBackStack();
            }
        });

        song2Btn = view.findViewById(R.id.button_help_song_2);
        song2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.sendSelection(5);
                getParentFragmentManager().popBackStack();
            }
        });

        song3Btn = view.findViewById(R.id.button_help_song_3);
        song3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.sendSelection(6);
                getParentFragmentManager().popBackStack();
            }
        });

    }
}