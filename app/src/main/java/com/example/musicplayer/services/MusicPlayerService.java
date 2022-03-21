package com.example.musicplayer.services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.example.musicplayer.R;
import com.example.musicplayer.activities.MainActivity;
import com.example.musicplayer.controller.FileHandler;
import com.example.musicplayer.model.Song;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerService extends Service
        implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener{

    private static MediaPlayer player = null;

    private ArrayList<Song> mSongs = null;
    private int currentIndex = 0;

    final int NOTIF_ID = 1;
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    RemoteViews remoteViews;
    NotificationTarget targetImageView;

    final public static String INDEX_TAG = "index";
    final public static String CASE_TAG = "case";
    final public static String LAUNCH_TAG = "launch";
    final String CLOSE_TAG = "close";
    final public static String PLAY_PAUSE_TAG = "play_pause";
    final String NEXT_TAG = "next";
    final String PREV_TAG = "prev";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; } // Irrelevant.

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        player.setOnPreparedListener(this);
        player.reset();

        handleNotificationSettings();

        targetImageView = new NotificationTarget(getApplicationContext(), R.id.imageview_notification_image,
                remoteViews, builder.build(), NOTIF_ID);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSongs = FileHandler.getSongArrayList(getApplicationContext());

        String currentCase = intent.getStringExtra(CASE_TAG);
        switch (currentCase) {
            case LAUNCH_TAG:
                currentIndex = intent.getIntExtra(INDEX_TAG, 0);
                setViewsAndCreateNotification();
                launchSong();
                break;

            case CLOSE_TAG:
                stopSelf();
                break;

            case PLAY_PAUSE_TAG:
                if (player.isPlaying()) {
                    remoteViews.setImageViewResource(R.id.imagebutton_play_pause_btn, android.R.drawable.ic_media_play);
                    player.pause();
                } else {
                    remoteViews.setImageViewResource(R.id.imagebutton_play_pause_btn, android.R.drawable.ic_media_pause);
                    player.start();
                }
                notificationManager.notify(NOTIF_ID, builder.build());
                break;

            case NEXT_TAG:
                remoteViews.setImageViewResource(R.id.imagebutton_play_pause_btn, android.R.drawable.ic_media_pause);
                currentIndex = (currentIndex + 1) % mSongs.size();
                setViewsAndCreateNotification();
                launchSong();
                break;

            case PREV_TAG:
                remoteViews.setImageViewResource(R.id.imagebutton_play_pause_btn, android.R.drawable.ic_media_pause);
                currentIndex -= 1;
                if (currentIndex == -1)
                    currentIndex = mSongs.size() - 1;
                setViewsAndCreateNotification();
                launchSong();
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(NOTIF_ID);
        if(player != null) {
            if(player.isPlaying())
                player.stop();
            player.release();
        }
    }

    // On completion - play next song on the list. If current song was the last, start over.
    @Override
    public void onCompletion(MediaPlayer mp) {
        currentIndex = (currentIndex + 1) % mSongs.size();
        setViewsAndCreateNotification();
        launchSong();
    }

    @Override
    public void onPrepared(MediaPlayer mp) { player.start(); }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void handleNotificationSettings() {
        // Create NotificationChannel and add Musify-Notification.
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        String channelId = null;
        if(Build.VERSION.SDK_INT >= 26) {
            channelId = "musify_notification_channel_id";
            CharSequence channelName = "Musify Channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        builder = new NotificationCompat.Builder(this, channelId);
        builder.setSmallIcon(R.drawable.musify_icon_round);

        remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);

        // Open MainActivity -> MainFragment when tapping on img/title.
        Intent openMainFragmentIntent = new Intent(this, MainActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent openMainFragmentPendingIntent = PendingIntent.
                getActivity(this, 0, openMainFragmentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.imageview_notification_image, openMainFragmentPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.textview_notification_title_song, openMainFragmentPendingIntent);

        // Kill MusicPlayerService and MusifyNotification when tapping on X button.
        Intent closeServiceIntent = new Intent(this, MusicPlayerService.class);
        closeServiceIntent.putExtra(CASE_TAG, CLOSE_TAG);
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent closeServicePendingIntent = PendingIntent.
                getService(this, 1, closeServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.imagebutton_notification_close, closeServicePendingIntent);

        // Play / pause music when tapping Play/Pause button.
        Intent playPauseIntent = new Intent(this, MusicPlayerService.class);
        playPauseIntent.putExtra(CASE_TAG, PLAY_PAUSE_TAG);
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent playPausePendingIntent = PendingIntent.
                getService(this, 2, playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.imagebutton_play_pause_btn, playPausePendingIntent);

        // Play next song on the list.
        Intent nextIntent = new Intent(this, MusicPlayerService.class);
        nextIntent.putExtra(CASE_TAG, NEXT_TAG);
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent nextPendingIntent = PendingIntent.
                getService(this, 3, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.imagebutton_next_btn, nextPendingIntent);

        // Play prev song on the list.
        Intent prevIntent = new Intent(this, MusicPlayerService.class);
        prevIntent.putExtra(CASE_TAG, PREV_TAG);
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent prevPendingIntent = PendingIntent.
                getService(this, 4, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.imagebutton_prev_btn, prevPendingIntent);

        builder.setContent(remoteViews);
    }

    private void setViewsAndCreateNotification() {
        // Load image through remoteViews instead of Glide.
        // Probably loads full-sized image, thus loads slow. Glide works faster.
//        if(mSongs.get(currentIndex).getImgUri().equals("")) {
//            remoteViews.setImageViewResource(R.id.imageview_notification_image, mSongs.get(currentIndex).getSongResId());
//        } else {
//            remoteViews.setImageViewUri(R.id.imageview_notification_image, Uri.parse(mSongs.get(currentIndex).getImgUri()));
//        }

        // Set image.
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Glide.with(getApplicationContext()).clear(targetImageView);
                // String empty = no picture was manually taken -> load songResId.
                // else = picture was taken -> load img-Uri.
                if (mSongs.get(currentIndex).getImgUri().equals("")) {
                    Glide.with(getApplicationContext()).asBitmap().override(100, 100).
                            load(mSongs.get(currentIndex).getSongResId()).into(targetImageView);
                } else {
                    Glide.with(getApplicationContext()).asBitmap().override(100, 100).
                            load(mSongs.get(currentIndex).getImgUri()).into(targetImageView);
                }
            }
        });

        // Set title (song name).
        remoteViews.setTextViewText(R.id.textview_notification_title_song, mSongs.get(currentIndex).getSong());

        // Create and update notification.
        notificationManager.notify(NOTIF_ID, builder.build());
        // Start Foreground.
        startForeground(NOTIF_ID, builder.build());
    }

    private void launchSong() {
        player.reset();
        try {
            player.setDataSource(mSongs.get(currentIndex).getSongUri());
            player.prepareAsync(); // Using async function instead of sync function player.prepare().
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
