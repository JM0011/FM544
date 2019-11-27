package com.music.fm544;

import android.app.Application;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Environment;

import com.music.fm544.Bean.MusicPO;
import com.music.fm544.Helps.DatabaseHelper;
import com.music.fm544.Helps.DateBaseHelp;
import com.music.fm544.Helps.MusicDao;
import com.music.fm544.Service.MusicService;
import com.music.fm544.Utils.LocalAudioUtils;

public class MyApplication extends Application{
    private boolean isPlaying;
    private MusicPO music;
    private DateBaseHelp mDateBaseHelp;
    private Intent serviceIntent;
    private DatabaseHelper datebaseHelper;

    public DatabaseHelper getDatebaseHelper() {
        return datebaseHelper;
    }

    public Intent getServiceIntent() {
        return serviceIntent;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public MusicPO getMusic() {
        return music;
    }

    public void setMusic(MusicPO music) {
        this.music = music;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        datebaseHelper = new DatabaseHelper(this,"music.db",null,1);
        datebaseHelper.getWritableDatabase();


        mDateBaseHelp = new DateBaseHelp();
        isPlaying = false;
        music = mDateBaseHelp.getCurrentMusic();
        if (serviceIntent == null){
            serviceIntent = new Intent(this, MusicService.class);
            this.startService(serviceIntent);
        }



        //测试Musicdao
        MusicPO music1 = new MusicPO(0,"荼蘼未了","热门华语268","樱九",218570,"/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1574831373551",
                "/storage/emulated/0/Music/music/荼蘼未了.mp3",0);
        MusicPO music2 = new MusicPO(0,"为龙","唱给你的歌","河图",247879,"/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1574831351858",
                "/storage/emulated/0/Music/music/河图 - 为龙.mp3",0);
        MusicPO music3 = new MusicPO(0,"夜空中最亮的星","世界","逃跑计划",252268,"/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1574831381793",
                "/storage/emulated/0/Music/music/夜空中最亮的星.mp3",0);
        MusicDao musicDao = new MusicDao(datebaseHelper,this);

        music = music1;
//        musicDao.create_music_table_row(music1);
//        musicDao.create_music_table_row(music2);
//        musicDao.create_music_table_row(music3);

        musicDao.create_play_table_row(music1);
        musicDao.create_play_table_row(music2);
        musicDao.create_play_table_row(music3);

//        System.out.println(musicDao.search_music_table("夜空中最亮的星").toString());

        musicDao.set_like_status("夜空中最亮的星");
//        musicDao.cancel_like("夜空中最亮的星");

//        musicDao.add_already_played(music1);

//        musicDao.add_is_playing(music1);

//        测试本地音乐扫描
        MediaScannerConnection.scanFile(this, new String[] { Environment
                .getExternalStorageDirectory().getAbsolutePath() }, null, null);

        LocalAudioUtils localAudioUtils = new LocalAudioUtils(this);
        musicDao.init_music_table(localAudioUtils.getAllSongs(this));

//        musicDao.set_like_status("夜空中最亮的星");
//        musicDao.set_like_status("荼蘼未了");

    }
}
