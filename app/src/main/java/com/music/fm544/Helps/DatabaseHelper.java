package com.music.fm544.Helps;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class DatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;

    public DatabaseHelper(Context context,String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    //创建数据库执行
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql1 = "CREATE TABLE music_table(id Integer PRIMARY KEY AUTOINCREMENT," +
                "music_name VARCHAR(30)," +
                "music_album VARCHAR(30)," +
                "music_author VARCHAR(30),"+
                "music_time Integer(1000),"+
                "music_pic_path VARCHAR(600)," +
                "music_path VARCHAR(600))";

        String sql2 = "CREATE TABLE play_table(id Integer PRIMARY KEY AUTOINCREMENT," +
                "music_name VARCHAR(30)," +
                "music_album VARCHAR(30)," +
                "music_author VARCHAR(30),"+
                "music_time Integer(1000),"+
                "music_pic_path VARCHAR(600)," +
                "music_path VARCHAR(600)," +
                "playing Integer(10)," +
                "played Integer(10))";

        String sql3 = "CREATE TABLE like_table(id Integer PRIMARY KEY AUTOINCREMENT," +
                "music_name VARCHAR(30)," +
                "music_album VARCHAR(30)," +
                "music_author VARCHAR(30),"+
                "music_time Integer(1000),"+
                "music_pic_path VARCHAR(600)," +
                "music_path VARCHAR(600))";

        sqLiteDatabase.execSQL(sql1);
        sqLiteDatabase.execSQL(sql2);
        sqLiteDatabase.execSQL(sql3);
        Toast.makeText(mContext,"数据库创建成功",Toast.LENGTH_SHORT).show();
    }

    //对数据库进行变更，i、i1为数据库的老版本和新版本
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //增加表/修改表结构
    }

}
