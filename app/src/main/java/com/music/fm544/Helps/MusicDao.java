package com.music.fm544.Helps;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.music.fm544.Bean.MusicPO;

import java.util.LinkedList;

public class MusicDao{

    private SQLiteDatabase database;
    private Cursor cursor = null;
    private DatabaseHelper helper;
    private Context context;

    public MusicDao(DatabaseHelper helper, Context context) {
        this.helper = helper;
        this.context = context;
    }

    //创建歌曲行
    public void create_music_table_row(MusicPO musicpo){

        String sql = "insert into music_table(music_name,music_album,music_author,music_time,music_pic_path,music_path) values('" +musicpo.getMusic_name()+"','"+musicpo.getMusic_album()+"','"+musicpo.getMusic_author()+"','"+musicpo.getMusic_time()+"','"+musicpo.getMusic_pic_path()+"','"+musicpo.getMusic_path()+"')" ;

        database.execSQL(sql);

    }

    //创建播放列表行
    public void create_play_table_row(MusicPO musicpo){

        String sql = "insert into play_table(music_name,music_album,music_author,music_time,music_pic_path,music_path,playing,played) values('"
                +musicpo.getMusic_name()+ "','"
                +musicpo.getMusic_album()+"','"
                +musicpo.getMusic_author()+"','"
                +musicpo.getMusic_time()+"','"
                +musicpo.getMusic_pic_path()+"','"
                +musicpo.getMusic_path()+"','"+0+"','"+0+"')" ;

        database.execSQL(sql);

    }

    //创建喜欢行
    public void create_like_table_row(MusicPO musicpo){

        String sql = "insert into like_table(music_name,music_album,music_author,music_time,music_pic_path,music_path) values('"
                +musicpo.getMusic_name()+"','"
                +musicpo.getMusic_album()+"','"
                +musicpo.getMusic_author()+"','"
                +musicpo.getMusic_time()+"','"
                +musicpo.getMusic_pic_path()+"','"
                +musicpo.getMusic_path()+"')" ;

        database.execSQL(sql);

    }

    //通过歌曲名查找歌曲信息
    public MusicPO search_music_table(String music_name){
        String[] args = null;
        args = new String[]{music_name};
        String sql = "select * from music_table where music_name = ?";
        cursor = database.rawQuery(sql,args);
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

    //添加到喜欢列表
    public void add_like_table(String music_name){
        //先从music_table获取到相关音乐的信息
        String[] args = null;
        args = new String[]{music_name};
        String sql = "select * from music_table where music_name = ?";
        cursor = database.rawQuery(sql,args);
        cursor.moveToFirst();
        String music_album = cursor.getString(cursor.getColumnIndex("music_album"));
        String music_author = cursor.getString(cursor.getColumnIndex("music_author"));
        int music_time = cursor.getInt(cursor.getColumnIndex("music_time"));
        String music_pic_path = cursor.getString(cursor.getColumnIndex("music_pic_path"));
        String music_path = cursor.getString(cursor.getColumnIndex("music_path"));
        MusicPO musicPO = new MusicPO(0,music_name,music_album,music_author,music_time,music_pic_path,music_path);
        //然后把获得的数据作为参数添加到喜欢列表中
        create_like_table_row(musicPO);
    }

    //从喜欢列表取消
    public void cancel_like(String music_name){
        String[] args = null;
        args = new String[]{music_name};
        String sql = "delete from like_table where music_name = ?";
        database.execSQL(sql,args);

    }

    //设置 已经播放
    public void add_already_played(MusicPO musicpo){
        String sql = "update play_table set played=1 and playing = 0" +"where music_name = '" + musicpo.getMusic_name()+"'";
        database.execSQL(sql);
    }

    //设置 正在播放
    public void add_is_playing(MusicPO musicpo){
        String sql = "update play_table set playing=1"+" where music_name = '"+musicpo.getMusic_name()+"'";
        database.execSQL(sql);
    }


    //播放完毕移除播放列表第一行
    public void after_playing(){
        String sql = "select * from play_table";
        cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        String music_name = cursor.getString(cursor.getColumnIndex("music_name"));
        String sql2 = "delete from play_table where music_name = '"+music_name+"'";
        database.execSQL(sql2);

    }

    //遍历歌曲列表,返回对象列表
    public LinkedList<MusicPO> select_all_music_table(){
        LinkedList<MusicPO> list = new LinkedList<>();
        String sql = "select * from music_table";
        cursor = database.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            MusicPO musicpo = new MusicPO();
            musicpo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            musicpo.setMusic_name(cursor.getString(cursor.getColumnIndex("music_name")));
            musicpo.setMusic_album(cursor.getString(cursor.getColumnIndex("music_album")));
            musicpo.setMusic_author(cursor.getString(cursor.getColumnIndex("music_author")));
            musicpo.setMusic_time(cursor.getInt(cursor.getColumnIndex("music_time")));
            musicpo.setMusic_pic_path(cursor.getString(cursor.getColumnIndex("music_pic_path")));
            musicpo.setMusic_path(cursor.getString(cursor.getColumnIndex("music_path")));
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
            list.add(musicpo);
        }
        return  list;
    }

    //遍历播放列表
    public LinkedList<MusicPO> select_all_play_table(){
        LinkedList<MusicPO> list = new LinkedList<>();
        String sql = "select * from play_table";
        cursor = database.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            MusicPO musicpo = new MusicPO();
            musicpo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            musicpo.setMusic_name(cursor.getString(cursor.getColumnIndex("music_name")));
            musicpo.setMusic_album(cursor.getString(cursor.getColumnIndex("music_album")));
            musicpo.setMusic_author(cursor.getString(cursor.getColumnIndex("music_author")));
            musicpo.setMusic_time(cursor.getInt(cursor.getColumnIndex("music_time")));
            musicpo.setMusic_pic_path(cursor.getString(cursor.getColumnIndex("music_pic_path")));
            musicpo.setMusic_path(cursor.getString(cursor.getColumnIndex("music_path")));
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
            list.add(musicpo);
        }
        return  list;
    }


    //遍历喜爱列表
    public LinkedList<MusicPO> select_all_like_table(){
        LinkedList<MusicPO> list = new LinkedList<>();
        String sql = "select * from like_table";
        cursor = database.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            MusicPO musicpo = new MusicPO();
            musicpo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            musicpo.setMusic_name(cursor.getString(cursor.getColumnIndex("music_name")));
            musicpo.setMusic_album(cursor.getString(cursor.getColumnIndex("music_album")));
            musicpo.setMusic_author(cursor.getString(cursor.getColumnIndex("music_author")));
            musicpo.setMusic_time(cursor.getInt(cursor.getColumnIndex("music_time")));
            musicpo.setMusic_pic_path(cursor.getString(cursor.getColumnIndex("music_pic_path")));
            musicpo.setMusic_path(cursor.getString(cursor.getColumnIndex("music_path")));
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
            list.add(musicpo);
        }
        return  list;
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
        cursor = database.rawQuery(sql,null);
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
        cursor = database.rawQuery(sql, null);
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
}
