package com.example.musicplayer.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicplayer.R;
import com.example.musicplayer.activities.MainActivity;
import com.example.musicplayer.model.Song;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;


public class AddSongFragment extends Fragment implements HelpFragment.OnSongSuggestionSelected {

    // Source - Implement ActivityResultLauncher - https://www.youtube.com/watch?v=HxlAktedIhM
    ActivityResultLauncher<Intent> takePictureActivityResultLauncher;
    ActivityResultLauncher<String> pickFromGalleryActivityResultLauncher;

    final String ADD_SONG_FRAGMENT_TAG = "add_song_fragment";
    final String HELP_FRAGMENT_TAG = "help_fragment";

    EditText songEt, singerEt, minutesEt, secondsEt, urlLinkEt;
    Button takePictureBtn, pickFromGalleryBtn, addSongBtn, needHelpBtn;
    ImageView roundImgIv, squareImgIv, exitIv;

    private int drawable = R.drawable.musify_icon_round;
    private Song newSong;
    private String song, singer, minutes, seconds, urlLink, duration, imgAsString = "";

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

        // Source - Capture image with camera - https://www.youtube.com/watch?v=RaOyw84625w
        // Source - Encode & Decode image in Base64 - https://www.youtube.com/watch?v=R_OSC0-nSIg
        // After taking a picture - load into both ImageViews, and save Base64 String to imgAsString.
        takePictureActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Initialize result data.
                        Intent intent = result.getData();
                        // Check condition. Right condition in case camera opened and closed.
                        if (intent != null && intent.getExtras() != null) {
                            // Get Bitmap data / Initialize Bitmap
                            Bitmap bitmap = (Bitmap) intent.getExtras().get("data");

                            imgAsString = LoadImageAndConvertFromBitmapToBase64(bitmap);
                        }
                    }
                }
        );

        // Source - Open file manager and pick photo (get Uri) - https://www.youtube.com/watch?v=cXyeozbLqq0
        // After picking from gallery - Converts Uri to Base64 String
        pickFromGalleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        try {
                            // Initialize Bitmap.
                            if (result != null) {
                                Bitmap bitmap = MediaStore.Images.Media.
                                        getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), result);

                                imgAsString = LoadImageAndConvertFromBitmapToBase64(bitmap);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
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
        minutesEt = view.findViewById(R.id.edittext_add_minutes);
        secondsEt = view.findViewById(R.id.edittext_add_seconds);
        urlLinkEt = view.findViewById(R.id.edittext_add_link);
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

        takePictureBtn = view.findViewById(R.id.button_add_take_picture);
        takePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureActivityResultLauncher.launch(intent);
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
                    newSong = new Song(drawable, imgAsString, song, singer, duration, urlLink);
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
                minutesEt.setText("3");
                secondsEt.setText("17");
                urlLinkEt.setText("https://www.mboxdrive.com/human.mp3");
                drawable = R.drawable.img_human;
                squareImgIv.setImageResource(R.drawable.img_human);
                Glide.with(this).load(R.drawable.img_human).
                        apply(RequestOptions.circleCropTransform()).into(roundImgIv);

                break;
            case 2:
                songEt.setText("Is This Love");
                singerEt.setText("Bob Marley");
                minutesEt.setText("3");
                secondsEt.setText("51");
                urlLinkEt.setText("https://www.mboxdrive.com/is_this_love.mp3");
                drawable = R.drawable.img_is_this_love;
                squareImgIv.setImageResource(R.drawable.img_is_this_love);
                Glide.with(this).load(R.drawable.img_is_this_love).
                        apply(RequestOptions.circleCropTransform()).into(roundImgIv);
                break;
            case 3:
                songEt.setText("In The End");
                singerEt.setText("Linkin Park");
                minutesEt.setText("3");
                secondsEt.setText("38");
                urlLinkEt.setText("https://www.mboxdrive.com/in_the_end_.mp3");
                drawable = R.drawable.img_in_the_end;
                squareImgIv.setImageResource(R.drawable.img_in_the_end);
                Glide.with(this).load(R.drawable.img_in_the_end).
                        apply(RequestOptions.circleCropTransform()).into(roundImgIv);
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

    private String LoadImageAndConvertFromBitmapToBase64(Bitmap bitmap) {
        // Set images to ImageViews in AddSongFragment XML.
        Glide.with(Objects.requireNonNull(getContext())).load(bitmap).
                apply(RequestOptions.circleCropTransform()).into(roundImgIv);
        Glide.with(getContext()).load(bitmap).into(squareImgIv);

        // Initialize byte stream
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress Bitmap
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        // Initialize byte array
        byte[] bytes = stream.toByteArray();
        // Get Base64 encoded string
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

}