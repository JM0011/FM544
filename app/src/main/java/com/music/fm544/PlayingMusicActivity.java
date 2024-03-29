package com.music.fm544;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.music.fm544.Bean.MusicPO;
import com.music.fm544.Utils.StatusBarUtils;
import com.music.fm544.Views.PlayMusicView;

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




        ImageView back =  (ImageView) this.findViewById(R.id.back_btn);
        mSongName = (TextView) this.findViewById(R.id.songName);
        mSinger = (TextView) this.findViewById(R.id.singer);
        mPlayMusicView = (PlayMusicView) this.findViewById(R.id.play_music_view);

        initStatusBar();
        initView();

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayMusicView.destory();
    }

    private void initView(){
//        MusicPO music = new MusicPO();
//        music.setMusic_author("赵雷");
//        music.setMusic_name("成都");
//        music.setMusic_path(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/song1.mp3");
        MyApplication app = (MyApplication) getApplication();
        MusicPO music = app.getMusic();
        mSongName.setText(music.getMusic_name());
        mSinger.setText(music.getMusic_author());
        imageView = findViewById(R.id.iv_bg);
        if (music.getMusic_pic_path() == null || music.getMusic_pic_path().equals("")){
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
//        Glide.with(this)
//                .load(music.getMusic_pic_path())
//                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,8)))
//                .into(imageView);

//        String url = "/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1574831377660";

//        Glide.with(this)
//                .load(url)
//                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,8)))
//                .into(imageView);



//        mPlayMusicView = findViewById(R.id.play_music_view);
//        mPlayMusicView.setMusicIcon(imgUrl);
//        mPlayMusicView.playMusic("http://res.lgdsunday.club/Nostalgic%20Piano.mp3");
//        mPlayMusicView.playMusic("http://www.170mv.com/kw/antiserver.kuwo.cn/anti.s?rid=MUSIC_11736829&response=res&format=mp3|aac&type=convert_url&br=128kmp3&agent=iPhone&callback=getlink&jpcallback=getlink.mp3");
//        String url = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/song1.mp3";
//        mPlayMusicView.setMusic(music);
//        mPlayMusicView.playMusic();
//        mPlayMusicView.initAnim();
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
