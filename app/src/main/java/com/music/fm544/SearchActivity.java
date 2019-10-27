package com.music.fm544;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.music.fm544.Adapter.MusicSearchAdapter;
import com.music.fm544.bean.Music;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        list = findViewById(R.id.listview);
        List<Music> list1 = getData();
        MusicSearchAdapter adapter = new MusicSearchAdapter(getApplicationContext(),R.layout.item_mine_music,list1);
        list.setAdapter(adapter);

    }

    private List<Music> getData() {

        List<Music> list1 = new ArrayList<Music>();
        for (int i = 0; i < 10; i++) {
            Music m = new Music();
            m.setImgId(R.drawable.song);
            m.setSongName("音乐");
            m.setSinger("作者");
            list1.add(m);
        }
        return list1;

    }


}
