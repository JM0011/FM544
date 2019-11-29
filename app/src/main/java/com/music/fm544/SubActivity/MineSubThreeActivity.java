package com.music.fm544.SubActivity;

import android.app.FragmentManager;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.music.fm544.Adapter.MusicItemAdapter;
import com.music.fm544.Bean.MusicPO;
import com.music.fm544.Helps.MusicDao;
import com.music.fm544.MyApplication;
import com.music.fm544.R;
import com.music.fm544.Service.MusicService;
import com.music.fm544.Utils.StatusBarUtils;
import com.music.fm544.Views.PlayMusicTab;

import java.util.ArrayList;
import java.util.List;

public class MineSubThreeActivity extends AppCompatActivity implements MusicItemAdapter.InnerItemOnclickListener,AdapterView.OnItemClickListener{
    private ListView list;
    private MusicItemAdapter mAdapter;

    private Intent mServiceIntent;
    private MusicService.MusicBind mMusicBind;
    private boolean isBindService;

    private FragmentManager fragmentManager;

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
        setContentView(R.layout.activity_mine_sub_three);
        initStatusBar();
        //获取ListView对象
        list = this.findViewById(R.id.listview);
        mPlayMusicTab = this.findViewById(R.id.plaing_tab);
        mPlayMusicTab.setFragmentManager(getFragmentManager());
        initView();

        List<MusicPO> list1 = getData();
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

    //屏幕适应--限制listView
    private void initView() {
        int srHeight = this.getResources().getDisplayMetrics().heightPixels;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)srHeight*9/11);
        list.setLayoutParams(layoutParams);

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
        mPlayMusicTab.destory();
    }

    private List<MusicPO> getData() {

        List<MusicPO> list1 = new ArrayList<MusicPO>();
//        for (int i = 0; i < 20; i++) {
//            MusicPO m = new MusicPO();
//            MusicPO m1 = new MusicPO();
//            String url = Environment.getExternalStorageDirectory().getAbsolutePath();
//            m.setMusic_name("成都");
//            m.setMusic_author("赵雷");
//            m.setMusic_path(url+"/Music/song.mp3");
//            m.setMusic_pic_path(url+"/Music/song.jpg");
//            m1.setMusic_name("当你");
//            m1.setMusic_author("林俊杰");
//            m1.setMusic_path(url+"/Music/song1.mp3");
//            m1.setMusic_pic_path(url+"/Music/song2.jpg");
//            m1.setImgId(R.drawable.song3);
//            m1.setSongName("当你");
//            m1.setSinger("林俊杰");
//            m.setImgId(R.drawable.song);
//            m.setSongName("成都");
//            m.setSinger("赵磊");
//            list1.add(m);
//            list1.add(m1);
//        }
        MyApplication app = (MyApplication) this.getApplication();
        MusicDao musicDao = new MusicDao(app.getDatebaseHelper(),this);
        list1 = musicDao.select_all_like_table();
        System.out.println(list1.size());
        return list1;

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MusicPO music = (MusicPO) mAdapter.getItem(i);
        toPlayMusic(music);
    }

    @Override
    public void itemClick(View view) {
        int positon;
        positon = (Integer) view.getTag();
        MusicPO music;
        if (view.getId() == R.id.item_music_more){
            music = (MusicPO) mAdapter.getItem(positon);
            Toast toast1 = Toast.makeText(this,"菜单",Toast.LENGTH_SHORT);
            toast1.show();
            showPopupMenu(view,music);
        }
//        switch (view.getId()){
//            case R.id.item_music_play:
//                music = (MusicPO) mAdapter.getItem(positon);
//                toPlayMusic(music);
//                break;
//            case R.id.item_music_more:
//                music = (MusicPO) mAdapter.getItem(positon);
//                Toast toast1 = Toast.makeText(this,"菜单",Toast.LENGTH_SHORT);
//                toast1.show();
//                showPopupMenu(view,music);
//                break;
//            default:
//                break;
//        }
    }


    private void showPopupMenu(View view, final MusicPO music)
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

    private void toPlayMusic(MusicPO music){
        if (mMusicBind != null){
            mMusicBind.insertMusic(music);
            mPlayMusicTab.resetPlayTabStatus(music);
        }
        Toast toast1 = Toast.makeText(this,"播放歌曲： "+music.getMusic_name(),Toast.LENGTH_SHORT);
        toast1.show();
    }

    //添加喜爱歌曲
    private void addLikeMusic(MusicPO music) {
        Toast toast1 = Toast.makeText(this,"添加喜爱歌曲: "+music.getMusic_name(),Toast.LENGTH_SHORT);
        toast1.show();
    }

    /**
     * 添加到播放列表
     */
    private void addIntoPlayList(MusicPO music) {
        MyApplication app = (MyApplication) getApplication();
        if (music != null){
            app.addPlayMusic(music);
        }
        Toast toast1 = Toast.makeText(this,"添加到播放列表: "+music.getMusic_name(),Toast.LENGTH_SHORT);
        toast1.show();
    }



    //设置状态栏颜色
    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtils.setStatusBarColor(MineSubThreeActivity.this, R.color.statusTab);
        }
    }

}
