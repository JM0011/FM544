package com.music.fm544;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.music.fm544.Bean.MusicDetail;
import com.music.fm544.Bean.MusicPO;
import com.music.fm544.Utils.LocalAudioUtils;
import com.music.fm544.Utils.StatusBarUtils;

import java.io.File;
import java.text.DecimalFormat;

public class MusicDetailActivity extends AppCompatActivity {
    private MusicPO mMusic;

    private ImageView backBtn;
    private Button deleteBtn;
    private Button okBtn;

    private ImageView songImag;
    private TextView songName;
    private TextView singName;
    private TextView albumName;
    private TextView musicSize;
    private TextView songLength;
    private TextView songBits;
    private TextView musicPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail);

        mMusic = (MusicPO) getIntent().getSerializableExtra("musicDetail");

        songImag = this.findViewById(R.id.song_imag);
        songName = this.findViewById(R.id.song_name);
        singName = this.findViewById(R.id.singer_name);
        albumName = this.findViewById(R.id.album_name);
        musicSize = this.findViewById(R.id.size_value);
        songLength = this.findViewById(R.id.length_value);
        songBits = this.findViewById(R.id.bits_value);
        musicPath = this.findViewById(R.id.path_value);

        backBtn = this.findViewById(R.id.back_btn);
//        okBtn = this.findViewById(R.id.ok_btn);
//        deleteBtn = this.findViewById(R.id.delete_btn);


        initStatusBar();

        initView();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

//        okBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });

//        deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });

    }

    //使用文件管理器打开音乐路径（未实现）
    private void openExternalFile(MusicPO music) {
        File file = new File(music.getMusic_path());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
     try {
//        startActivity(intent);
            startActivity(Intent.createChooser(intent,"选择浏览工具"));
    } catch(
    ActivityNotFoundException e)
    {
        e.printStackTrace();
    }

}

    private void initView() {
        LocalAudioUtils localAudioUtils = new LocalAudioUtils(this);
        if (mMusic != null) {
            MusicDetail musicDetail = localAudioUtils.getMusicMessage(mMusic);
            if (musicDetail != null) {
                if (musicDetail == null || musicDetail.getMusic_pic_path() == null || musicDetail.getMusic_pic_path().equals("")) {
                    Glide.with(this)
                            .load(R.mipmap.default_music_center)
                            .into(songImag);
                } else {
                    Glide.with(this)
                            .load(musicDetail.getMusic_pic_path())
                            .into(songImag);
                }
            }
            songName.setText(musicDetail.getMusic_name());
            singName.setText(musicDetail.getMusic_author());
            albumName.setText(musicDetail.getMusic_album());
            String sizeMsg = "";
            String timeMsg = "";
            String bitMsg = "";
            if (musicDetail.getMusic_size() != null && musicDetail.getMusic_size() > 0){
                DecimalFormat decimalFormat=new DecimalFormat(".00");
                if (musicDetail.getMusic_size()/1024 > 1){
                    if ((musicDetail.getMusic_size()/1024)/1024 >1){
                        double mb = (musicDetail.getMusic_size()/1024.00)/1024.00;
                        sizeMsg = decimalFormat.format(mb)+"MB";
                    }else {
                        double kb = musicDetail.getMusic_size()/1024.00;
                        sizeMsg = decimalFormat.format(kb)+"KB";
                    }
                }else {
                    sizeMsg = musicDetail.getMusic_size() + "B";
                }
            }
            if (musicDetail.getMusic_time() > 0 && musicDetail.getMusic_time() != null){
                if (musicDetail.getMusic_time()/1000 > 1){
                    int s = musicDetail.getMusic_time()/1000;
                    if (s/60 > 1){
                        int m = s/60;
                        if (m/60 > 1 ){
                            timeMsg = m/60+":"+ m%60 + ":" + s%60;
                        }else {
                            timeMsg = m + ":" + s%60;
                        }
                    }else{
                        timeMsg = s + "s";
                    }
                }else {
                    timeMsg = musicDetail.getMusic_time() + "ms";
                }
            }
            if (musicDetail.getMusic_size() != null && musicDetail.getMusic_size() > 0 && musicDetail.getMusic_time() > 0 && musicDetail.getMusic_time() != null){
                int bits = musicDetail.getMusic_size()/1024 * 8 / (musicDetail.getMusic_time()/1000);
                bitMsg = bits + "kbps";
            }
            musicSize.setText(sizeMsg);
            songLength.setText(timeMsg);
            songBits.setText(bitMsg);
            musicPath.setText(musicDetail.getMusic_path());

        }
    }


    //设置状态栏颜色
    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtils.setStatusBarColor(MusicDetailActivity.this, R.color.statusTab);
        }
    }
}
