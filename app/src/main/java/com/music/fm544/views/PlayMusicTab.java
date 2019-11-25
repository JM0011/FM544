package com.music.fm544.views;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.music.fm544.MyApplication;
import com.music.fm544.PlayingMusicActivity;
import com.music.fm544.R;
import com.music.fm544.service.MusicService;

public class PlayMusicTab extends RelativeLayout {
    private Intent mServiceIntent;
    private MusicService.MusicBind mMusicBind;
    private boolean isBindService;

    private ImageView play_music_img;
    private ImageView play_btn;
    private ImageView next_btn;
    private ImageView list_btn;
    private Context mContext;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mMusicBind = (MusicService.MusicBind) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };


    public PlayMusicTab(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.music_play_tab,this);
        mContext = context;
        play_music_img = (ImageView) findViewById(R.id.music_img);
        play_btn = (ImageView) findViewById(R.id.music_play);
        next_btn = (ImageView) findViewById(R.id.music_next);
        list_btn = (ImageView) findViewById(R.id.music_list);

        initView();

        play_music_img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,PlayingMusicActivity.class);
                context.startActivity(i);
            }
        });

        play_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                togger();
            }
        });

        next_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMusicBind != null){
                    mMusicBind.nextMusic();
                }
                play_btn.setImageResource(R.mipmap.play_stop);
                Toast toast = Toast.makeText(mContext,"下一首",Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        list_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(mContext,"显示列表",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void initView() {
        MyApplication app = (MyApplication) mContext.getApplicationContext();
        if (app.isPlaying()){
            play_btn.setImageResource(R.mipmap.play_stop);
        }else{
            play_btn.setImageResource(R.drawable.all_play);
        }
        mServiceIntent = app.getServiceIntent();
        //绑定service
        if (!isBindService){
            isBindService = true;
            mContext.bindService(mServiceIntent,conn,Context.BIND_AUTO_CREATE);
        }

    }

    //判断当前播放状态，并切换播放暂停图标
    private void togger() {
        MyApplication app = (MyApplication) mContext.getApplicationContext();
        if (app.isPlaying()){
            play_btn.setImageResource(R.drawable.all_play);
            if (mMusicBind != null){
                mMusicBind.stopMusic();
            }
            Toast toast = Toast.makeText(mContext,"音乐暂停",Toast.LENGTH_SHORT);
            toast.show();
        }else {
            play_btn.setImageResource(R.mipmap.play_stop);
            mMusicBind.playMusic();
            Toast toast = Toast.makeText(mContext,"音乐播放",Toast.LENGTH_SHORT);
            toast.show();
        }
    }




    /**
     * 取消绑定
     */
    public void destory (){
        if (isBindService){
            isBindService = false;
            mContext.unbindService(conn);
        }
    }
}