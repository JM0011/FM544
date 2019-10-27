package com.music.fm544.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.music.fm544.R;
import com.music.fm544.SubActivity.MineSubOneActivity;
import com.music.fm544.SubActivity.MineSubThreeActivity;
import com.music.fm544.SubActivity.MineSubTwoActivity;

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
        ListView listView = view.findViewById(R.id.label_list);

        //2.准备数据源
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("label_img",R.drawable.phone);
        map.put("label_name","本地音乐");
        map.put("label_img1",R.drawable.next);
        list.add(map);

        Map<String,Object> map1 = new HashMap<String, Object>();
        map1.put("label_img",R.drawable.recent_music);
        map1.put("label_name","最近播放");
        map1.put("label_img1",R.drawable.next);
        list.add(map1);

        Map<String,Object> map2 = new HashMap<String, Object>();
        map2.put("label_img",R.drawable.like);
        map2.put("label_name","我的喜爱");
        map2.put("label_img1",R.drawable.next);
        list.add(map2);



        // 3.设置适配器
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),list, R.layout.item_mine_label,
                new String[]{"label_img","label_name","label_img1"},
                new int[]{R.id.label_img,R.id.label_name,R.id.label_img1});

        //关联适配器
        listView.setAdapter(simpleAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv_name = (TextView) view.findViewById(R.id.label_name);
                String text = tv_name.getText().toString();
                Intent intent = null;
                if( text.equals("本地音乐")){
                    intent = new Intent(getActivity().getApplicationContext(),MineSubOneActivity.class);
                    startActivity(intent);
                }else if(text.equals("最近播放")){
                    intent = new Intent(getActivity().getApplicationContext(),MineSubTwoActivity.class);
                    startActivity(intent);

                }else if(text.equals("我的喜爱")){
                    intent = new Intent(getActivity().getApplicationContext(),MineSubThreeActivity.class);
                    startActivity(intent);
                }
            }
        });


        return view;
    }

}
