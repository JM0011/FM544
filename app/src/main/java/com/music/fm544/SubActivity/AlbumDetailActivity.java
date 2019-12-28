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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.music.fm544.Adapter.MusicItemAdapter;
import com.music.fm544.Bean.Album;
import com.music.fm544.Bean.MusicPO;
import com.music.fm544.Helps.MusicDao;
import com.music.fm544.MusicDetailActivity;
import com.music.fm544.MyApplication;
import com.music.fm544.R;
import com.music.fm544.Service.MusicService;
import com.music.fm544.Utils.StatusBarUtils;
import com.music.fm544.Views.PlayMusicTab;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailActivity extends AppCompatActivity implements MusicItemAdapter.InnerItemOnclickListener,AdapterView.OnItemClickListener{
    private ListView list;
    private TextView tvSongName;
    private TextView tvSingerName;
    private MusicItemAdapter mAdapter;
    List<MusicPO> dataList;


    private Album mAlbum;
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
        setContentView(R.layout.activity_album_detail);
        mAlbum = (Album) getIntent().getSerializableExtra("album");
        initStatusBar();
        tvSongName = (TextView) this.findViewById(R.id.song_name);
        tvSingerName = (TextView) this.findViewById(R.id.singer_name);
        tvSongName.setText(mAlbum.getAlbumName().toString());
        tvSingerName.setText(mAlbum.getSinger().toString());
        //获取ListView对象
        list = this.findViewById(R.id.listview);
        mPlayMusicTab = this.findViewById(R.id.plaing_tab);
        mPlayMusicTab.setFragmentManager(getFragmentManager());

        initView();

        dataList = getData();
        mAdapter = new MusicItemAdapter(getApplicationContext(),dataList);
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
        MyApplication app = (MyApplication) this.getApplication();
        MusicDao musicDao = new MusicDao(app.getDatebaseHelper(),this);
        list1 = musicDao.get_music_by_album(mAlbum);
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
        MenuItem menuItem = menu.getMenu().findItem(R.id.music_like);
        if (music.getMusic_like_status() == 1){
            menuItem.setTitle("取消喜爱");
        }
        //设置点击事件
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle().equals("加入播放列表")){
                    addIntoPlayList(music);
                }else if(menuItem.getTitle().equals("取消喜爱")){
                    setLikeMusic(music);
                }else if(menuItem.getTitle().equals("添加喜爱")){
                    setLikeMusic(music);
                }else if(menuItem.getTitle().equals("歌曲详情")){
                    gotoMusicDetail(music);
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

    private void gotoMusicDetail(MusicPO music) {
        Intent intent = new Intent(this, MusicDetailActivity.class);
        intent.putExtra("musicDetail", music);
        startActivity(intent);
    }

    private void toPlayMusic(MusicPO music){
        MyApplication app = (MyApplication) getApplication();

        if (mMusicBind != null){
            mMusicBind.insertMusic(music);
            mPlayMusicTab.resetPlayTabStatus(music);

        }
        Toast toast1 = Toast.makeText(this,"播放歌曲： "+music.getMusic_name(),Toast.LENGTH_SHORT);
        toast1.show();
    }

    //设置喜爱歌曲
    private void setLikeMusic(MusicPO music) {
        String str = "";
        MyApplication app = (MyApplication) getApplication();
        app.setMusicLikeStatus(music);
        //music为对象引用
        for (MusicPO musicPO : dataList) {
            if (musicPO.getMusic_path().equals(music.getMusic_path())){
                if (music.getMusic_like_status() == 0){
                    musicPO.setMusic_like_status(0);
                    break;
                }else if (music.getMusic_like_status() == 1){
                    musicPO.setMusic_like_status(1);
                    break;
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        if (music.getMusic_like_status() == 1){
            str = "添加喜爱歌曲：";
        }else {
            str = "取消歌曲喜爱：";
        }
        Toast toast1 = Toast.makeText(this,str + music.getMusic_name(),Toast.LENGTH_SHORT);
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
            StatusBarUtils.setStatusBarColor(AlbumDetailActivity.this, R.color.statusTab);
        }
    }





}
