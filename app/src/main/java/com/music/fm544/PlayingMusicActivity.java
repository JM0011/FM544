package com.music.fm544;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.music.fm544.views.PlayMusicView;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class PlayingMusicActivity extends AppCompatActivity {
    private static final String TAG = "play";
    private ImageView imageView;
    private PlayMusicView mPlayMusicView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_music);

        initView();

        ImageView back =  (ImageView) this.findViewById(R.id.back_btn);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void initView(){
        imageView = findViewById(R.id.iv_bg);
        Glide.with(this)
                .load(R.mipmap.song)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,8)))
                .into(imageView);
        mPlayMusicView = findViewById(R.id.play_music_view);
        mPlayMusicView.setMusicIcon(R.mipmap.song);
//        mPlayMusicView.playMusic("http://res.lgdsunday.club/Nostalgic%20Piano.mp3");
//        mPlayMusicView.playMusic("http://www.170mv.com/kw/antiserver.kuwo.cn/anti.s?rid=MUSIC_11736829&response=res&format=mp3|aac&type=convert_url&br=128kmp3&agent=iPhone&callback=getlink&jpcallback=getlink.mp3");
        String url = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/song.mp3";
        mPlayMusicView.playMusic(url);
    }
}
