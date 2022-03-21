package com.example.musicplayer.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicplayer.R;
import com.example.musicplayer.model.Song;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;


public class AddSongFragment extends Fragment implements HelpFragment.OnSongSuggestionSelected {

    // Source - Implement ActivityResultLauncher - https://www.youtube.com/watch?v=HxlAktedIhM
    ActivityResultLauncher<Intent> takePictureActivityResultLauncher;
    ActivityResultLauncher<String> pickFromGalleryActivityResultLauncher;

    final String ADD_SONG_FRAGMENT_TAG = "add_song_fragment";
    final String HELP_FRAGMENT_TAG = "help_fragment";

    EditText songEt, singerEt, songUriLinkEt;
    Button takePictureBtn, pickFromGalleryBtn, addSongBtn, needHelpBtn;
    ImageView roundImgIv, squareImgIv, exitIv;

    private int drawable = R.drawable.musify_icon_round;
    private Song newSong;
    private String song, singer, minutes, seconds, songUriLink, duration, imgUri = "";

    MediaPlayer mediaPlayerToMeasureLength;
    private int durationInMilliseconds;

    private String currentPhotoPath;

    public interface AddSongListener {
        void onAddButtonClicked(Song song);
    }

    AddSongListener callBack;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mediaPlayerToMeasureLength = new MediaPlayer();
        mediaPlayerToMeasureLength.reset();

        try {
            callBack = (AddSongListener)context;
        } catch (ClassCastException ex) {
            throw new ClassCastException("Activity/Fragment must implement AddSongListener interface.");
        }

        // Source - Capture image with camera - https://www.youtube.com/watch?v=RaOyw84625w
        // After taking a picture - load into both ImageViews, and save URI to imgUri.
        takePictureActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Check condition. In case camera opened and closed.
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Glide.with(Objects.requireNonNull(getContext())).
                                    load(currentPhotoPath).circleCrop().into(roundImgIv);
                            Glide.with(getContext()).load(currentPhotoPath).into(squareImgIv);
                            imgUri = currentPhotoPath;
                        }
                    }
                }
        );

        // Source - Open file manager and pick photo (get Uri) - https://www.youtube.com/watch?v=cXyeozbLqq0
        // After picking from gallery - Load into both ImageViews, and save URI as String to imgUri.
        pickFromGalleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            Glide.with(Objects.requireNonNull(getContext())).
                                    load(result).circleCrop().into(roundImgIv);
                            Glide.with(getContext()).load(result).into(squareImgIv);
                            imgUri = result.toString();
                        }
                    }
                }
        );
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
        songUriLinkEt = view.findViewById(R.id.edittext_add_link);
        roundImgIv = view.findViewById(R.id.imageview_add_img_round);
        squareImgIv = view.findViewById(R.id.imageview_add_img_square);

        exitIv = view.findViewById(R.id.imageview_add_exit);
        exitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        // Request for camera permission.
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    new String[] { Manifest.permission.CAMERA }, 100);
        }
        // Request for gallery permission.
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }

        // Source - Take photos - Save the full-size photo - https://developer.android.com/training/camera/photobasics
        takePictureBtn = view.findViewById(R.id.button_add_take_picture);
        takePictureBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void onClick(View v) {
                // Open camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // Ensure that there's a camera activity to handle the intent
                if (intent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ignored) {} // Error occurred while creating the File

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(Objects.requireNonNull(getContext()),
                                "com.example.android.fileprovider",
                                photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        takePictureActivityResultLauncher.launch(intent);
                    }
                }
            }
        });

        pickFromGalleryBtn = view.findViewById(R.id.button_add_pick_picture);
        pickFromGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open gallery
                pickFromGalleryActivityResultLauncher.launch("image/*");
            }
        });

        addSongBtn = view.findViewById(R.id.button_add_song);
        addSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    mediaPlayerToMeasureLength.setDataSource(songUriLinkEt.getText().toString());
                    mediaPlayerToMeasureLength.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                song = songEt.getText().toString();
                singer = singerEt.getText().toString();
                songUriLink = songUriLinkEt.getText().toString();

                durationInMilliseconds = mediaPlayerToMeasureLength.getDuration();
                minutes = (int)(durationInMilliseconds % (1000 * 60 * 60)) / (1000 * 60) + "";
                seconds = (int)((durationInMilliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000) + "";
                if (Integer.parseInt(seconds) < 10) { seconds = "0" + seconds; }
                duration = minutes + ":" + seconds;

                if (song.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.please_fill_in_song_name), Toast.LENGTH_SHORT).show();
                } else if (singer.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.please_fill_in_singer_name), Toast.LENGTH_SHORT).show();
                } else if (songUriLink.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.please_add_url_link_to_a_song), Toast.LENGTH_SHORT).show();
                } else {
                    newSong = new Song(drawable, imgUri, song, singer, duration, songUriLink);
                    getParentFragmentManager().popBackStack();
                    callBack.onAddButtonClicked(newSong);

                    // Release mediaPlayer
                    mediaPlayerToMeasureLength.reset();
                    mediaPlayerToMeasureLength.release();
                }
            }
        });

        needHelpBtn = view.findViewById(R.id.button_add_need_help);
        needHelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpFragment helpFragment = new HelpFragment();
                // TODO: replace deprecated.
                // Source - Communicate between fragments - https://www.youtube.com/watch?v=LUV_djRHSEY
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
    public void songSuggestionSelected(int selection) {

        switch (selection) {
            case 1:
                songEt.setText("Human");
                singerEt.setText("Rag'n'Bone Man");
                songUriLinkEt.setText("https://www.mboxdrive.com/human.mp3");
                drawable = R.drawable.img_human;
                squareImgIv.setImageResource(R.drawable.img_human);
                Glide.with(this).load(R.drawable.img_human).
                        apply(RequestOptions.circleCropTransform()).into(roundImgIv);

                break;
            case 2:
                songEt.setText("Is This Love");
                singerEt.setText("Bob Marley");
                songUriLinkEt.setText("https://www.mboxdrive.com/is_this_love.mp3");
                drawable = R.drawable.img_is_this_love;
                squareImgIv.setImageResource(R.drawable.img_is_this_love);
                Glide.with(this).load(R.drawable.img_is_this_love).
                        apply(RequestOptions.circleCropTransform()).into(roundImgIv);
                break;
            case 3:
                songEt.setText("In The End");
                singerEt.setText("Linkin Park");
                songUriLinkEt.setText("https://www.mboxdrive.com/in_the_end_.mp3");
                drawable = R.drawable.img_in_the_end;
                squareImgIv.setImageResource(R.drawable.img_in_the_end);
                Glide.with(this).load(R.drawable.img_in_the_end).
                        apply(RequestOptions.circleCropTransform()).into(roundImgIv);
                break;
            case 4:
                songEt.setText("Song #1");
                singerEt.setText("Unknown");
                songUriLinkEt.setText("https://www.syntax.org.il/xtra/bob.m4a");
                break;
            case 5:
                songEt.setText("Song #2");
                singerEt.setText("Unknown");
                songUriLinkEt.setText("https://www.syntax.org.il/xtra/bob1.m4a");
                break;
            case 6:
                songEt.setText("Song #3");
                singerEt.setText("Unknown");
                songUriLinkEt.setText("https://www.syntax.org.il/xtra/bob2.mp3");
                break;
        }
    }

    // Source - https://developer.android.com/training/camera/photobasics
    @RequiresApi(api = Build.VERSION_CODES.N)
    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Objects.requireNonNull(getActivity()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}