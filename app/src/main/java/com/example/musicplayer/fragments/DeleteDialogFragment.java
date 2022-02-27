package com.example.musicplayer.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.musicplayer.model.Song;

import java.util.List;

public class DeleteDialogFragment extends DialogFragment {

    public interface DeleteDialogListener {
        void onPositiveButtonClicked();
        void onNegativeButtonClicked();
    }

    DeleteDialogListener callBack;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            callBack = (DeleteDialogListener)context;
        } catch (ClassCastException ex) {
            throw new ClassCastException("Activity/Fragment must implement DeleteDialogListener interface.");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("DELETE SONG").setMessage("Are you sure you want to delete this song?").
                setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callBack.onPositiveButtonClicked();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.onNegativeButtonClicked();
            }
        });

        return builder.create();
    }
}
