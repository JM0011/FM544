package com.music.fm544.views;


import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.music.fm544.PlayingMusicActivity;
import com.music.fm544.R;

public class PlayMusicTab extends RelativeLayout {
    private boolean isPlaying;
    private ImageView play_music_img;
    private ImageView play_btn;
    private ImageView next_btn;
    private ImageView list_btn;
    private Context mContext;

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public PlayMusicTab(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.music_play_tab,this);
        mContext = context;
        play_music_img = (ImageView) findViewById(R.id.music_img);
        play_btn = (ImageView) findViewById(R.id.music_play);
        next_btn = (ImageView) findViewById(R.id.music_next);
        list_btn = (ImageView) findViewById(R.id.music_list);


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

    //判断当前播放状态，并切换播放暂停图标
    private void togger() {
        if (isPlaying){
            isPlaying = false;
            play_btn.setImageResource(R.drawable.all_play);
            Toast toast = Toast.makeText(mContext,"音乐暂停",Toast.LENGTH_SHORT);
            toast.show();
        }else {
            isPlaying = true;
            play_btn.setImageResource(R.mipmap.play_stop);
            Toast toast = Toast.makeText(mContext,"音乐播放",Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
