package com.music.fm544.Helps;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.music.fm544.Bean.Album;
import com.music.fm544.Bean.MusicListItem;
import com.music.fm544.Bean.MusicPO;
import com.music.fm544.Bean.Singer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MusicDao{

    private SQLiteDatabase database;
    private DatabaseHelper helper;
    private Context context;

    public MusicDao(DatabaseHelper helper, Context context) {
        this.helper = helper;
        this.context = context;
        database = helper.getWritableDatabase();
    }

    //创建歌曲行(已测试）
    public void create_music_table_row(MusicPO musicpo){

        String sql = "insert into music_table(music_name,music_album,music_author,music_time,music_pic_path,music_path,isHighQuality,music_like_status) values('" +musicpo.getMusic_name()+"','"
                +musicpo.getMusic_album()+"','"
                +musicpo.getMusic_author()+"','"
                +musicpo.getMusic_time()+"','"
                +musicpo.getMusic_pic_path()+"','"
                +musicpo.getMusic_path()+"','"
                +musicpo.isHighQuality()+"','"
                +musicpo.getMusic_like_status()+"')" ;

        database.execSQL(sql);

    }

    //创建播放列表行（已测试）
    public void create_play_table_row(MusicListItem music){
        int  play_status = 0;
        if(music.isPlaying()){
            play_status = 1;
        }
        String sql = "insert into play_table(music_name,music_album,music_author,music_time,music_pic_path,music_path,isHighQuality,music_like_status,isPlaying) values('"
                +music.getMusic_name()+ "','"
                +music.getMusic_album()+"','"
                +music.getMusic_author()+"','"
                +music.getMusic_time()+"','"
                +music.getMusic_pic_path()+"','"
                +music.getMusic_path()+"','"
                +music.isHighQuality()+"','"
                +music.getMusic_like_status()+"','"
                +play_status+"')" ;

        database.execSQL(sql);

    }

    //清除播放列表并初始化(已测试)
    public void init_play_table(List<MusicListItem> musicList){
        String sql = "delete from play_table";
        database.execSQL(sql);
        for (MusicListItem music : musicList) {
            if (music != null){
                create_play_table_row(music);
            }
        }
    }



    //通过歌曲名查找music_table歌曲信息(已测试)
    public MusicPO search_music_table(String music_name){
        String[] args = null;
        args = new String[]{music_name};
        String sql = "select * from music_table where music_name = ?";
        Cursor cursor = database.rawQuery(sql,args);
        cursor.moveToFirst();
        MusicPO musicpo = new MusicPO();
        musicpo.setId(cursor.getInt(cursor.getColumnIndex("id")));
        musicpo.setMusic_name(cursor.getString(cursor.getColumnIndex("music_name")));
        musicpo.setMusic_album(cursor.getString(cursor.getColumnIndex("music_album")));
        musicpo.setMusic_author(cursor.getString(cursor.getColumnIndex("music_author")));
        musicpo.setMusic_time(cursor.getInt(cursor.getColumnIndex("music_time")));
        musicpo.setMusic_pic_path(cursor.getString(cursor.getColumnIndex("music_pic_path")));
        musicpo.setMusic_path(cursor.getString(cursor.getColumnIndex("music_path")));
        musicpo.setMusic_like_status(cursor.getInt(cursor.getColumnIndex("music_like_status")));
        musicpo.setHighQuality(cursor.getInt(cursor.getColumnIndex("isHighQuality")));
        return musicpo;
    }

    //设置歌曲为喜欢（已测试）
    public void set_like_status(String music_path){
        String sql = "update music_table set music_like_status=1 " +"where music_path = '" + music_path +"'";
        database.execSQL(sql);

    }

    //从喜欢列表取消(已测试)
    public void cancel_like(String music_path){
        String sql = "update music_table set music_like_status=0 " +"where music_path = '" + music_path +"'";
        database.execSQL(sql);

    }

    //设置 已经播放（已测试）
    public void add_already_played(MusicPO musicpo){
        String sql = "update play_table set played=1 and playing=0 " +"where music_name = '" + musicpo.getMusic_name()+"'";
        database.execSQL(sql);
    }

    //设置 正在播放(已测试)
    public void add_is_playing(MusicPO musicpo){
        String sql = "update play_table set playing=1 and played=0 "+" where music_name = '"+musicpo.getMusic_name()+"'";
        database.execSQL(sql);
    }

    //初始化歌曲表music_table(已测试)
    public void init_music_table(List<MusicPO> musics){
        String sql = "delete from music_table";
        database.execSQL(sql);
        for (MusicPO music : musics) {
            if (music != null){
                create_music_table_row(music);
            }
        }
    }


    //播放完毕移除播放列表第一行
    public void after_playing(){
        String sql = "select * from play_table";
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        String music_name = cursor.getString(cursor.getColumnIndex("music_name"));
        String sql2 = "delete from play_table where music_name = '"+music_name+"'";
        database.execSQL(sql2);
    }

    //遍历歌曲列表,返回对象列表(已测试)
    public LinkedList<MusicPO> select_all_music_table(){
        LinkedList<MusicPO> list = new LinkedList<>();
        String sql = "select * from music_table";
        Cursor cursor = database.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            MusicPO musicpo = new MusicPO();
            musicpo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            musicpo.setMusic_name(cursor.getString(cursor.getColumnIndex("music_name")));
            musicpo.setMusic_album(cursor.getString(cursor.getColumnIndex("music_album")));
            musicpo.setMusic_author(cursor.getString(cursor.getColumnIndex("music_author")));
            musicpo.setMusic_time(cursor.getInt(cursor.getColumnIndex("music_time")));
            musicpo.setMusic_pic_path(cursor.getString(cursor.getColumnIndex("music_pic_path")));
            musicpo.setMusic_path(cursor.getString(cursor.getColumnIndex("music_path")));
            musicpo.setMusic_like_status(cursor.getInt(cursor.getColumnIndex("music_like_status")));
            musicpo.setHighQuality(cursor.getInt(cursor.getColumnIndex("isHighQuality")));
            list.add(musicpo);
        }
        while(cursor.moveToNext()){
            MusicPO musicpo = new MusicPO();
            musicpo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            musicpo.setMusic_name(cursor.getString(cursor.getColumnIndex("music_name")));
            musicpo.setMusic_album(cursor.getString(cursor.getColumnIndex("music_album")));
            musicpo.setMusic_author(cursor.getString(cursor.getColumnIndex("music_author")));
            musicpo.setMusic_time(cursor.getInt(cursor.getColumnIndex("music_time")));
            musicpo.setMusic_pic_path(cursor.getString(cursor.getColumnIndex("music_pic_path")));
            musicpo.setMusic_path(cursor.getString(cursor.getColumnIndex("music_path")));
            musicpo.setMusic_like_status(cursor.getInt(cursor.getColumnIndex("music_like_status")));
            musicpo.setHighQuality(cursor.getInt(cursor.getColumnIndex("isHighQuality")));
            list.add(musicpo);
        }
        return  list;
    }

    //遍历播放列表(已测试)
    public List<MusicListItem> select_all_play_table(){
        List<MusicListItem> list = new ArrayList<>();
        String sql = "select * from play_table";
        Cursor cursor = database.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            MusicListItem music = new MusicListItem();
            music.setId(cursor.getInt(cursor.getColumnIndex("id")));
            music.setMusic_name(cursor.getString(cursor.getColumnIndex("music_name")));
            music.setMusic_album(cursor.getString(cursor.getColumnIndex("music_album")));
            music.setMusic_author(cursor.getString(cursor.getColumnIndex("music_author")));
            music.setMusic_time(cursor.getInt(cursor.getColumnIndex("music_time")));
            music.setMusic_pic_path(cursor.getString(cursor.getColumnIndex("music_pic_path")));
            music.setMusic_path(cursor.getString(cursor.getColumnIndex("music_path")));
            music.setHighQuality(cursor.getInt(cursor.getColumnIndex("isHighQuality")));
            music.setMusic_like_status(cursor.getInt(cursor.getColumnIndex("music_like_status")));
            int play_status = cursor.getInt(cursor.getColumnIndex("isPlaying"));
            if (play_status == 1){
                music.setPlaying(true);
            }else {
                music.setPlaying(false);
            }
            list.add(music);
        }
        while(cursor.moveToNext()){
            MusicListItem music = new MusicListItem();
            music.setId(cursor.getInt(cursor.getColumnIndex("id")));
            music.setMusic_name(cursor.getString(cursor.getColumnIndex("music_name")));
            music.setMusic_album(cursor.getString(cursor.getColumnIndex("music_album")));
            music.setMusic_author(cursor.getString(cursor.getColumnIndex("music_author")));
            music.setMusic_time(cursor.getInt(cursor.getColumnIndex("music_time")));
            music.setMusic_pic_path(cursor.getString(cursor.getColumnIndex("music_pic_path")));
            music.setMusic_path(cursor.getString(cursor.getColumnIndex("music_path")));
            music.setHighQuality(cursor.getInt(cursor.getColumnIndex("isHighQuality")));
            music.setMusic_like_status(cursor.getInt(cursor.getColumnIndex("music_like_status")));
            int play_status = cursor.getInt(cursor.getColumnIndex("isPlaying"));
            if (play_status == 1){
                music.setPlaying(true);
            }else {
                music.setPlaying(false);
            }
            list.add(music);
        }
        return  list;
    }


    //根据音乐名，查询是否喜爱（已测试）
    public Integer get_like_status(String music_path){
        String sql = "select music_like_status from music_table where music_path = '"+ music_path +"'";
        Cursor cursor = database.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            return cursor.getInt(cursor.getColumnIndex("music_like_status"));
        }else {
            return 0;
        }

    }

    //遍历喜爱列表(已测试)
    public LinkedList<MusicPO> select_all_like_table(){
        LinkedList<MusicPO> list = new LinkedList<>();
        String sql = "select * from music_table where music_like_status = 1";
        Cursor cursor = database.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            MusicPO musicpo = new MusicPO();
            musicpo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            musicpo.setMusic_name(cursor.getString(cursor.getColumnIndex("music_name")));
            musicpo.setMusic_album(cursor.getString(cursor.getColumnIndex("music_album")));
            musicpo.setMusic_author(cursor.getString(cursor.getColumnIndex("music_author")));
            musicpo.setMusic_time(cursor.getInt(cursor.getColumnIndex("music_time")));
            musicpo.setMusic_pic_path(cursor.getString(cursor.getColumnIndex("music_pic_path")));
            musicpo.setMusic_path(cursor.getString(cursor.getColumnIndex("music_path")));
            musicpo.setMusic_like_status(cursor.getInt(cursor.getColumnIndex("music_like_status")));
            musicpo.setHighQuality(cursor.getInt(cursor.getColumnIndex("isHighQuality")));
            list.add(musicpo);
        }
        while(cursor.moveToNext()){
            MusicPO musicpo = new MusicPO();
            musicpo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            musicpo.setMusic_name(cursor.getString(cursor.getColumnIndex("music_name")));
            musicpo.setMusic_album(cursor.getString(cursor.getColumnIndex("music_album")));
            musicpo.setMusic_author(cursor.getString(cursor.getColumnIndex("music_author")));
            musicpo.setMusic_time(cursor.getInt(cursor.getColumnIndex("music_time")));
            musicpo.setMusic_pic_path(cursor.getString(cursor.getColumnIndex("music_pic_path")));
            musicpo.setMusic_path(cursor.getString(cursor.getColumnIndex("music_path")));
            musicpo.setMusic_like_status(cursor.getInt(cursor.getColumnIndex("music_like_status")));
            musicpo.setHighQuality(cursor.getInt(cursor.getColumnIndex("isHighQuality")));
            list.add(musicpo);
        }
        return  list;
    }

    //获取专辑列表(已测试)
    public List<Album> get_music_group_by_album(){
        List<Album> albums = new ArrayList<>();
        String sql = "select * from music_table GROUP BY music_album";
        Cursor cursor = database.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do{
                Album album = new Album();
                album.setAlbumName(cursor.getString(cursor.getColumnIndex("music_album")));
                album.setSinger(cursor.getString(cursor.getColumnIndex("music_author")));
                album.setPicPath(cursor.getString(cursor.getColumnIndex("music_pic_path")));
                albums.add(album);
            }while (cursor.moveToNext());
        }
        return albums;
    }

    //获取对应专辑的歌曲列表(已测试)
    public List<MusicPO> get_music_by_album(Album album){
        List<MusicPO> musics = new ArrayList<>();
        String sql = "select * from music_table where music_album = '"+album.getAlbumName()+"' and music_pic_path = '"+album.getPicPath()+"'";
        Cursor cursor = database.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do{
                MusicPO musicpo = new MusicPO();
                musicpo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                musicpo.setMusic_name(cursor.getString(cursor.getColumnIndex("music_name")));
                musicpo.setMusic_album(cursor.getString(cursor.getColumnIndex("music_album")));
                musicpo.setMusic_author(cursor.getString(cursor.getColumnIndex("music_author")));
                musicpo.setMusic_time(cursor.getInt(cursor.getColumnIndex("music_time")));
                musicpo.setMusic_pic_path(cursor.getString(cursor.getColumnIndex("music_pic_path")));
                musicpo.setMusic_path(cursor.getString(cursor.getColumnIndex("music_path")));
                musicpo.setMusic_like_status(cursor.getInt(cursor.getColumnIndex("music_like_status")));
                musicpo.setHighQuality(cursor.getInt(cursor.getColumnIndex("isHighQuality")));
                musics.add(musicpo);

            }while (cursor.moveToNext());
        }
        return musics;
    }

    //获取歌手列表(已测试)
    public List<Singer> get_singer_group_by_album(){
        List<Singer> singers = new ArrayList<>();
        String sql = "select music_author,COUNT(id) from music_table GROUP BY music_author";
        Cursor cursor = database.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do{
                Singer singer = new Singer();
                singer.setSingerName(cursor.getString(0));
                int num = cursor.getInt(1);
                singer.setMusicNum(String.valueOf(num));
                singers.add(singer);
            }while (cursor.moveToNext());
        }
        return singers;
    }


    //获取对应歌手的歌曲列表(已测试)
    public List<MusicPO> get_music_by_singer(Singer singer){
        List<MusicPO> musics = new ArrayList<>();
        String sql = "select * from music_table where music_author = '"+singer.getSingerName()+"'";
        Cursor cursor = database.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do{
                MusicPO musicpo = new MusicPO();
                musicpo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                musicpo.setMusic_name(cursor.getString(cursor.getColumnIndex("music_name")));
                musicpo.setMusic_album(cursor.getString(cursor.getColumnIndex("music_album")));
                musicpo.setMusic_author(cursor.getString(cursor.getColumnIndex("music_author")));
                musicpo.setMusic_time(cursor.getInt(cursor.getColumnIndex("music_time")));
                musicpo.setMusic_pic_path(cursor.getString(cursor.getColumnIndex("music_pic_path")));
                musicpo.setMusic_path(cursor.getString(cursor.getColumnIndex("music_path")));
                musicpo.setMusic_like_status(cursor.getInt(cursor.getColumnIndex("music_like_status")));
                musicpo.setHighQuality(cursor.getInt(cursor.getColumnIndex("isHighQuality")));
                musics.add(musicpo);

            }while (cursor.moveToNext());
        }
        return musics;
    }

    //搜索相关歌曲(已测试)
    public List<MusicPO> search_music(String str){
        List<MusicPO> musics = new ArrayList<>();
        String sql = "select * from music_table where music_name LIKE '%"+str+"%' or music_author LIKE '%" + str +"%'";
        Cursor cursor = database.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do{
                MusicPO musicpo = new MusicPO();
                musicpo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                musicpo.setMusic_name(cursor.getString(cursor.getColumnIndex("music_name")));
                musicpo.setMusic_album(cursor.getString(cursor.getColumnIndex("music_album")));
                musicpo.setMusic_author(cursor.getString(cursor.getColumnIndex("music_author")));
                musicpo.setMusic_time(cursor.getInt(cursor.getColumnIndex("music_time")));
                musicpo.setMusic_pic_path(cursor.getString(cursor.getColumnIndex("music_pic_path")));
                musicpo.setMusic_path(cursor.getString(cursor.getColumnIndex("music_path")));
                musicpo.setMusic_like_status(cursor.getInt(cursor.getColumnIndex("music_like_status")));
                musicpo.setHighQuality(cursor.getInt(cursor.getColumnIndex("isHighQuality")));
                musics.add(musicpo);
            }while (cursor.moveToNext());
        }
        return musics;
    }

    //清空播放列表
    public void remove_all_play(){
        String sql = "delete from play_table";
        database.execSQL(sql);
    }

    //按照音乐名删除对应三个表中的数据
    public void remove_music_name(String music_name){
        String sql1 = "delete from music_table where music_name = '"+music_name+"'";
        database.execSQL(sql1);
        String sql2 = "delete from play_table where music_name = '"+music_name+"'";
        database.execSQL(sql2);
        String sql3 = "delete from like_table where music_name = '"+music_name+"'";
        database.execSQL(sql3);

    }

    //按照音乐名删除数据，分别的
    public void  remove_music_name_from_music_table(String music_name){
        String sql1 = "delete from music_table where music_name = '"+music_name+"'";
        database.execSQL(sql1);
    }

    public void  remove_music_name_from_play_table(String music_name){
        String sql2 = "delete from play_table where music_name = '"+music_name+"'";
        database.execSQL(sql2);
    }

    public void  remove_music_name_from_like_table(String music_name){
        String sql3 = "delete from like_table where music_name = '"+music_name+"'";
        database.execSQL(sql3);
    }

    //获取正在播放的音乐信息
    public MusicPO get_playing(){
        String sql = "select * from play_table where playing = 1";
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        MusicPO musicpo = new MusicPO();
        musicpo.setId(cursor.getInt(cursor.getColumnIndex("id")));
        musicpo.setMusic_name(cursor.getString(cursor.getColumnIndex("music_name")));
        musicpo.setMusic_album(cursor.getString(cursor.getColumnIndex("music_album")));
        musicpo.setMusic_author(cursor.getString(cursor.getColumnIndex("music_author")));
        musicpo.setMusic_time(cursor.getInt(cursor.getColumnIndex("music_time")));
        musicpo.setMusic_pic_path(cursor.getString(cursor.getColumnIndex("music_pic_path")));
        musicpo.setMusic_path(cursor.getString(cursor.getColumnIndex("music_path")));
        return musicpo;

    }

    //获取下一首播放信息
    public MusicPO get_next_music() {
        String sql = "select * from play_table where playing = 1";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        if (cursor.moveToNext()) {
            MusicPO musicpo = new MusicPO();
            musicpo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            musicpo.setMusic_name(cursor.getString(cursor.getColumnIndex("music_name")));
            musicpo.setMusic_album(cursor.getString(cursor.getColumnIndex("music_album")));
            musicpo.setMusic_author(cursor.getString(cursor.getColumnIndex("music_author")));
            musicpo.setMusic_time(cursor.getInt(cursor.getColumnIndex("music_time")));
            musicpo.setMusic_pic_path(cursor.getString(cursor.getColumnIndex("music_pic_path")));
            musicpo.setMusic_path(cursor.getString(cursor.getColumnIndex("music_path")));
            return musicpo;
        } else {
            return null;
        }

    }

    //获取id，music_table表
    public int get_id_from_music_table(MusicPO musicpo){
        String sql = "select id from music_table where id = '"+musicpo.getId()+"'";
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        return id;
    }

    //获取id，play_table表
    public int get_id_from_play_table(MusicPO musicpo){
        String sql = "select id from play_table where id = '"+musicpo.getId()+"'";
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        return id;
    }

    //获取id，like_table表
    public int get_id_from_like_table(MusicPO musicpo){
        String sql = "select id from like_table where id = '"+musicpo.getId()+"'";
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        return id;
    }
}
