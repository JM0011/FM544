package com.music.fm544;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.music.fm544.Bean.MusicPO;
import com.music.fm544.Service.MusicService;
import com.music.fm544.Utils.FormatUtils;
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
    private SeekBar mSeekBar;
    private ImageView mLikeStatus;
    private ImageView mDetailMusic;
    private TextView mProgressText;
    private TextView mLengthText;

    //进度更新线程
    private SeekBarUpdate seekBarUpdate;

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
        mSeekBar = (SeekBar) this.findViewById(R.id.seek_bar);
        mLikeStatus = (ImageView) findViewById(R.id.like_status);
        mDetailMusic = (ImageView) findViewById(R.id.more_detail);
        mLengthText = (TextView) findViewById(R.id.length_text);
        mProgressText = (TextView) findViewById(R.id.progress_text);

        initView();

        //注册本地广播监听器
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.fm544.broadcast.REFRESH_MUSIC");
        localReciver = new LocalReciver();
        localBroadcastManager.registerReceiver(localReciver, intentFilter);



        mLikeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLikeMusic();
            }
        });

        mDetailMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication app = (MyApplication) getApplication();
                MusicPO musicPO = app.getMusic();
                if (musicPO != null){
                    gotoMusicDetail(musicPO);
                }else {
                    Toast.makeText(getApplication(),"暂无音乐",Toast.LENGTH_SHORT).show();
                }
            }
        });

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

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean user) {
                MyApplication app = (MyApplication) getApplication();
                if (user && mMusicBind != null){
                    mMusicBind.seekTo(i*1000);
                    if (!app.isPlaying() && app.getMusic()!=null){
                        mMusicBind.playMusic();
                        initView();
                        mPlayMusicView.initAnim();
                    }
                }
                String progress = FormatUtils.formatTime(i*1000);
                mProgressText.setText(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    //前往音乐详情界面
    private void gotoMusicDetail(MusicPO music) {
        Intent intent = new Intent(this, MusicDetailActivity.class);
        intent.putExtra("musicDetail", music);
        startActivity(intent);
    }

    //更新进度条线程
    private class SeekBarUpdate extends AsyncTask<Integer,Integer,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            MyApplication app = (MyApplication) getApplication();
            MusicPO musicPO = app.getMusic();
            mSeekBar.setMax(musicPO.getMusic_time()/1000);
            Integer postion = mMusicBind.getCurrentPostion();
            mSeekBar.setProgress(postion/1000);
            mProgressText.setText(FormatUtils.formatTime(postion));
        }

        @Override
        protected String doInBackground(Integer... integers) {
            while (true){
                if (isCancelled()){
                    return "";
                }
                publishProgress();
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
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


    //TODO 暂停后线程消亡，重新播放后不能再次启动
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
    protected void onStart() {
        super.onStart();
        //更新seekBar
        MyApplication app = (MyApplication)getApplication();
        if (app.getMusic() != null){
            mSeekBar.setMax(app.getMusic().getMusic_time()/1000);
            seekBarUpdate = new SeekBarUpdate();
            seekBarUpdate.execute();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (seekBarUpdate != null){
            seekBarUpdate.cancel(true);
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

        if (music != null){
            if (music.getMusic_like_status() == 0){
                mLikeStatus.setImageResource(R.mipmap.not_like_status);
            }else {
                mLikeStatus.setImageResource(R.mipmap.like_status);
            }
            String time = FormatUtils.formatTime(music.getMusic_time());
            mLengthText.setText(time);
        }
//        initStatusBar();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }




    //设置喜爱歌曲
    private void setLikeMusic() {
        MyApplication app = (MyApplication) getApplication();
        MusicPO musicPO = app.getMusic();
        if (musicPO == null){
            return;
        }
        app.setMusicLikeStatus(musicPO);
        if (musicPO.getMusic_like_status() == 0){
            mLikeStatus.setImageResource(R.mipmap.not_like_status);
        }else {
            mLikeStatus.setImageResource(R.mipmap.like_status);
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
