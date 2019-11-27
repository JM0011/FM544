package com.music.fm544;

import android.app.Application;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Environment;

import com.music.fm544.Bean.MusicPO;
import com.music.fm544.Helps.DatabaseHelper;
import com.music.fm544.Helps.DateBaseHelp;
import com.music.fm544.Service.MusicService;
import com.music.fm544.Utils.LocalAudioUtils;

public class MyApplication extends Application{
    private boolean isPlaying;
    private MusicPO music;
    private DateBaseHelp mDateBaseHelp;
    private Intent serviceIntent;
    private DatabaseHelper datebaseHelper;

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


//        测试本地音乐扫描
        MediaScannerConnection.scanFile(this, new String[] { Environment
                .getExternalStorageDirectory().getAbsolutePath() }, null, null);

        LocalAudioUtils localAudioUtils = new LocalAudioUtils(this);
        localAudioUtils.getAllSongs(this);

    }
}
