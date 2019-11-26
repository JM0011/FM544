package com.music.fm544.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.music.fm544.MyApplication;
import com.music.fm544.bean.MusicPO;
import com.music.fm544.helps.DateBaseHelp;
import com.music.fm544.helps.MediaPlayerHelp;

public class MusicService extends Service {

    private boolean isPlaying;
    private MediaPlayerHelp mMediaPlayerHelp;
    private MusicPO mMusic;
    private DateBaseHelp mDateBaseHelp;


    public MusicService() {
    }

    public class MusicBind extends Binder {
        /**
         * 获取当前播放的音乐对象
         */
        public MusicPO getCurrentMusic(){
            return mMusic;
        }

        /**
         * 返回音乐播放状态
         *
         * @return
         */
        public boolean getPlayingStatus() {
            return isPlaying;
        }

        /**
         * 设置音乐对象
         */
        public void setMusic(MusicPO music) {
            mMusic = music;
        }

        /**
         * 播放音乐
         */
        public void playMusic() {
            MyApplication app = (MyApplication)getApplication();
            app.setPlaying(true);
            mMusic = app.getMusic();
            if (mMediaPlayerHelp.getPath() != null && mMediaPlayerHelp.getPath().equals(mMusic.getMusic_path())) {
                mMediaPlayerHelp.start();
            } else {
                mMediaPlayerHelp.setPath(mMusic.getMusic_path());
                mMediaPlayerHelp.setOnMediaPlayerHelperListener(new MediaPlayerHelp.OnMediaPlayerHelperListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mMediaPlayerHelp.start();
                    }

                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        nextMusic();
                    }

                });
            }
        }

        /**
         * 播放下一首歌
         */
        public void nextMusic() {
            //获取下一首歌路径
            MyApplication app = (MyApplication)getApplication();
            mMusic = mDateBaseHelp.getNextMusic(mMusic);
            if (mMusic == null) {
                //列表播放完毕
                return;
            }
            isPlaying = true;
            if (mMediaPlayerHelp.getPath() == null ||
                    !mMediaPlayerHelp.getPath().equals(mMusic.getMusic_path())) {
                mMediaPlayerHelp.setPath(mMusic.getMusic_path());
                app.setMusic(mMusic);
                app.setPlaying(true);
                mMediaPlayerHelp.setOnMediaPlayerHelperListener(new MediaPlayerHelp.OnMediaPlayerHelperListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mMediaPlayerHelp.start();
                    }

                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        nextMusic();
                    }
                });
            }

        }

        /**
         * 暂停播放
         */
        public void stopMusic() {
            MyApplication app = (MyApplication)getApplication();
            app.setPlaying(false);
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
        mDateBaseHelp = new DateBaseHelp();
        mMediaPlayerHelp = MediaPlayerHelp.getInstance(this);
        isPlaying = false;
//        //初始化进入程序，首次启动service的音乐播放路径
//        //从数据库中读取相应的播放音乐
//        Log.i(TAG, "onCreate: Service"+mDateBaseHelp.getCurrentMusic().getMusic_path());
//        System.out.println(mDateBaseHelp.getCurrentMusic().getMusic_path());
//        mMediaPlayerHelp.setPath(mDateBaseHelp.getCurrentMusic().getMusic_path());

    }


    @Override
    public void onDestroy() {

        //调用数据库方法，保存最后一次播放的音乐----------未实现

        super.onDestroy();
    }
}
