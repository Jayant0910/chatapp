package com.anass.ninflix.JPLAYER;

import static com.anass.ninflix.Config.config.Livelink;
import static com.anass.ninflix.JPLAYER.JSPLAYER.hideSystemPlayerUi;
import static com.anass.ninflix.JPLAYER.JSPLAYER.setSystemBarTransparent;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.anass.ninflix.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

public class LivePlayer extends AppCompatActivity {

    ImageButton videoView_go_back;
    ExoPlayer player;
    PlayerView playerView;

    String channel_url;
    String channel_title;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_player);
        setSystemBarTransparent(this);
        hideSystemPlayerUi(this,true,0);

        channel_url = getIntent().getStringExtra("url");
        channel_title = getIntent().getStringExtra("title");

        //ll
        videoView_go_back = findViewById(R.id.videoView_go_back);
        playerView = findViewById(R.id.jplayer);


        videoView_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        playchannel(channel_url);









    }

    public void playchannel(String livelink) {

        DefaultTrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory());
        player = new SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector).build();
        playerView.setPlayer(player);


        MediaItem videoSource = null;
        //MediaSource mediaSource = null;

        Uri video1 = Uri.parse(livelink);
        videoSource = MediaItem.fromUri(video1);
       // mediaSource = buildMediaSource(video1);


        player.addMediaItem(videoSource);
        player.prepare();
        player.setPlayWhenReady(true);
        player.play();
    }


    @Override
    protected void onDestroy() {
        player.release();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        player.setPlayWhenReady(false);
        super.onPause();
    }

    @Override
    protected void onResume() {
        player.seekToDefaultPosition();
        player.setPlayWhenReady(true);

        super.onResume();
    }
}