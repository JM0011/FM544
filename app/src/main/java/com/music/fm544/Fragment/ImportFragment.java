package com.music.fm544.Fragment;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.music.fm544.Adapter.MusicImportAdapter;
import com.music.fm544.Bean.MusicImport;
import com.music.fm544.Bean.MusicPO;
import com.music.fm544.MyApplication;
import com.music.fm544.R;
import com.music.fm544.Service.MusicService;
import com.music.fm544.Views.PlayMusicTab;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImportFragment extends Fragment implements MusicImportAdapter.InnerItemOnclickListener,AdapterView.OnItemClickListener{

    //适配器
    private MusicImportAdapter adapter;
    //数据源
    private List<MusicImport> musics = new ArrayList<>();

     private ListView listView;
    //全选按钮
     private CheckBox checkBox;
    //扫描歌曲按钮
     private Button scan_btn;
    //全选文本
    private TextView chooseText;
    //导入按钮
    private Button import_btn;

    //本地广播
    private LocalBroadcastManager localBroadcastManager;


    //音乐服务相关
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

    public ImportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_import,container,false);
        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new MusicImportAdapter(getActivity(),musics);
        adapter.setOnInnerItemOnclickListener(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        //获取本地广播单例
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());

        scan_btn = view.findViewById(R.id.scaning_btn);
        checkBox = view.findViewById(R.id.choose_all);
        chooseText = view.findViewById(R.id.isAllChoose);
        import_btn = view.findViewById(R.id.import_btn);

        import_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击导入后存入数据库
                List<MusicPO> musicPOList = adapter.getChooseMusic();
                MyApplication app = (MyApplication) getActivity().getApplication();
                if(app.importMusicList(musicPOList)){
                    toPlayMusic(musicPOList.get(0));
                }
                Toast.makeText(getActivity(),musicPOList.size()+"首歌曲导入成功",Toast.LENGTH_SHORT).show();
                //发送本地广播，刷新数据
                Intent intent = new Intent("com.fm544.broadcast.MUSIC_IMPORT");
                localBroadcastManager.sendBroadcast(intent);
            }
        });

        //设置扫描歌曲监听事件
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = (String) scan_btn.getText();
                if (text.equals("扫描歌曲")){
                    scan_btn.setText("重新扫描");
                }else if(text.equals("重新扫描")){
                    checkBox.setChecked(false);
                    chooseText.setText("全选");
                }
                reinit();
                adapter.notifyIsAllCheck(false);
            }
        });

        //设置全选按钮监听事件
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()){
                    chooseText.setText("取消全选");
                    adapter.notifyIsAllCheck(true);
                }else{
                    chooseText.setText("全选");
                    adapter.notifyIsAllCheck(false);
                }
            }
        });
        //绑定Msuic服务
        initBind();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消服务绑定
        if(isBindService){
            isBindService = false;
            getActivity().unbindService(conn);
        }
    }


    //绑定服务
    private void initBind() {
        MyApplication app = (MyApplication) getActivity().getApplication();
        mServiceIntent = app.getServiceIntent();
        //绑定service
        if (!isBindService){
            isBindService = true;
            getActivity().bindService(mServiceIntent,conn, Context.BIND_AUTO_CREATE);
        }
    }


    private void reinit(){
        MyApplication app = (MyApplication) getActivity().getApplication();
        List<MusicPO> musicPOS = app.getLocalMusic();
        List<MusicImport> musicList = new ArrayList<>();
        for (MusicPO musicPO : musicPOS) {
            MusicImport music = new MusicImport(musicPO,false,false);
            musicList.add(music);
        }
        musics.clear();
        musics.addAll(musicList);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MusicPO music = (MusicPO) adapter.getItem(i);
        toPlayMusic(music);
    }

    private void toPlayMusic(MusicPO music){
        MyApplication app = (MyApplication) getActivity().getApplication();

        if (mMusicBind != null){
            mMusicBind.insertMusic(music);
            PlayMusicTab playMusicTab = getActivity().findViewById(R.id.music_tab);
            playMusicTab.initView();
        }
        Toast toast1 = Toast.makeText(getActivity(),"播放歌曲： "+music.getMusic_name(),Toast.LENGTH_SHORT);
        toast1.show();
    }

    @Override
    public void itemClick(View view) {
        int positon;
        positon = (Integer) view.getTag();
        if (view.getId() == R.id.item_music_choose){
            adapter.changeChooseStatus(positon);
        }
    }

}
