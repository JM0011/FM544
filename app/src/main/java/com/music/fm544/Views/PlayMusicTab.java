package com.music.fm544.Views;


import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.music.fm544.Bean.MusicPO;
import com.music.fm544.Dialog.AddPlaylistDialog;
import com.music.fm544.MyApplication;
import com.music.fm544.PlayingMusicActivity;
import com.music.fm544.R;
import com.music.fm544.Service.MusicService;

public class PlayMusicTab extends RelativeLayout {
    //音乐服务
    private Intent mServiceIntent;
    private MusicService.MusicBind mMusicBind;
    private boolean isBindService;

    //本地广播
    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;
    private LocalReciver localReciver;

    //基本控件
    private ImageView play_music_img;
    private ImageView play_btn;
    private ImageView next_btn;
    private ImageView list_btn;
    private TextView song_txt;
    private TextView singer_txt;
    private Context mContext;
    private FragmentManager fragmentManager;

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

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
        song_txt = (TextView) findViewById(R.id.music_name);
        singer_txt = (TextView) findViewById(R.id.music_singer);

        //注册本地广播监听器
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.fm544.broadcast.REFRESH_MUSIC");
        localReciver = new LocalReciver();
        localBroadcastManager.registerReceiver(localReciver, intentFilter);

        initView();

        play_music_img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPlayActivity();
            }
        });

        song_txt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPlayActivity();
            }
        });

        singer_txt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPlayActivity();
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
                MyApplication app = (MyApplication)mContext.getApplicationContext();
                if (app.getPlayListSize() != 0){
                    if (mMusicBind != null){
                        mMusicBind.nextMusic();
                    }
                    resetPlayTabStatus(app.getMusic());

                    play_btn.setImageResource(R.mipmap.play_stop);
                    Toast toast = Toast.makeText(mContext,"下一首",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        list_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPlaylistDialog playList = new AddPlaylistDialog();
                playList.show(fragmentManager,"playList");
//                Toast toast = Toast.makeText(mContext,"显示列表",Toast.LENGTH_SHORT);
//                toast.show();
            }
        });
    }

    private void gotoPlayActivity(){
        Intent i = new Intent(mContext,PlayingMusicActivity.class);
        mContext.startActivity(i);
    }

    public void initView() {
        MyApplication app = (MyApplication) mContext.getApplicationContext();
        MusicPO music = app.getMusic();
        if (app.isPlaying()){
            play_btn.setImageResource(R.mipmap.play_stop);
        }else{
            play_btn.setImageResource(R.drawable.all_play);
        }
        if (music == null || music.getMusic_pic_path() == null || music.getMusic_pic_path().equals("")){
            Glide.with(mContext)
                    .load(R.mipmap.default_music_img)
                    .into(play_music_img);
        }else {
            Glide.with(mContext)
                    .load(music.getMusic_pic_path())
                    .into(play_music_img);
        }
        if (music == null){
            song_txt.setText("暂无歌曲");
            singer_txt.setText("");
        }else {
            song_txt.setText(music.getMusic_name());
            singer_txt.setText(music.getMusic_author());
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
            if (app.getMusic()!=null){
                play_btn.setImageResource(R.mipmap.play_stop);
                mMusicBind.playMusic();

            }else{
                Toast toast = Toast.makeText(mContext,"暂无可播放音乐",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    //修改PlayingTab信息状态
    public void resetPlayTabStatus(MusicPO music){
        play_btn.setImageResource(R.mipmap.play_stop);

        if (music.getMusic_pic_path() == null || music.getMusic_pic_path().equals("")){
            Glide.with(mContext)
                    .load(R.mipmap.default_music_img)
                    .into(play_music_img);
        }else {
            Glide.with(mContext)
                    .load(music.getMusic_pic_path())
                    .into(play_music_img);
        }
//        play_music_img.setImageResource(R.drawable.icon_logo);
        song_txt.setText(music.getMusic_name());
        singer_txt.setText(music.getMusic_author());
    }

    /**
     * 取消绑定
     */
    public void destory (){
        if (isBindService){
            isBindService = false;
            mContext.unbindService(conn);
        }
        localBroadcastManager.unregisterReceiver(localReciver);
    }

    //本地广播监听
    class LocalReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            initView();
        }
    }
}
