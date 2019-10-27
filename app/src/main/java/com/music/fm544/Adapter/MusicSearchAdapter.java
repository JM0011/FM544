package com.music.fm544.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.music.fm544.R;
import com.music.fm544.bean.Music;

import java.util.List;


public class MusicSearchAdapter extends BaseAdapter {

    //data就是要显示的信息
    private List<Music> data;
    private Context context;
    private int resid;

    static class Info
    {
        ImageView imgview;
        TextView tv1;
        TextView tv2;
    }
    public MusicSearchAdapter(Context context,int resid,List<Music> data)
    {
        this.data = data;
        this.context = context;
        this.resid = resid;
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
        Info info = null;
        if (view != null) {
            info = (Info) view.getTag();
        }
        else{
            view = LayoutInflater.from(context).inflate(resid,null);
            info = new Info();
            info.imgview = (ImageView)view.findViewById(R.id.item_music_img1);
            info.tv1 = (TextView)view.findViewById(R.id.item_music_name1);
            info.tv2 = (TextView)view.findViewById(R.id.item_music_name2);
            view.setTag(info);
        }

        Music m = (Music) getItem(i);
        info.imgview.setImageResource(m.getImgId());
        info.tv1.setText(m.getSongName());
        info.tv2.setText(m.getSinger());
        return view;
    }

}
