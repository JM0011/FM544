package com.music.fm544;

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
import android.widget.SearchView;
import android.widget.Toast;

import com.music.fm544.Adapter.MusicItemAdapter;
import com.music.fm544.Bean.MusicPO;
import com.music.fm544.Helps.MusicDao;
import com.music.fm544.Service.MusicService;
import com.music.fm544.Utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements MusicItemAdapter.InnerItemOnclickListener,AdapterView.OnItemClickListener{

    private ListView list;
    private SearchView searchView;
    private MusicItemAdapter mAdapter;
    private List<MusicPO> listDate;

    private Intent mServiceIntent;
    private MusicService.MusicBind mMusicBind;
    private boolean isBindService;

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
        setContentView(R.layout.activity_search);
        initStatusBar();

        list = findViewById(R.id.listview);
        searchView = findViewById(R.id.searchview);
        listDate = getData();
        mAdapter = new MusicItemAdapter(getApplicationContext(),listDate);
        mAdapter.setOnInnerItemOnclickListener(this);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(this);

        setSearchListen();

        ImageView back =  (ImageView) this.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //绑定Msuic服务
        initBind();
    }

    //设置搜索响应事件
    private void setSearchListen() {
        MyApplication app = (MyApplication) getApplication();
        final MusicDao musicDao = new MusicDao(app.getDatebaseHelper(),this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query == null || query.equals("")){

                }else{
                    listDate.clear();
                    listDate.addAll(musicDao.search_music(query));
                    mAdapter.notifyDataSetChanged();
                }
                return true;
            }

            //当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.equals("")){

                }else{
                    listDate.clear();
                    listDate.addAll(musicDao.search_music(newText));
                    mAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });


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
////            m.setSinger("赵磊");
//            list1.add(m);
//            list1.add(m1);
//        }
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
        for (MusicPO musicPO : listDate) {
            if (musicPO.getMusic_path().equals(music.getMusic_path())){
                if (music.getMusic_like_status() == 0){
                    musicPO.setMusic_like_status(1);
                    break;
                }else if (music.getMusic_like_status() == 1){
                    musicPO.setMusic_like_status(0);
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
            StatusBarUtils.setStatusBarColor(SearchActivity.this, R.color.statusTab);
        }
    }

}
