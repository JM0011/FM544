package com.music.fm544.views;

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
import com.music.fm544.R;
import com.music.fm544.bean.MusicPO;
import com.music.fm544.helps.MediaPlayerHelp;
import com.music.fm544.service.MusicService;


public class PlayMusicView extends FrameLayout{

    private Context myContext;
    private Intent mServiceIntent;
    private MusicService.MusicBind mMusicBind;
    private MusicPO mMusic;

    private MediaPlayerHelp mMediaPlayerHelp;
    private boolean isPlaying,isBindService;
    private String mPath;
    private View myView;
    private FrameLayout mFlPlayMusic;
    private ImageView mIvNeedle,mIvIcon,mIvPlay;
    private Animation mPlayMusicAnim,mPlayNeedleAnim,mStopNeedleAnim;

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
        mFlPlayMusic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                trigger();
            }
        });
        mIvNeedle = myView.findViewById(R.id.iv_needle);
        mIvPlay = myView.findViewById(R.id.iv_play);
        mPlayMusicAnim = AnimationUtils.loadAnimation(myContext,R.anim.play_music_anim);
        mPlayNeedleAnim = AnimationUtils.loadAnimation(myContext,R.anim.play_needle_anim);
        mStopNeedleAnim = AnimationUtils.loadAnimation(myContext,R.anim.stop_needle_anim);


        addView(myView);

//        mMediaPlayerHelp = MediaPlayerHelp.getInstance(myContext);
    }


    /**
     * 切换播放状态
     */
    private void trigger(){
        if (isPlaying){
            stopMusic();
        }else{
            playMusic();
        }
    }

    /**
     * 播放音乐
     */
    public void playMusic(){
        isPlaying = true;
        mIvPlay.setVisibility(View.GONE);
        mFlPlayMusic.startAnimation(mPlayMusicAnim);
        mIvNeedle.startAnimation(mPlayNeedleAnim);

//        if (mMediaPlayerHelp.getPath() != null && mMediaPlayerHelp.getPath().equals(path)){
//            mMediaPlayerHelp.start();
//        }else {
//            mMediaPlayerHelp.setPath(path);
//            mMediaPlayerHelp.setOnMediaPlayerHelperListener(new MediaPlayerHelp.OnMediaPlayerHelperListener() {
//                @Override
//                public void onPrepared(MediaPlayer mediaPlayer) {
//                    mMediaPlayerHelp.start();
//                }
//            });
//        }
        //启动音乐服务
        startMusicService();
    }

    /**
     * 停止播放
     */
    public void stopMusic(){
        isPlaying = false;
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
     * @param icon
     */
    public void setMusicIcon(int icon){
        Glide.with(myContext)
                .load(icon)
                .into(mIvIcon);

    }

    /**
     *设置播放音乐对象
     */
    public void setMusic(MusicPO music){
        mMusic = music;
    }

    /**
     * 启动音乐服务
     */
    private void startMusicService(){
        //启动service
        if (mServiceIntent == null){
            mServiceIntent = new Intent(myContext, MusicService.class);
            myContext.startService(mServiceIntent);
        }
        else {
            mMusicBind.playMusic();
        }

        //绑定service
        if (!isBindService){
            isBindService = true;
            myContext.bindService(mServiceIntent,conn,Context.BIND_AUTO_CREATE);
        }

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


    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mMusicBind = (MusicService.MusicBind) service;
            mMusicBind.setMusic(mMusic);
            mMusicBind = (MusicService.MusicBind) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };
}
