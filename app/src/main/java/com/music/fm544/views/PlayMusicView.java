package com.music.fm544.views;

import android.content.Context;
import android.media.MediaPlayer;
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
import com.music.fm544.R;
import com.music.fm544.helps.MediaPlayerHelp;

/**
 * Created by jm on 2019/11/24/0024.
 */

public class PlayMusicView extends FrameLayout{

    private Context myContext;
    private MediaPlayerHelp mMediaPlayerHelp;
    private boolean isPlaying;
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

        mMediaPlayerHelp = MediaPlayerHelp.getInstance(myContext);
    }


    /**
     * 切换播放状态
     */
    private void trigger(){
        if (isPlaying){
            stopMusic();
        }else{
            playMusic(mPath);
        }
    }

    /**
     * 播放音乐
     */
    public void playMusic(String path){
        mPath = path;
        isPlaying = true;
        mIvPlay.setVisibility(View.GONE);
        mFlPlayMusic.startAnimation(mPlayMusicAnim);
        mIvNeedle.startAnimation(mPlayNeedleAnim);

        if (mMediaPlayerHelp.getPath() != null && mMediaPlayerHelp.getPath().equals(path)){
            mMediaPlayerHelp.start();
        }else {
            mMediaPlayerHelp.setPath(path);
            mMediaPlayerHelp.setOnMediaPlayerHelperListener(new MediaPlayerHelp.OnMediaPlayerHelperListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mMediaPlayerHelp.start();
                }
            });
        }
    }

    /**
     * 停止播放
     */
    public void stopMusic(){
        isPlaying = false;
        mIvPlay.setVisibility(View.VISIBLE);
        mFlPlayMusic.clearAnimation();
        mIvNeedle.startAnimation(mStopNeedleAnim);

        mMediaPlayerHelp.pause();
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
}
