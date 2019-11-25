package com.music.fm544;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.music.fm544.utils.StatusBarUtils;
import com.music.fm544.views.PlayMusicView;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class PlayingMusicActivity extends AppCompatActivity {
    private ImageView imageView;
    private PlayMusicView mPlayMusicView;
    private TextView mSongName;
    private TextView mSinger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_music);
        Intent intent = getIntent();
        initStatusBar();
        initView();



        ImageView back =  (ImageView) this.findViewById(R.id.back_btn);
        mSongName = (TextView) this.findViewById(R.id.songName);
        mSinger = (TextView) this.findViewById(R.id.singer);

        mSongName.setText(intent.getStringExtra("songName"));
        mSinger.setText(intent.getStringExtra("songer"));

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }


    private void initView(){
        int imgUrl = R.drawable.song2;
        imageView = findViewById(R.id.iv_bg);
        Glide.with(this)
                .load(imgUrl)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,8)))
                .into(imageView);
        mPlayMusicView = findViewById(R.id.play_music_view);
        mPlayMusicView.setMusicIcon(imgUrl);
//        mPlayMusicView.playMusic("http://res.lgdsunday.club/Nostalgic%20Piano.mp3");
//        mPlayMusicView.playMusic("http://www.170mv.com/kw/antiserver.kuwo.cn/anti.s?rid=MUSIC_11736829&response=res&format=mp3|aac&type=convert_url&br=128kmp3&agent=iPhone&callback=getlink&jpcallback=getlink.mp3");
        String url = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/song.mp3";
        mPlayMusicView.playMusic(url);
    }

    //设置状态栏颜色
    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtils.setStatusBarColor(PlayingMusicActivity.this, R.color.statusTab);
        }
    }
}
