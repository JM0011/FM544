package com.music.fm544.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.music.fm544.Adapter.MusicImportAdapter;
import com.music.fm544.R;
import com.music.fm544.bean.MusicImport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImportFragment extends Fragment {

    //适配器
    private MusicImportAdapter adapter;
    //数据源
    private List<MusicImport> musics;

     private RecyclerView recyclerView;




    public ImportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view_default = inflater.inflate(R.layout.fragment_import_result, container, false);
        View view = inflater.inflate(R.layout.fragment_import,container,false);

        initMusic();
        setMusicAdapter(view);


        final Button scan_btn = view.findViewById(R.id.scaning_btn);

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = (String) scan_btn.getText();
                if (text.equals("扫描歌曲")){
                    recyclerView.setVisibility(View.VISIBLE);
                    scan_btn.setText("重新扫描");
                }else if(text.equals("重新扫描")){
                    reinit();
                    adapter.notifyDataSetChanged();
                }

            }
        });

        return view;
    }

    private void setMusicAdapter(View view) {
        adapter = new MusicImportAdapter(getActivity(),musics);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initMusic() {
        musics = new ArrayList<>();
        MusicImport music = new MusicImport(R.drawable.song,"成都","赵雷",false);
        MusicImport music1 = new MusicImport(R.drawable.song2,"红色高跟鞋","蔡健雅",false);
        MusicImport music2 = new MusicImport(R.drawable.song3,"雅俗共赏","许嵩",false);
        for (int i = 0; i < 8; i++) {
            musics.add(music);
            musics.add(music1);
            musics.add(music2);
        }

    }

    private void reinit(){
        musics.clear();
        Random random = new Random();
        int num = random.nextInt(3);
        MusicImport music2 = null;
        switch (num){
            case 0:
                music2 = new MusicImport(R.drawable.song3,"雅俗共赏","许嵩",false);
                break;
            case 1:
                music2 = new MusicImport(R.drawable.song2,"红色高跟鞋","蔡健雅",false);
                break;
            case 2:
                music2 = new MusicImport(R.drawable.song,"成都","赵雷",false);
                break;
        }
        for (int i = 0; i < 8; i++) {
            musics.add(music2);
        }
    }

}
