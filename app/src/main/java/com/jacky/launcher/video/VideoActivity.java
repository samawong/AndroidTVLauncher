package com.jacky.launcher.video;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.jacky.launcher.R;
import com.jacky.launcher.detail.MediaModel;

//import cn.jzvd.Jzvd;
//import cn.jzvd.JzvdStd;

@UnstableApi public class VideoActivity extends Activity {

    protected PlayerView playerView;
    public static final String VIDEO = "Video";
    private MediaModel mMediaModel;
    protected  @Nullable ExoPlayer player;

    private  boolean playWhenReady = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMediaModel = getIntent().getParcelableExtra(VideoActivity.VIDEO);
        setContentView();

        playerView = findViewById(R.id.player_view);
        playerView.requestFocus();

    }

    @Override
    public void onStart(){
        super.onStart();
        if(Build.VERSION.SDK_INT > 23){
            initializerPlay();
            if(playerView != null){
                playerView.onResume();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT <= 23 || player == null){
            initializerPlay();
            if(playerView != null){
                playerView.onResume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT <= 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT > 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void setContentView() {
        setContentView(R.layout.activity_video);
    }

    private void initializerPlay(){
        if(player == null){
            player = new ExoPlayer.Builder(this).build();
            player.setPlayWhenReady(false);
            playerView.setPlayer(player);
        }
        //player.addMediaItem(MediaItem.fromUri(mMediaModel.getVideoUrl()));
        MediaItem mediaItem = new MediaItem.Builder().setUri(mMediaModel.getVideoUrl()).build();
        player.setMediaItem(mediaItem);
        player.setPlayWhenReady(playWhenReady);
        playerView.setPlayer(player);
        player.prepare();
    }

    protected void releasePlayer(){
        if(player != null){
            playWhenReady = player.getPlayWhenReady();
            player.release();
        }
        player = null;
    }
}
