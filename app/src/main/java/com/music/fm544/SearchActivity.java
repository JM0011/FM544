package com.music.fm544;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.music.fm544.Adapter.MusicSearchAdapter;
import com.music.fm544.bean.Music;
import com.music.fm544.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initStatusBar();

        list = findViewById(R.id.listview);
        List<Music> list1 = getData();
        MusicSearchAdapter adapter = new MusicSearchAdapter(getApplicationContext(),R.layout.item_mine_music,list1);
        list.setAdapter(adapter);

        ImageView back =  (ImageView) this.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private List<Music> getData() {

        List<Music> list1 = new ArrayList<Music>();
        for (int i = 0; i < 20; i++) {
            Music m = new Music();
            m.setImgId(R.drawable.song);
            m.setSongName("成都");
            m.setSinger("赵磊");
            list1.add(m);
        }
        return list1;

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
