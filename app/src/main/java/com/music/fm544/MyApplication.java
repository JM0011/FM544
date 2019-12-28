package com.music.fm544;

import android.app.Application;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Environment;

import com.music.fm544.Bean.MusicListItem;
import com.music.fm544.Bean.MusicPO;
import com.music.fm544.Helps.DatabaseHelper;
import com.music.fm544.Helps.MusicDao;
import com.music.fm544.Service.MusicService;
import com.music.fm544.Utils.LocalAudioUtils;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application{
    private boolean isPlaying;
    private MusicPO music = null;
    private List<MusicListItem> playMusics;
    private Intent serviceIntent;
    private DatabaseHelper datebaseHelper;

    public List<MusicListItem> getPlayMusics() {
        return playMusics;
    }

    public void setPlayMusics(List<MusicListItem> playMusics) {
        this.playMusics = playMusics;
    }

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

    //不暴露
    public void setMusic(MusicPO music) {
        this.music = music;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        datebaseHelper = new DatabaseHelper(this,"music.db",null,1);
        datebaseHelper.getWritableDatabase();


        isPlaying = false;
        if (serviceIntent == null){
            serviceIntent = new Intent(this, MusicService.class);
            this.startService(serviceIntent);
        }



//        //测试Musicdao
//        MusicPO music1 = new MusicPO(0,"荼蘼未了","热门华语268","樱九",218570,"/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1574831373551",
//                "/storage/emulated/0/Music/music/荼蘼未了.mp3",0,0);
//        MusicPO music2 = new MusicPO(0,"为龙","唱给你的歌","河图",247879,"/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1574831351858",
//                "/storage/emulated/0/Music/music/河图 - 为龙.mp3",0,0);
//        MusicPO music3 = new MusicPO(0,"夜空中最亮的星","世界","逃跑计划",252268,"/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1574831381793",
//                "/storage/emulated/0/Music/music/夜空中最亮的星.mp3",0,0);
//
//
//
//
//        MusicListItem music4 = new MusicListItem(0,"荼蘼未了","热门华语268","樱九",218570,"/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1574831373551",
//                "/storage/emulated/0/Music/music/荼蘼未了.mp3",0,0,false);
//        MusicListItem music5 = new MusicListItem(0,"为龙","唱给你的歌","河图",247879,"/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1574831351858",
//                "/storage/emulated/0/Music/music/河图 - 为龙.mp3",0,0,false);
//        MusicListItem music6 = new MusicListItem(0,"夜空中最亮的星","世界","逃跑计划",252268,"/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1574831381793",
//                "/storage/emulated/0/Music/music/夜空中最亮的星.mp3",0,0,true);

        // 测试本地音乐扫描
//        MediaScannerConnection.scanFile(this, new String[] { Environment
//                .getExternalStorageDirectory().getAbsolutePath() }, null, null);
//        LocalAudioUtils localAudioUtils = new LocalAudioUtils(this);
//        musicDao.init_music_table(localAudioUtils.getAllSongs(this));


        playMusics =  new ArrayList<>();
        MusicDao musicDao = new MusicDao(datebaseHelper,this);
        playMusics.addAll(musicDao.select_all_play_table());
        for (MusicListItem playMusic : playMusics) {
            if (playMusic.isPlaying()){
                music = playMusic;
            }
        }


    }

    //导入歌曲到数据库
    public boolean importMusicList(List<MusicPO> musicPOLists){
        //记录被播放列表被移出歌曲是否是正在播放的歌曲
        boolean status = false;
        MusicDao musicDao = new MusicDao(datebaseHelper,this);
        for (MusicPO musicPOList : musicPOLists) {
            musicPOList.setMusic_like_status(musicDao.get_like_status(musicPOList.getMusic_path()));
        }
        musicDao.init_music_table(musicPOLists);
        //删除播放列表中不存在于导入音乐集的歌曲
        for (int i = 0; i < playMusics.size(); i++) {
            boolean exist = false;
            for (MusicPO musicPOList : musicPOLists) {
                if (musicPOList.getMusic_path().equals(playMusics.get(i).getMusic_path())){
                    exist = true;
                    break;
                }
            }
            if (!exist){
                playMusics.remove(i);
            }
        }
        //若正在播放歌曲不存在与播放列表中，则更改正在播放歌曲为第一首歌
        int index = -1;
        for (int i = 0; i < playMusics.size(); i++) {
            if (playMusics.get(i).getMusic_path().equals(music.getMusic_path())){
                index = i;
                break;
            }
        }
        if(index == -1){
            if (playMusics.size() == 0){
                music = null;
            }else {
                music = playMusics.get(0);
                setDefault();
                playMusics.get(0).setPlaying(true);
                status = true;
            }
        }
        //初始化最近播放
        musicDao.init_recent_music();
        return status;
    }

    //扫描本地歌曲
    public List<MusicPO> getLocalMusic(){
        MediaScannerConnection.scanFile(this, new String[] { Environment
                .getExternalStorageDirectory().getAbsolutePath() }, null, null);
        LocalAudioUtils localAudioUtils = new LocalAudioUtils(this);
        return localAudioUtils.getAllSongs(this);
    }

    //保存播放列表记录到数据库
    public void savePlayMusicList(){
        MusicDao musicDao = new MusicDao(datebaseHelper,this);
        musicDao.init_play_table(playMusics);
    }

    //移除一条播放记录music
    public void removePlayMusic(MusicListItem music){
        int index = playMusics.indexOf(music);
        if (music.isPlaying()){
            if (index == -1){
                //播放器中无记录
            }else {
//                getNextMusic(); //播放下一首歌
                playMusics.remove(index);
            }
        }else {
            playMusics.remove(index);
        }
    }

    //添加一条播放记录(末尾)
    public void addPlayMusic(MusicPO music){
        MusicListItem musicListItem = new MusicListItem(music,false);
        int index = findMusicIndex(musicListItem);
        if (index == -1){
            playMusics.add(musicListItem);
        }
    }

    //插入一条播放记录
    public void insertPlayMusic(MusicPO music){
        MusicListItem musicListItem = new MusicListItem(music,true);
        int index = -1 ;
        for (int i = 0; i < playMusics.size(); i++) {
            if (playMusics.get(i).isPlaying()){
                index = i;
                break;
            }
        }
        setDefault();
        if (index != -1){
            playMusics.add(index+1,musicListItem);
        }else {
            playMusics.add(0,musicListItem);
        }
        isPlaying = true;
        this.music = music;

    }

    //播放列表中的歌曲（播放列表中）
    public void resetPlayMusic(MusicPO music){
        setDefault();
        for (int i = 0; i < playMusics.size(); i++) {
            if (playMusics.get(i).getMusic_path().equals(music.getMusic_path())){
                playMusics.get(i).setPlaying(true);
                break;
            }
        }
    }

    //获取music对应index，若没有则返回-1
    public int findMusicIndex(MusicListItem music){
        for (int i = 0; i < playMusics.size(); i++) {
            if (playMusics.get(i).getMusic_path().equals(music.getMusic_path())){
                return i;
            }
        }
        return -1;
    }

    //清空播放列表
    public void deletePlayList(){
        isPlaying = false;
        music = null;
        playMusics.clear();
    }

    //判断返回播放列表大小
    public int getPlayListSize(){
        return playMusics.size();
    }


    //获取下一首歌
    public MusicPO getNextMusic(){
        int index = -1;
        for (int i = 0; i < playMusics.size(); i++) {
            if (playMusics.get(i).isPlaying()){
                index = i;
                break;
            }
        }
        index += 1;
        if (index == 0){
            return null;
        } else if (playMusics.size() == index){
            setDefault();
            playMusics.get(0).setPlaying(true);
            return playMusics.get(0);
        }else {
            setDefault();
            playMusics.get(index).setPlaying(true);
            return playMusics.get(index);
        }
    }

    //获取上一首歌
    public MusicPO getPreviousMusic(){
        int index = -1;
        for (int i = 0; i < playMusics.size(); i++) {
            if (playMusics.get(i).isPlaying()){
                index = i;
                break;
            }
        }
        index -= 1;
        if (index == -2){
            return null;
        } else if (index == -1){
            setDefault();
            playMusics.get(playMusics.size()-1).setPlaying(true);
            return playMusics.get(playMusics.size()-1);
        }else {
            setDefault();
            playMusics.get(index).setPlaying(true);
            return playMusics.get(index);
        }
    }

    //将所有歌播放状态置为false
    private void setDefault(){
        for (MusicListItem playMusic : playMusics) {
            playMusic.setPlaying(false);
        }
    }

    //设置歌曲喜欢状态
    public void setMusicLikeStatus(MusicPO musicPO){
        MusicDao musicDao = new MusicDao(datebaseHelper,this);
        //更改歌曲数据库中相应歌曲
        if (musicPO.getMusic_like_status() == 0){
            musicDao.set_like_status(musicPO.getMusic_path());
        }else {
            musicDao.cancel_like(musicPO.getMusic_path());
        }
        if (musicPO.getMusic_like_status() == 0){
            musicPO.setMusic_like_status(1);
        }else {
            musicPO.setMusic_like_status(0);
        }

        //更改正在播放歌曲
        if (music != null && music.getMusic_path().equals(musicPO.getMusic_path())){
            music.setMusic_like_status(musicPO.getMusic_like_status());
        }
        //更改播放歌曲列表
        for (MusicListItem playMusic : playMusics) {
            if (playMusic.getMusic_path().equals(musicPO.getMusic_path())){
                playMusic.setMusic_like_status(musicPO.getMusic_like_status());
            }
        }
    }
}
