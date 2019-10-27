package com.music.fm544.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.music.fm544.Adapter.MusicImportAdapter;
import com.music.fm544.R;
import com.music.fm544.bean.MusicImport;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImportFragment extends Fragment {

    //适配器
    private MusicImportAdapter adapter;
    //数据源
    private List<MusicImport> musics;


    public ImportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_import, container, false);

        initMusic();

        adapter = new MusicImportAdapter(getActivity(),musics);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
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

}
