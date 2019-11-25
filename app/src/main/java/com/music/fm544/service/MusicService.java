package com.music.fm544.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.music.fm544.bean.MusicPO;
import com.music.fm544.helps.MediaPlayerHelp;

public class MusicService extends Service {

    private MediaPlayerHelp mMediaPlayerHelp;
    private MusicPO mMusic;

    public MusicService() {
    }

    public  class MusicBind extends Binder{
        /**
         *设置音乐对象
         */
        public void setMusic(MusicPO music){
            mMusic = music;
        }
        /**
         * 播放音乐
         */
        public void playMusic(){
            if (mMediaPlayerHelp.getPath() != null && mMediaPlayerHelp.getPath().equals(mMusic.getMusic_path())){
                mMediaPlayerHelp.start();
            }else {
                mMediaPlayerHelp.setPath(mMusic.getMusic_path());
                mMediaPlayerHelp.setOnMediaPlayerHelperListener(new MediaPlayerHelp.OnMediaPlayerHelperListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mMediaPlayerHelp.start();
                    }

                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        stopSelf();
                    }

                });
            }
        }
        /**
         * 暂停播放
         */
        public void stopMusic(){
            mMediaPlayerHelp.pause();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return new MusicBind();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mMediaPlayerHelp = MediaPlayerHelp.getInstance(this);
    }


    /**
     * 设置通知栏音乐
     * Notification
     */
//    private void startForeGround(){
//        //创建Notification
//        Notification notification = new Notification.Builder(this,)
//    }

}
