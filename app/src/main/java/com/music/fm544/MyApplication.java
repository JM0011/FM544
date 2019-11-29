package com.music.fm544;

import android.app.Application;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Environment;

import com.music.fm544.Bean.MusicListItem;
import com.music.fm544.Bean.MusicPO;
import com.music.fm544.Helps.DatabaseHelper;
import com.music.fm544.Helps.DateBaseHelp;
import com.music.fm544.Helps.MusicDao;
import com.music.fm544.Service.MusicService;
import com.music.fm544.Utils.LocalAudioUtils;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application{
    private boolean isPlaying;
    private MusicPO music;
    private List<MusicListItem> playMusics;
    private DateBaseHelp mDateBaseHelp;
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


        mDateBaseHelp = new DateBaseHelp();
        isPlaying = false;
        music = mDateBaseHelp.getCurrentMusic();
        if (serviceIntent == null){
            serviceIntent = new Intent(this, MusicService.class);
            this.startService(serviceIntent);
        }



        //测试Musicdao
        MusicPO music1 = new MusicPO(0,"荼蘼未了","热门华语268","樱九",218570,"/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1574831373551",
                "/storage/emulated/0/Music/music/荼蘼未了.mp3",0,false);
        MusicPO music2 = new MusicPO(0,"为龙","唱给你的歌","河图",247879,"/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1574831351858",
                "/storage/emulated/0/Music/music/河图 - 为龙.mp3",0,false);
        MusicPO music3 = new MusicPO(0,"夜空中最亮的星","世界","逃跑计划",252268,"/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1574831381793",
                "/storage/emulated/0/Music/music/夜空中最亮的星.mp3",0,false);
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


        MusicListItem music4 = new MusicListItem(0,"荼蘼未了","热门华语268","樱九",218570,"/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1574831373551",
                "/storage/emulated/0/Music/music/荼蘼未了.mp3",0,false,false);
        MusicListItem music5 = new MusicListItem(0,"为龙","唱给你的歌","河图",247879,"/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1574831351858",
                "/storage/emulated/0/Music/music/河图 - 为龙.mp3",0,false,false);
        MusicListItem music6 = new MusicListItem(0,"夜空中最亮的星","世界","逃跑计划",252268,"/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1574831381793",
                "/storage/emulated/0/Music/music/夜空中最亮的星.mp3",0,true,true);
        playMusics =  new ArrayList<>();
//        playMusics.add(music4);
//        playMusics.add(music5);
//        playMusics.add(music6);


//        musicDao.cancel_like("夜空中最亮的星");

//        musicDao.add_already_played(music1);

//        musicDao.add_is_playing(music1);

//        测试本地音乐扫描
        MediaScannerConnection.scanFile(this, new String[] { Environment
                .getExternalStorageDirectory().getAbsolutePath() }, null, null);

        LocalAudioUtils localAudioUtils = new LocalAudioUtils(this);
        musicDao.init_music_table(localAudioUtils.getAllSongs(this));

//        List<Album> albums = musicDao.get_music_group_by_album();
//        for (Album a : albums) {
//            System.out.println(a.getAlbumName());
//            System.out.println(a.getSinger());
//            System.out.println(a.getPicPath());
//        }
//        musicDao.set_like_status("夜空中最亮的星");
//        musicDao.set_like_status("荼蘼未了");

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
        playMusics = null;
    }

//    //获取下一首歌
//    public MusicPO getNextMusic(MusicPO musicpo){
//        int index = -1;
//        for (int i = 0; i < playMusics.size(); i++) {
//            if (musicpo != null && playMusics.get(i).getMusic_path().equals(musicpo.getMusic_path())){
//                index = i;
//                break;
//            }
//        }
//        index +=1;
//        if (index == 0){
//          return null;
//        } else if (playMusics.size() == index){
//            setDefault();
//            playMusics.get(0).setPlaying(true);
//            return playMusics.get(0);
//        }else {
//            setDefault();
//            playMusics.get(index).setPlaying(true);
//            return playMusics.get(index);
//        }
//    }

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

    //将所有歌播放状态置为false
    private void setDefault(){
        for (MusicListItem playMusic : playMusics) {
            playMusic.setPlaying(false);
        }
    }
}
