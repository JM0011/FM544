package com.music.fm544.SubActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.music.fm544.Adapter.MusicItemAdapter;
import com.music.fm544.MyApplication;
import com.music.fm544.R;
import com.music.fm544.bean.Music;
import com.music.fm544.bean.MusicPO;
import com.music.fm544.service.MusicService;
import com.music.fm544.utils.StatusBarUtils;
import com.music.fm544.views.PlayMusicTab;

import java.util.ArrayList;
import java.util.List;

public class MineSubOneActivity extends AppCompatActivity implements MusicItemAdapter.InnerItemOnclickListener,AdapterView.OnItemClickListener{

    private ListView list;
    private MusicItemAdapter mAdapter;

    private Intent mServiceIntent;
    private MusicService.MusicBind mMusicBind;
    private boolean isBindService;

    //获取tab中的控件
    private PlayMusicTab mPlayMusicTab;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mMusicBind = (MusicService.MusicBind) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_sub_one);
        initStatusBar();
        //获取ListView对象
        list = this.findViewById(R.id.listview);
        mPlayMusicTab = this.findViewById(R.id.plaing_tab);

        List<Music> list1 = getData();
        mAdapter = new MusicItemAdapter(getApplicationContext(),list1);
        mAdapter.setOnInnerItemOnclickListener(this);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(this);

        ImageView back =  (ImageView) this.findViewById(R.id.back_btn);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //绑定Msuic服务
        initBind();


    }

    //绑定服务
    private void initBind() {
        MyApplication app = (MyApplication) getApplication();
        mServiceIntent = app.getServiceIntent();
        //绑定service
        if (!isBindService){
            isBindService = true;
            this.bindService(mServiceIntent,conn, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消服务绑定
        if(isBindService){
            isBindService = false;
            this.unbindService(conn);
        }
    }

    private List<Music> getData() {

        List<Music> list1 = new ArrayList<Music>();
        for (int i = 0; i < 20; i++) {
            Music m = new Music();
            Music m1 = new Music();
            m1.setImgId(R.drawable.song3);
            m1.setSongName("当你");
            m1.setSinger("林俊杰");
            m.setImgId(R.drawable.song);
            m.setSongName("成都");
            m.setSinger("赵磊");
            list1.add(m);
            list1.add(m1);
        }
        return list1;

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Music music = (Music) mAdapter.getItem(i);
        toPlayMusic(music);
    }

    @Override
    public void itemClick(View view) {
        int positon;
        positon = (Integer) view.getTag();
        Music music;
        switch (view.getId()){
            case R.id.item_music_play:
                music = (Music) mAdapter.getItem(positon);
                toPlayMusic(music);
                break;
            case R.id.item_music_more:
                music = (Music) mAdapter.getItem(positon);
                Toast toast1 = Toast.makeText(this,"菜单",Toast.LENGTH_SHORT);
                toast1.show();
                showPopupMenu(view,music);
                break;
            default:
                break;
        }
    }


    private void showPopupMenu(View view, final Music music)
    {
        PopupMenu menu = new PopupMenu(this,view);
        menu.getMenuInflater().inflate(R.menu.more,menu.getMenu());
        //设置点击事件
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle().equals("加入播放列表")){
                    addIntoPlayList(music);
                }else{
                    addLikeMusic(music);
                }
                return false;
            }
        });
        //设置关闭事件
        menu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu popupMenu) {

            }
        });
        menu.show();
    }

    private void toPlayMusic(Music music){
        MyApplication app = (MyApplication) getApplication();

        //设置播放音乐--------未完成
//        app.setMusic(music);
        if (mMusicBind != null){
            mMusicBind.nextMusic();
            resetPlayTabStatus(null);

        }
        Toast toast1 = Toast.makeText(this,"播放歌曲： "+music.getSongName(),Toast.LENGTH_SHORT);
        toast1.show();
    }

    //添加喜爱歌曲
    private void addLikeMusic(Music music) {
        Toast toast1 = Toast.makeText(this,"添加喜爱歌曲: "+music.getSongName(),Toast.LENGTH_SHORT);
        toast1.show();
    }

    /**
     * 添加到播放列表
     */
    private void addIntoPlayList(Music music) {
        Toast toast1 = Toast.makeText(this,"添加到播放列表: "+music.getSongName(),Toast.LENGTH_SHORT);
        toast1.show();
    }

    //修改PlayingTab信息状态
    private void resetPlayTabStatus(MusicPO music){
        ImageView song_img = mPlayMusicTab.getPlay_music_img();
        TextView song_name = mPlayMusicTab.getSong_txt();
        TextView singer_name = mPlayMusicTab.getSinger_txt();
        ImageView play_btn = mPlayMusicTab.getPlay_btn();

        //未完成
//        play_btn.setImageResource(R.mipmap.play_stop);
//        song_img.setImageResource(R.drawable.song3);
//        song_name.setText(music.getMusic_name());
//        singer_name.setText(music.getMusic_author());


        play_btn.setImageResource(R.mipmap.play_stop);
        song_img.setImageResource(R.drawable.song3);
        song_name.setText("当你");
        singer_name.setText("林俊杰");

    }

    //设置状态栏颜色
    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtils.setStatusBarColor(MineSubOneActivity.this, R.color.statusTab);
        }
    }

}
