package com.music.fm544.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.music.fm544.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {


    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        //1.拿到ListView对象
        ListView listView = view.findViewById(R.id.lv);

        //2.准备数据源
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("item_music_img1",R.drawable.song);
        map.put("item_music_name1","成都");
        map.put("item_music_name2","赵磊");
        list.add(map);

        Map<String,Object> map1 = new HashMap<String, Object>();
        map1.put("item_music_img1",R.drawable.song2);
        map1.put("item_music_name1","红色高跟鞋");
        map1.put("item_music_name2","蔡健雅");

        Map<String,Object> map2 = new HashMap<String, Object>();
        map2.put("item_music_img1",R.drawable.song3);
        map2.put("item_music_name1","雅俗共赏");
        map2.put("item_music_name2","许嵩");

        for (int i = 0; i < 5; i++) {
            list.add(map);
            list.add(map1);
            list.add(map2);
        }

        // 3.设置适配器
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),list, R.layout.item_mine_music,
                new String[]{"item_music_img1","item_music_name1","item_music_name2"},
                new int[]{R.id.item_music_img1,R.id.item_music_name1,R.id.item_music_name2});

        //关联适配器
        listView.setAdapter(simpleAdapter);

        return view;
    }

}
