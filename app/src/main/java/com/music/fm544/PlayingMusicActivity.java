package com.music.fm544;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.music.fm544.Bean.MusicPO;
import com.music.fm544.Service.MusicService;
import com.music.fm544.Utils.StatusBarUtils;
import com.music.fm544.Views.PlayMusicView;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class PlayingMusicActivity extends AppCompatActivity {
    private Intent mServiceIntent;
    private MusicService.MusicBind mMusicBind;
    private boolean isBindService;



    private PlayMusicView mPlayMusicView;
    private ImageView imageView;
    private ImageView playMusicBtn;
    private ImageView previousMusicBtn;
    private ImageView nextMusicBtn;
    private TextView mSongName;
    private TextView mSinger;


    //本地广播
    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;
    private LocalReciver localReciver;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mMusicBind = (MusicService.MusicBind) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_music);
        initBindService();


        ImageView back =  (ImageView) this.findViewById(R.id.back_btn);
        mSongName = (TextView) this.findViewById(R.id.songName);
        mSinger = (TextView) this.findViewById(R.id.singer);
        mPlayMusicView = (PlayMusicView) this.findViewById(R.id.play_music_view);
        playMusicBtn = (ImageView) this.findViewById(R.id.play_music);
        previousMusicBtn = (ImageView) this.findViewById(R.id.previous_music);
        nextMusicBtn = (ImageView) this.findViewById(R.id.next_music);

        initView();

        //注册本地广播监听器
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.fm544.broadcast.REFRESH_MUSIC");
        localReciver = new LocalReciver();
        localBroadcastManager.registerReceiver(localReciver, intentFilter);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        playMusicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trigger();
            }
        });

        nextMusicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNextMusic();
            }
        });

        previousMusicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPreviousMusic();
            }
        });

    }

    //播放上一首歌
    private void playPreviousMusic() {
        MyApplication app = (MyApplication)this.getApplicationContext();
        if (app.getPlayListSize() != 0){
            if (mMusicBind != null){
                mMusicBind.previousMusic();
            }
            MusicPO music = app.getMusic();
            mPlayMusicView.setMusicIcon(music);
            mPlayMusicView.playMusic();
            initView();
        }
    }

    //播放下一首歌
    private void playNextMusic() {
        MyApplication app = (MyApplication)this.getApplicationContext();
        if (app.getPlayListSize() != 0){
            if (mMusicBind != null){
                mMusicBind.nextMusic();
            }
            MusicPO music = app.getMusic();
            mPlayMusicView.setMusicIcon(music);
            mPlayMusicView.playMusic();
            initView();
        }

    }


    //歌曲暂停播放
    private void trigger() {
        MyApplication app = (MyApplication) this.getApplicationContext();
        if (app.getMusic()!=null){
            if (app.isPlaying()){
                playMusicBtn.setImageResource(R.mipmap.music_play);
                if (mMusicBind != null){
                    mMusicBind.stopMusic();
                }
                mPlayMusicView.stopMusic();
            }else{
                playMusicBtn.setImageResource(R.mipmap.music_stop);
                if (mMusicBind != null){
                    mMusicBind.playMusic();
                }
                mPlayMusicView.playMusic();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBindService){
            isBindService = false;
            this.unbindService(conn);
        }
        localBroadcastManager.unregisterReceiver(localReciver);
    }


    //绑定服务
    public void initBindService(){
        MyApplication app = (MyApplication) getApplication();
        mServiceIntent = app.getServiceIntent();
        //绑定service
        if (!isBindService){
            isBindService = true;
            this.bindService(mServiceIntent,conn, Context.BIND_AUTO_CREATE);
        }
    }


    private void initView(){
        MyApplication app = (MyApplication) getApplication();
        imageView = findViewById(R.id.iv_bg);
        MusicPO music = app.getMusic();
        if (music == null){
            mSongName.setText("暂无音乐");
            mSinger.setText("");
        }else{
            mSongName.setText(music.getMusic_name());
            mSinger.setText(music.getMusic_author());
        }
        if (music == null || music.getMusic_pic_path() == null || music.getMusic_pic_path().equals("")){
            Glide.with(this)
                    .load(R.mipmap.default_music_center)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,8)))
                    .into(imageView);
        }else {
            Glide.with(this)
                    .load(music.getMusic_pic_path())
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,8)))
                    .into(imageView);
        }
        if (app.isPlaying()){
            playMusicBtn.setImageResource(R.mipmap.music_stop);
        }else {
            playMusicBtn.setImageResource(R.mipmap.music_play);
        }
        initStatusBar();
    }




    //设置状态栏颜色
    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtils.setStatusBarColor(PlayingMusicActivity.this, R.color.statusTab);
        }
    }

    //本地广播监听
    class LocalReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            initView();
            mPlayMusicView.initAnim();
        }
    }

}
