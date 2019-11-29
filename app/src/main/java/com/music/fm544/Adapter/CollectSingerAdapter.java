package com.music.fm544.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.music.fm544.Bean.Singer;
import com.music.fm544.R;

import java.util.List;

/**
 * 收藏页面(歌手适配器)
 */
public class CollectSingerAdapter extends BaseAdapter {
    //data就是要显示的信息
    private List<Singer> data;
    private Context context;

    static class Info {
        ImageView imgview;
        TextView tv1;
        TextView tv2;
    }

    public CollectSingerAdapter(Context context, List<Singer> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CollectSingerAdapter.Info info = null;
        if (view != null) {
            info = (CollectSingerAdapter.Info) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.singer_collect, null);
            info = new CollectSingerAdapter.Info();
            info.imgview = (ImageView) view.findViewById(R.id.singer_img);
            info.tv1 = (TextView) view.findViewById(R.id.singer_name);
            info.tv2 = (TextView) view.findViewById(R.id.music_num);
            view.setTag(info);
        }

        Singer m = (Singer) getItem(i);
        //歌手图片
        Glide.with(context)
                .load(R.drawable.icon_logo)
                .into(info.imgview);
        info.tv1.setText(m.getSingerName());
        info.tv2.setText(m.getMusicNum());
        return view;
    }


}

