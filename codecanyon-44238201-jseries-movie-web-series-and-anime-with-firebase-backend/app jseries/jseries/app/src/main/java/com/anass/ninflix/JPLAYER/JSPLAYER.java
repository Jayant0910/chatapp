package com.anass.ninflix.JPLAYER;

import static android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;

import static com.anass.ninflix.Activities.Settings.SettingsJcartoon.SETTINGS_NAME;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.anass.ninflix.Config.Function;
import com.anass.ninflix.Config.Utils;
import com.anass.ninflix.DB.JseriesDB;
import com.anass.ninflix.Models.CountinuModel;
import com.anass.ninflix.R;
import com.google.android.exoplayer2.DeviceInfo;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.TracksInfo;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoSize;


import java.io.File;
import java.util.List;


public class JSPLAYER extends AppCompatActivity {

    private boolean isShowingTrackSelectionDialog;
    private DefaultTrackSelector trackSelector;
    String[] speed = {"0.25x","0.5x","Normal","1.5x","2x"};
    String url1,mCookie;
    String NameAndEpesoed;

    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;
    TextView cartoontitle,cartoonepesod,et_url;
    ImageButton videoView_go_back;
    ProgressBar prgorssd;
    LinearLayout downloadvideo,videoView_one_layout,videoView_three_layout
            ,videoView_four_layout,resizethevid;
    ImageView imageView5,imageView8;
    Long mLastPosition;
    String position;
    int epePOSIT;


    private SharedPreferences sp;
    private int currentPage;
    private int allduration;


    boolean flag = false;

    Handler controllerHandler;

    AudioManager audioManager;

    View volumeMenu;

    View forward;
    View backward;

    float y;
    float dy;

    int maxVolume = 0;
    int currentVolume = 0;



    ProgressDialog progressDialog;
    JseriesDB comicsdb;

    String hdlink,postkey,epetitle,type,fhdlink,sdlink,qdlink,message;
    String posterid,titlecartoon,id,poste,place,age,studio,date,story,cast,tasnif,where,mortabit_id,chapters;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_jplayer);
        sp = getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
        comicsdb = new JseriesDB(JSPLAYER.this);

        url1 = getIntent().getStringExtra("url");
        setSystemBarTransparent(this);
        hideSystemPlayerUi(this,true,0);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        String name = getIntent().getStringExtra("name");
        titlecartoon = getIntent().getStringExtra("name");
        String eName = getIntent().getStringExtra("eName");
        epetitle = getIntent().getStringExtra("eName");
        message = getIntent().getStringExtra("message");

        id = getIntent().getStringExtra("id");
        posterid = getIntent().getStringExtra("posterid");
        poste = getIntent().getStringExtra("poste");
        place = getIntent().getStringExtra("place");
        age = getIntent().getStringExtra("age");
        studio = getIntent().getStringExtra("studio");
        date = getIntent().getStringExtra("date");
        story = getIntent().getStringExtra("story");
        cast = getIntent().getStringExtra("cast");
        tasnif = getIntent().getStringExtra("tasnif");
        where = getIntent().getStringExtra("where");
        mortabit_id = getIntent().getStringExtra("mortabit_id");
        chapters = getIntent().getStringExtra("chapters");

        type = getIntent().getStringExtra("type");
        fhdlink = getIntent().getStringExtra("fhd");
        hdlink = getIntent().getStringExtra("hd");
        sdlink = getIntent().getStringExtra("sd");
        qdlink = getIntent().getStringExtra("qd");
        postkey = getIntent().getStringExtra("key");

        position = getIntent().getStringExtra("position");
        NameAndEpesoed = name+":"+eName;
        currentPage = sp.getInt("currentPage"+NameAndEpesoed,1);
        allduration = sp.getInt("allduration"+NameAndEpesoed,1);
        epePOSIT = Function.ToInterger(position);

        playerView = findViewById(R.id.jplayer);
        cartoontitle = findViewById(R.id.cartoontitle);
        cartoonepesod = findViewById(R.id.cartoonepesod);
        videoView_go_back = findViewById(R.id.videoView_go_back);
        prgorssd = findViewById(R.id.prgorssd);
        imageView5 = findViewById(R.id.imageView5);
        imageView8 = findViewById(R.id.imageView8);
        downloadvideo = findViewById(R.id.downloadvideo);
        videoView_three_layout = findViewById(R.id.videoView_three_layout);
        videoView_four_layout = findViewById(R.id.videoView_four_layout);
        videoView_one_layout = findViewById(R.id.videoView_one_layout);
        ImageButton farwordBtn = playerView.findViewById(R.id.fwd);
        ImageButton rewBtn = playerView.findViewById(R.id.rew);
        LinearLayout resizethevid = playerView.findViewById(R.id.resizethevid);
        LinearLayout NextEpebtn = playerView.findViewById(R.id.exo_playback_next_epe);
        TextView speedTxt = playerView.findViewById(R.id.speed);
        volumeMenu = findViewById(R.id.volume_menu);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        controllerHandler = new Handler();


        if (eName == null){
            cartoonepesod.setVisibility(View.GONE);
        }

        videoView_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory());
        simpleExoPlayer = new SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector).build();
        playerView.setPlayer(simpleExoPlayer);


        resizethevid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentWidth = playerView.getWidth();
                switch (currentWidth) {
                    case 1:
                        playerView.setLayoutParams(new ViewGroup.LayoutParams(800, playerView.getHeight()));
                        break;
                    case 2:
                        playerView.setLayoutParams(new ViewGroup.LayoutParams(400, playerView.getHeight()));
                        break;
                    case 3:
                        playerView.setLayoutParams(new ViewGroup.LayoutParams(playerView.getWidth(), playerView.getHeight()));
                        break;
                }
                }});

        SetEpeLinks(url1,name,eName,message);


        simpleExoPlayer.addListener(new Player.Listener() {
            @Override
            public void onTimelineChanged(Timeline timeline, int reason) {

            }

            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {

            }

            @Override
            public void onTracksInfoChanged(TracksInfo tracksInfo) {

            }

            @Override
            public void onIsLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onAvailableCommandsChanged(Player.Commands availableCommands) {

            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {

                if (playbackState == Player.STATE_BUFFERING){
                    prgorssd.setVisibility(View.VISIBLE);

                }else if (playbackState == Player.STATE_READY){
                    prgorssd.setVisibility(View.GONE);

                }

            }

            @Override
            public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {

            }

            @Override
            public void onPlaybackSuppressionReasonChanged(int playbackSuppressionReason) {

            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(PlaybackException error) {

            }

            @Override
            public void onPlayerErrorChanged(@Nullable PlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(Player.PositionInfo oldPosition, Player.PositionInfo newPosition, int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekForwardIncrementChanged(long seekForwardIncrementMs) {

            }

            @Override
            public void onSeekBackIncrementChanged(long seekBackIncrementMs) {

            }

            @Override
            public void onAudioSessionIdChanged(int audioSessionId) {

            }

            @Override
            public void onAudioAttributesChanged(AudioAttributes audioAttributes) {

            }

            @Override
            public void onVolumeChanged(float volume) {

            }

            @Override
            public void onSkipSilenceEnabledChanged(boolean skipSilenceEnabled) {

            }

            @Override
            public void onDeviceInfoChanged(DeviceInfo deviceInfo) {

            }

            @Override
            public void onDeviceVolumeChanged(int volume, boolean muted) {

            }

            @Override
            public void onEvents(Player player, Player.Events events) {

            }

            @Override
            public void onVideoSizeChanged(VideoSize videoSize) {

            }

            @Override
            public void onSurfaceSizeChanged(int width, int height) {

            }

            @Override
            public void onRenderedFirstFrame() {

            }

            @Override
            public void onCues(List<Cue> cues) {

            }

            @Override
            public void onMetadata(Metadata metadata) {

            }

            @Override
            public void onMediaMetadataChanged(MediaMetadata mediaMetadata) {

            }

            @Override
            public void onPlaylistMetadataChanged(MediaMetadata mediaMetadata) {

            }
        });







       /* NextEpebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (epePOSIT == getComicsnextback.Epesodes.size()-1 ){
                    Toast.makeText(Jplayer.this, "هده أخر حلقة", Toast.LENGTH_SHORT).show();
                }else {
                    epePOSIT++;
                    letGo(getComicsnextback.Epesodes.get(epePOSIT).getDownloadhd());
                }


            }
        });*/

        farwordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() + 10000);

            }
        });

        rewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long num = simpleExoPlayer.getCurrentPosition() - 10000;
                if (num < 0) {

                    simpleExoPlayer.seekTo(0);


                } else {

                    simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() - 10000);

                }


            }
        });

    }

    private void SaveinDB() {

        CountinuModel comicsModel = new CountinuModel();
        comicsModel.setId(id);
        comicsModel.setTitle(titlecartoon);
        comicsModel.setPostkey(posterid);
        comicsModel.setImage(poste);
        comicsModel.setDate(date);
        comicsModel.setTasnifh(tasnif);
        comicsModel.setStudio(studio);
        comicsModel.setAge(age);
        comicsModel.setDesc(story);
        comicsModel.setCast(cast);
        comicsModel.setEpesodes(mortabit_id);
        comicsModel.setAlmotarjim(where);
        comicsModel.setSeasons(place);
        comicsModel.setLink(chapters);
        comicsModel.setEpetitle(epetitle);
        comicsModel.setPostkey(postkey);
        comicsModel.setServer1(hdlink);
        comicsModel.setServer2(sdlink);
        comicsModel.setServer3(fhdlink);

        comicsModel.setPosit(position);
        comicsModel.setCurrnt(String.valueOf(simpleExoPlayer.getCurrentPosition()));
        comicsModel.setDuration(String.valueOf(simpleExoPlayer.getDuration()));



        if (!comicsdb.checkIfCountineExists(comicsModel.getPostkey())){
            comicsdb.AddCountineWatchingDB(comicsModel);

        }else {
            comicsdb.UpdateCountinuDB(comicsModel);

        }
    }


    public void showVolumeMenu(){
        View volumeBar = findViewById(R.id.volume_bar);
        TextView volumeText = findViewById(R.id.volume_text);

        int mV = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int cV = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int barH = (Utils.DpToPx(100, this) / mV) * cV;

        volumeText.setText("" + cV);
        volumeBar.getLayoutParams().height = barH;
        volumeBar.requestLayout();

        volumeMenu.setVisibility(View.VISIBLE);
    }

    public void hideVolumeMenu(){
        volumeMenu.setVisibility(View.INVISIBLE);
    }

    boolean doubleClick = false;


    private void SetEpeLinks(String url,String name,String epeNumber,String message) {



        cartoontitle.setText(name);
        cartoonepesod.setText(epeNumber);

        MediaItem videoSource = null;
        MediaSource mediaSource = null;


        if(message.equals("")) {
            Uri video1 = Uri.parse(url);
            videoSource = MediaItem.fromUri(video1);
            mediaSource = buildMediaSource(video1);
        }else if(message.equals("local")){
            Uri video1 = Uri.fromFile(new File(url));
            videoSource = MediaItem.fromUri(video1);
            mediaSource = buildMediaSource(video1);
        }



        //simpleExoPlayer.addMediaItem(videoSource);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
        if (currentPage == 0){
            simpleExoPlayer.play();

        }else {
            simpleExoPlayer.seekTo(currentPage);
            simpleExoPlayer.play();

        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        playerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    y = event.getY();

                    maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);


                }
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    dy = event.getY() - y;

                    if(Math.abs(dy) > 10) {
                        float delta = dy / (Math.abs(width - height) / 2);
                        int volume = (int) (delta * maxVolume * -1);

                        if (volume > maxVolume - currentVolume) {
                            volume = maxVolume - currentVolume;
                        }
                        if (volume < -currentVolume) {
                            volume = -currentVolume;
                        }

                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume + volume, 0);

                        showVolumeMenu();
                    }
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    hideVolumeMenu();
                }
                return false;
            }
        });

    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                JSPLAYER.this, Util.getUserAgent(JSPLAYER.this, "jseries"));
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri));
    }

    public static void setSystemBarTransparent(Activity act) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

    }


    // the system bars on Player
    public static void hideSystemPlayerUi(@NonNull final Activity activity, final boolean immediate) {
        hideSystemPlayerUi(activity, immediate, 5000);
    }

    // This snippet hides the system bars for api 30 or less
    @SuppressLint("ObsoleteSdkInt")
    public static void hideSystemPlayerUi(@NonNull final Activity activity, final boolean immediate, final int delayMs) {


        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        View decorView = activity.getWindow().getDecorView();
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        int uiState = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar
        if (Util.SDK_INT > 18) {
            uiState |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
        } else {
            final Handler handler = new Handler(Looper.getMainLooper());
            decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
                if (visibility == View.VISIBLE) {
                    Runnable runnable = () -> hideSystemPlayerUi(activity, false);
                    if (immediate) {
                        handler.post(runnable);
                    } else {
                        handler.postDelayed(runnable, delayMs);
                    }
                }
            });
        }
        decorView.setSystemUiVisibility(uiState);

    }

    private void showDefaultControls() {

        videoView_go_back.setVisibility(View.VISIBLE);
        videoView_one_layout.setVisibility(View.VISIBLE);
        imageView5.setVisibility(View.VISIBLE);
        imageView8.setVisibility(View.VISIBLE);
        videoView_three_layout.setVisibility(View.VISIBLE);
        videoView_four_layout.setVisibility(View.VISIBLE);
    }


    private void hideDefaultControls(){
        videoView_go_back.setVisibility(View.GONE);
        videoView_one_layout.setVisibility(View.GONE);
        imageView5.setVisibility(View.GONE);
        imageView8.setVisibility(View.GONE);
        videoView_three_layout.setVisibility(View.GONE);
        videoView_four_layout.setVisibility(View.GONE);
        //Todo this function will hide status and navigation when we click on screen

    }



    @Override
    protected void onResume() {
        super.onResume();
        sp.edit().putInt("currentPage"+ NameAndEpesoed, (int) simpleExoPlayer.getCurrentPosition()).apply();
        sp.edit().putInt("allduration"+ NameAndEpesoed, (int) simpleExoPlayer.getDuration()).apply();

        if (!message.equals("local")){
            if(simpleExoPlayer.getDuration() > 100000){
                SaveinDB();
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        simpleExoPlayer.release();
        sp.edit().putInt("currentPage"+ NameAndEpesoed, (int) simpleExoPlayer.getCurrentPosition()).apply();
        sp.edit().putInt("allduration"+ NameAndEpesoed, (int) simpleExoPlayer.getDuration()).apply();
        if (!message.equals("local")){
            if(simpleExoPlayer.getDuration() > 100000){
                SaveinDB();
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        super.onPause();
        if (simpleExoPlayer!=null) {
            simpleExoPlayer.stop();
            mLastPosition = simpleExoPlayer.getCurrentPosition();
        }
        sp.edit().putInt("currentPage"+ NameAndEpesoed, (int) simpleExoPlayer.getCurrentPosition()).apply();
        sp.edit().putInt("allduration"+ NameAndEpesoed, (int) simpleExoPlayer.getDuration()).apply();
        if (!message.equals("local")){
            if(simpleExoPlayer.getDuration() > 100000){
                SaveinDB();
            }
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        simpleExoPlayer.stop();
        sp.edit().putInt("currentPage"+ NameAndEpesoed, (int) simpleExoPlayer.getCurrentPosition()).apply();
        sp.edit().putInt("allduration"+ NameAndEpesoed, (int) simpleExoPlayer.getDuration()).apply();

        if (!message.equals("local")){
            if(simpleExoPlayer.getDuration() > 100000){
                SaveinDB();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}