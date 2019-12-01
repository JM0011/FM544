package com.music.fm544.Dialog;

import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.music.fm544.Adapter.PlayListItemAdapter;
import com.music.fm544.Bean.MusicListItem;
import com.music.fm544.MyApplication;
import com.music.fm544.R;
import com.music.fm544.Service.MusicService;

import java.util.List;


public class AddPlaylistDialog extends DialogFragment implements PlayListItemAdapter.InnerItemOnclickListener,AdapterView.OnItemClickListener{
    private ListView listView;
    private ImageView ivDeleteAll;
    private PlayListItemAdapter mAdapter;
    private Context mContext;
    private Intent mServiceIntent;
    private MusicService.MusicBind mMusicBind;
    private boolean isBindService;
    private List<MusicListItem> list;



    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mMusicBind = (MusicService.MusicBind) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        //去掉dialog的标题，需要在setContentView()之前
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = this.getDialog().getWindow();
        //去掉dialog默认的padding
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
       // lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //高度设置为屏幕的3/5
        int srHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        lp.height = (int)srHeight * 3/5;
        //设置dialog的位置在底部
        lp.gravity = Gravity.BOTTOM;
        //设置dialog的动画
        lp.windowAnimations = R.style.mystyle;
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable());

        initBind();

        final View view = inflater.inflate(R.layout.play_list, null);

        ivDeleteAll = view.findViewById(R.id.all_delete);
        listView = view.findViewById(R.id.listview);
        list = getData();
        mAdapter = new PlayListItemAdapter(mContext,list);
        mAdapter.setOnInnerItemOnclickListener(this);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);



        //删除全部播放列表
        ivDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication app = (MyApplication) mContext.getApplicationContext();
                if (mMusicBind != null){
                    mMusicBind.stopMusic();
                    app.deletePlayList();
                    list.clear();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isBindService){
            isBindService = false;
            mContext.unbindService(conn);
        }
    }

    //绑定服务
    private void initBind() {
        MyApplication app = (MyApplication) mContext.getApplicationContext();
        mServiceIntent = app.getServiceIntent();
        //绑定service
        if (!isBindService){
            isBindService = true;
            mContext.bindService(mServiceIntent,conn,Context.BIND_AUTO_CREATE);
        }
    }

    private List<MusicListItem> getData() {
//        MusicListItem music1 = new MusicListItem(0,"荼蘼未了","热门华语268","樱九",218570,"/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1574831373551",
//                "/storage/emulated/0/Music/music/荼蘼未了.mp3",0,false,false);
//        MusicListItem music2 = new MusicListItem(0,"为龙","唱给你的歌","河图",247879,"/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1574831351858",
//                "/storage/emulated/0/Music/music/河图 - 为龙.mp3",0,false,false);
//        MusicListItem music3 = new MusicListItem(0,"夜空中最亮的星","世界","逃跑计划",252268,"/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1574831381793",
//                "/storage/emulated/0/Music/music/夜空中最亮的星.mp3",0,true,false);
//        List<MusicListItem> lists = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            lists.add(music1);
//            lists.add(music2);
//            lists.add(music3);
//        }
//        return lists;
        MyApplication app = (MyApplication) mContext.getApplicationContext();
        return app.getPlayMusics();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MusicListItem music = (MusicListItem) mAdapter.getItem(i);
        if (mMusicBind != null){
            mMusicBind.insertMusic(music);

            mAdapter.notifyDataSetChanged();
        }
        Toast toast = Toast.makeText(mContext,"播放歌曲"+music.getMusic_name(),Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void itemClick(View view) {
        int positon;
        positon = (Integer) view.getTag();
        MusicListItem music;
        if (view.getId() == R.id.item_delete){
            MyApplication app = (MyApplication) mContext.getApplicationContext();
            music = (MusicListItem) mAdapter.getItem(positon);
            if (music.isPlaying()){
                if (mMusicBind != null){
                    mMusicBind.nextMusic();
                    app.removePlayMusic(music);
                }
            }else {
                app.removePlayMusic(music);
            }
            mAdapter.notifyDataSetChanged();
            Toast toast = Toast.makeText(mContext,"删除歌曲"+music.getMusic_name(),Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}

