package com.music.fm544.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.music.fm544.Bean.MusicListItem;
import com.music.fm544.MyApplication;
import com.music.fm544.Bean.MusicPO;
import com.music.fm544.Helps.DateBaseHelp;
import com.music.fm544.Helps.MediaPlayerHelp;

public class MusicService extends Service {

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
            if (mMusic == null ){
                return;
            }
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
            MusicPO musicPO = app.getMusic();
            mMusic = app.getNextMusic();
            if (mMusic == null) {
                //列表播放完毕
                return;
            }
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

//        /**
//         * 插入立即播放
//         */
        public void insertMusic(MusicPO music){
            MyApplication app = (MyApplication)getApplication();
            if (music == null) {
                //列表播放完毕
                return;
            }
            mMusic = music;
            if (mMediaPlayerHelp.getPath() == null ||
                    !mMediaPlayerHelp.getPath().equals(mMusic.getMusic_path())) {
                mMediaPlayerHelp.setPath(mMusic.getMusic_path());
                app.setMusic(mMusic);
                if (app.findMusicIndex(new MusicListItem(music,false)) == -1){
                    app.insertPlayMusic(mMusic);
                }else {
                    app.resetPlayMusic(mMusic);
                }
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
        mMediaPlayerHelp = MediaPlayerHelp.getInstance(this);

        MyApplication app = (MyApplication) getApplication();
        app.setPlaying(false);
        if (app.getMusic() != null){
            mMediaPlayerHelp.setPath(app.getMusic().getMusic_path());
            mMediaPlayerHelp.setOnMediaPlayerHelperListener(new MediaPlayerHelp.OnMediaPlayerHelperListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {

                }

                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                }
            });
        }


    }


    @Override
    public void onDestroy() {

        //调用数据库方法，保存最后一次播放的音乐----------未实现

        super.onDestroy();
    }
}
