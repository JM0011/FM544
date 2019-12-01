package com.music.fm544.Views;

import android.content.Context;
import android.os.Build;
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


public class PlayMusicView extends FrameLayout{

    private Context myContext;

    private View myView;
    private FrameLayout mFlPlayMusic;
    private ImageView mIvNeedle,mIvIcon;
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
        mIvNeedle = myView.findViewById(R.id.iv_needle);


        mPlayMusicAnim = AnimationUtils.loadAnimation(myContext,R.anim.play_music_anim);
        mPlayNeedleAnim = AnimationUtils.loadAnimation(myContext,R.anim.play_needle_anim);
        mStopNeedleAnim = AnimationUtils.loadAnimation(myContext,R.anim.stop_needle_anim);


        addView(myView);
        initAnim();
    }


    public void initAnim(){
        MyApplication app = (MyApplication) myContext.getApplicationContext();
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
        if (app.isPlaying()){
            mFlPlayMusic.startAnimation(mPlayMusicAnim);
            mIvNeedle.startAnimation(mPlayNeedleAnim);
        }else{
            mFlPlayMusic.clearAnimation();
            mIvNeedle.startAnimation(mStopNeedleAnim);
        }

    }


    /**
     * 播放音乐
     */
    public void playMusic(){
        mFlPlayMusic.startAnimation(mPlayMusicAnim);
        mIvNeedle.startAnimation(mPlayNeedleAnim);

    }

    /**
     * 停止播放
     */
    public void stopMusic(){
        mFlPlayMusic.clearAnimation();
        mIvNeedle.startAnimation(mStopNeedleAnim);

    }


    /**
     * 设置光盘中显示得音乐的封面图片
     */
    public void setMusicIcon(MusicPO music){

        if (music.getMusic_pic_path() == null || music.getMusic_pic_path().equals("")){
            Glide.with(myContext)
                    .load(R.mipmap.default_music_center)
                    .into(mIvIcon);
        }else {
            Glide.with(myContext)
                    .load(music.getMusic_pic_path())
                    .into(mIvIcon);
        }

    }







}
