package com.music.fm544.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.music.fm544.Adapter.MusicImportAdapter;
import com.music.fm544.Bean.MusicImport;
import com.music.fm544.Bean.MusicPO;
import com.music.fm544.MyApplication;
import com.music.fm544.R;

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


        scan_btn = view.findViewById(R.id.scaning_btn);
        checkBox = view.findViewById(R.id.choose_all);
        chooseText = view.findViewById(R.id.isAllChoose);
        import_btn = view.findViewById(R.id.import_btn);

        import_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //导入后存入数据库
                MyApplication app = (MyApplication) getActivity().getApplication();
                app.importMusicList(adapter.getChooseMusic());
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

        return view;
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
