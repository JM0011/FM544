package com.music.fm544.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.music.fm544.R;
import com.music.fm544.Bean.Album;

import java.util.List;

/**
 * 收藏页面（专辑适配器）
 */
public class CollectAlbumAdapter extends BaseAdapter {

    //data就是要显示的信息
    private List<Album> data;
    private Context context;

    static class Info
    {
        ImageView imgview;
        TextView tv1;
        TextView tv2;
    }
    public CollectAlbumAdapter(Context context,List<Album> data)
    {
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
        CollectAlbumAdapter.Info info = null;
        if (view != null) {
            info = (CollectAlbumAdapter.Info) view.getTag();
        }
        else{
            view = LayoutInflater.from(context).inflate(R.layout.album_collect,null);
            info = new CollectAlbumAdapter.Info();
            info.imgview = (ImageView)view.findViewById(R.id.album_image);
            info.tv1 = (TextView)view.findViewById(R.id.album_name);
            info.tv2 = (TextView)view.findViewById(R.id.author);
            view.setTag(info);
        }

        Album m = (Album) getItem(i);
        if (m.getPicPath() == null || m.getPicPath().equals("")){
            Glide.with(context)
                    .load(R.drawable.album_default_img)
                    .into(info.imgview);
        }else {
            Glide.with(context)
                    .load(m.getPicPath())
                    .into(info.imgview);
        }

        info.tv1.setText(m.getAlbumName());
        info.tv2.setText(m.getSinger());
        return view;
    }




}
