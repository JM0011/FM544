package com.music.fm544.Helps;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;



public class MediaPlayerHelp {

    private static MediaPlayerHelp instance;

    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private String mPath;
    private OnMediaPlayerHelperListener onMediaPlayerHelperListener;

    public void setOnMediaPlayerHelperListener(OnMediaPlayerHelperListener onMediaPlayerHelperListener) {
        this.onMediaPlayerHelperListener = onMediaPlayerHelperListener;
    }

    //单例
    public static MediaPlayerHelp getInstance(Context context){
        if (instance == null ){
            synchronized (MediaPlayerHelp.class){
                if (instance ==  null){
                    instance = new MediaPlayerHelp(context);
                }
            }
        }
        return instance;
    }

    private MediaPlayerHelp(Context context){
        mContext = context;
        mMediaPlayer = new MediaPlayer();
    }

    //设置需要播放的音乐路径
    public void setPath(String path){
        if(mMediaPlayer.isPlaying() || !path.equals(mPath)){
            mMediaPlayer.reset();
        }
        mPath = path;
        try {
            mMediaPlayer.setDataSource(mContext, Uri.parse(mPath));
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //异步加载音乐
//        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                if(onMediaPlayerHelperListener != null){
                    onMediaPlayerHelperListener.onPrepared(mediaPlayer);
                }
            }
        });

        //监听音乐播放完成
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (onMediaPlayerHelperListener != null){
                    onMediaPlayerHelperListener.onCompletion(mediaPlayer);
                }
            }
        });
    }

    // 返回正在播放的音乐路径
    public String getPath(){
        return mPath;
    }

    //播放音乐
    public void start(){
        if (mMediaPlayer.isPlaying()) return;
        mMediaPlayer.start();
    }

    //暂停播放
    public void pause(){
        mMediaPlayer.pause();
    }

    public interface OnMediaPlayerHelperListener{
        void onPrepared(MediaPlayer mediaPlayer);
        void onCompletion(MediaPlayer mediaPlayer);
    }
}
