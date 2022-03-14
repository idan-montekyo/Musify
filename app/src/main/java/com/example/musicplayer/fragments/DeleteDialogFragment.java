package com.example.musicplayer.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.musicplayer.R;

import java.util.Objects;

public class DeleteDialogFragment extends DialogFragment {

    private static final String ARG_POSITION = "position";

    public interface DeleteDialogListener {
        void onPositiveButtonClicked(int position);
        void onNegativeButtonClicked();
    }

    DeleteDialogListener callBack;

    public static DeleteDialogFragment newInstance(int position) {

        DeleteDialogFragment fragment = new DeleteDialogFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_POSITION, position);

        fragment.setArguments(args);
        return fragment;
    }

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

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        setCancelable(false);

        builder.setTitle(getResources().getString(R.string.delete_song)).
                setMessage(getResources().getString(R.string.are_you_sure_you_want_to_delete_this_song)).
                setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        assert getArguments() != null;
                        callBack.onPositiveButtonClicked(getArguments().getInt(ARG_POSITION));
                    }
                }).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.onNegativeButtonClicked();
            }
        });

        return builder.create();
    }
}
