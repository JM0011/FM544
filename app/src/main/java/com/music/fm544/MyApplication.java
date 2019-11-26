package com.music.fm544;

import android.app.Application;
import android.content.Intent;

import com.music.fm544.Bean.MusicPO;
import com.music.fm544.Helps.DateBaseHelp;
import com.music.fm544.Service.MusicService;

public class MyApplication extends Application{
    private boolean isPlaying;
    private MusicPO music;
    private DateBaseHelp mDateBaseHelp;
    private Intent serviceIntent;

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
        mDateBaseHelp = new DateBaseHelp();
        isPlaying = false;
        music = mDateBaseHelp.getCurrentMusic();
        if (serviceIntent == null){
            serviceIntent = new Intent(this, MusicService.class);
            this.startService(serviceIntent);
        }
    }
}
