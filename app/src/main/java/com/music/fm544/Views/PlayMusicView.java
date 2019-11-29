package com.music.fm544.Views;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.music.fm544.Bean.MusicPO;
import com.music.fm544.MyApplication;
import com.music.fm544.R;
import com.music.fm544.Service.MusicService;


public class PlayMusicView extends FrameLayout{

    private Context myContext;
    private Intent mServiceIntent;
    private MusicService.MusicBind mMusicBind;
    private MusicPO mMusic;
    private boolean isBindService;

    private View myView;
    private FrameLayout mFlPlayMusic;
    private ImageView mIvNeedle,mIvIcon,mIvPlay;
    private Animation mPlayMusicAnim,mPlayNeedleAnim,mStopNeedleAnim;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mMusicBind = (MusicService.MusicBind) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };


    public PlayMusicView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){

        myContext = context;
        myView = LayoutInflater.from(myContext).inflate(R.layout.play_music,this,false);
        mIvIcon = myView.findViewById(R.id.iv_icon);
        mFlPlayMusic = myView.findViewById(R.id.fl_play_music);
        mIvNeedle = myView.findViewById(R.id.iv_needle);
        mIvPlay = myView.findViewById(R.id.iv_play);


        mPlayMusicAnim = AnimationUtils.loadAnimation(myContext,R.anim.play_music_anim);
        mPlayNeedleAnim = AnimationUtils.loadAnimation(myContext,R.anim.play_needle_anim);
        mStopNeedleAnim = AnimationUtils.loadAnimation(myContext,R.anim.stop_needle_anim);
        mFlPlayMusic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                trigger();
            }
        });

        addView(myView);
        initAnim();
    }


    public void initAnim(){
        MyApplication app = (MyApplication) myContext.getApplicationContext();
        mServiceIntent = app.getServiceIntent();
        MusicPO music = app.getMusic();
        if (music == null || music.getMusic_pic_path() == null || music.getMusic_pic_path().equals("")){
            Glide.with(myContext)
                    .load(R.mipmap.default_music_center)
                    .into(mIvIcon);

        }else {
            Glide.with(myContext)
                    .load(music.getMusic_pic_path())
                    .into(mIvIcon);
        }
        //绑定service
        if (!isBindService){
            isBindService = true;
            myContext.bindService(mServiceIntent,conn,Context.BIND_AUTO_CREATE);
        }
        if (app.isPlaying()){
            mIvPlay.setVisibility(View.GONE);
            mFlPlayMusic.startAnimation(mPlayMusicAnim);
            mIvNeedle.startAnimation(mPlayNeedleAnim);
        }else{
            mIvPlay.setVisibility(View.VISIBLE);
            mFlPlayMusic.clearAnimation();
            mIvNeedle.startAnimation(mStopNeedleAnim);
        }

    }

    /**
     * 切换播放状态
     */
    private void trigger(){
        MyApplication app = (MyApplication) myContext.getApplicationContext();
        if (app.getMusic()!=null){
            if (app.isPlaying()){
                stopMusic();
            }else{
                playMusic();
            }
        }
    }

    /**
     * 播放音乐
     */
    public void playMusic(){
        mIvPlay.setVisibility(View.GONE);
        mFlPlayMusic.startAnimation(mPlayMusicAnim);
        mIvNeedle.startAnimation(mPlayNeedleAnim);

        mMusicBind.playMusic();


    }

    /**
     * 停止播放
     */
    public void stopMusic(){
        mIvPlay.setVisibility(View.VISIBLE);
        mFlPlayMusic.clearAnimation();
        mIvNeedle.startAnimation(mStopNeedleAnim);

//        mMediaPlayerHelp.pause();
        //暂停音乐服务
        if (mMusicBind != null){
            mMusicBind.stopMusic();
        }
    }


    /**
     * 设置光盘中显示得音乐的封面图片
     */
    public void setMusicIcon(String url){
//        String url = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/song.jpg";
//        Glide.with(myContext)
//                .load(url)
//                .into(mIvIcon);
        if (url == null || url.equals("")){
            Glide.with(myContext)
                    .load(R.mipmap.default_music_center)
                    .into(mIvIcon);
        }else {
            Glide.with(myContext)
                    .load(url)
                    .into(mIvIcon);
        }

    }

    /**
     *设置播放音乐对象
     */
    public void setMusic(MusicPO music){
        mMusic = music;
    }


    /**
     * 取消绑定
     */
    public void destory (){
        if (isBindService){
            isBindService = false;
            myContext.unbindService(conn);
        }
    }



}
