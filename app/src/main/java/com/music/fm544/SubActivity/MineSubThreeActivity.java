package com.music.fm544.SubActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.music.fm544.PlayingMusicActivity;
import com.music.fm544.R;
import com.music.fm544.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineSubThreeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_sub_three);
        initStatusBar();

        //1.拿到ListView对象
        ListView listView = this.findViewById(R.id.listview);

        //2.准备数据源
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("item_music_img1",R.drawable.song);
        map.put("item_music_name1","成都");
        map.put("item_music_name2","赵雷");

        Map<String,Object> map2 = new HashMap<String, Object>();
        map2.put("item_music_img1",R.drawable.song2);
        map2.put("item_music_name1","红色高跟鞋");
        map2.put("item_music_name2","蔡健雅");

        for (int i = 0; i < 15; i++) {
            list.add(map);
            list.add(map2);
        }

        // 3.设置适配器
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,list, R.layout.item_mine_music,
                new String[]{"item_music_img1","item_music_name1","item_music_name2"},
                new int[]{R.id.item_music_img1,R.id.item_music_name1,R.id.item_music_name2});

        //关联适配器
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv_name = (TextView) view.findViewById(R.id.item_music_name1);
                TextView tv_name2 = (TextView) view.findViewById(R.id.item_music_name2);
                String song = tv_name.getText().toString();
                String songer = tv_name2.getText().toString();
                Intent intent = new Intent(MineSubThreeActivity.this,PlayingMusicActivity.class);
                intent.putExtra("songName",song);
                intent.putExtra("songer",songer);
                startActivity(intent);

            }
        });

        ImageView back =  (ImageView) this.findViewById(R.id.back_btn);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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
