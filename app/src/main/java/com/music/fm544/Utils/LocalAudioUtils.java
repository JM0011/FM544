package com.music.fm544.Utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.music.fm544.Bean.MusicPO;

import java.util.ArrayList;
import java.util.List;

public class LocalAudioUtils {
    private Context context;


    public LocalAudioUtils(Context context) {
        this.context = context;
    }

    public List<MusicPO> getAllSongs(Context context){
        List<MusicPO> songs = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media.MIME_TYPE + "=? or "
                        + MediaStore.Audio.Media.MIME_TYPE + "=?",
                new String[] { "audio/mpeg", "audio/x-ms-wma" }, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor == null){
            return songs;
        }
        if (cursor.moveToFirst()){
            MusicPO music = null;

            do {
                music = new MusicPO();
                String name,singer,album,type,pic_path = null,path,file_name;
                int time,album_id;
                Long size;
//                System.out.println(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                time = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                type = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE));
                size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                file_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                album_id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                if ("audio/mpeg".equals(type) ){
                    type = "mp3";
                } else if ("audio/x-ms-wma".equals(type) ){
                    type = "wma";
                }
                pic_path = getAlbumArt(album_id);
                if (pic_path == "" || pic_path == null || pic_path.equals("null")){
                    pic_path="";
                }
                System.out.println(name);
                System.out.println(singer);
                System.out.println(album);
                System.out.println(time);
                System.out.println(pic_path);
                System.out.println(path);
                music.setMusic_name(name);
                music.setMusic_author(singer);
                music.setMusic_album(album);
                music.setMusic_time(time);
                music.setMusic_pic_path(pic_path);
                music.setMusic_path(path);
                songs.add(music);
            }while (cursor.moveToNext());
            cursor.close();
        }

        return songs;
    }

    //获取专辑封面图片路径(若没有返回为空)
    private String getAlbumArt(int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        Cursor cur = context.getContentResolver().query(
                Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),
                projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        cur = null;
        return album_art;
    }

}
