package com.music.fm544.Helps;


import android.os.Environment;

import com.music.fm544.Bean.MusicPO;

public class DateBaseHelp {


    public MusicPO getCurrentMusic(){
        MusicPO music = new MusicPO();
        music.setMusic_author("赵雷");
        music.setMusic_name("成都");
        music.setMusic_pic_path(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/song.jpg");
        music.setMusic_path(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/song1.mp3");
        return music;
    }

    public MusicPO getNextMusic(MusicPO musicPO){
        MusicPO music = new MusicPO();
        music.setMusic_author("赵雷");
        music.setMusic_name("成都");
        if (musicPO == null || musicPO.getMusic_path().equals(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/song.mp3")){
            music.setMusic_pic_path(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/song.jpg");
            music.setMusic_path(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/song1.mp3");
        }else {
            music.setMusic_pic_path(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/song1.jpg");
            music.setMusic_path(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/song.mp3");
        }
        return music;
    }


}
