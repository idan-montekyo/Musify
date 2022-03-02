package com.example.musicplayer.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide ActionBar.
        if (getSupportActionBar() != null) { getSupportActionBar().hide(); }

        // Set custom GIF as splash activity, and add slide movement from bottom to top.
        ImageView circleIv = findViewById(R.id.splash_icon);
        Glide.with(this).load(R.drawable.splash_musify).into(circleIv);
        Animation rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash);
        circleIv.setAnimation(rotate);

        // Show splash for 3.5 seconds, then move to MainActivity.
        // Also kill this activity using finish() to disable returning to it via back-press.
        new Thread() {
            @Override
            public void run() {
                try {
                    MediaPlayer mp = MediaPlayer.create(SplashActivity.this, R.raw.musify);
                    new Handler(Looper.getMainLooper()).postDelayed(mp::start, 1250);
                    sleep(3500);
                }
                catch (InterruptedException e) { e.printStackTrace(); }
                finally {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }.start();

    }
}
