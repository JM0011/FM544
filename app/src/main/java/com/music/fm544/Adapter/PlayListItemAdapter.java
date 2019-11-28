package com.music.fm544.Adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.music.fm544.Bean.MusicListItem;
import com.music.fm544.R;

import java.util.List;

public class PlayListItemAdapter extends BaseAdapter implements View.OnClickListener{

    //data就是要显示的信息
    private List<MusicListItem> data;
    private Context context;
    private PlayListItemAdapter.InnerItemOnclickListener listener;

    static class Info
    {
        private ImageView iv_delete;
        private TextView tv_song;
        private TextView tv_singer;
    }
    public PlayListItemAdapter(Context context, List<MusicListItem> data)
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
        PlayListItemAdapter.Info info = null;
        if (view != null) {
            info = (PlayListItemAdapter.Info) view.getTag();
        }
        else{
            view = LayoutInflater.from(context).inflate(R.layout.play_list_item,null);
            info = new PlayListItemAdapter.Info();
            info.iv_delete = (ImageView)view.findViewById(R.id.item_delete);
            info.tv_song = (TextView)view.findViewById(R.id.music_name);
            info.tv_singer = (TextView)view.findViewById(R.id.singer_name);
            view.setTag(info);
        }
        MusicListItem music = (MusicListItem) getItem(i);
        info.iv_delete.setImageResource(R.mipmap.item_delete);
        info.tv_song.setText(music.getMusic_name());
        info.tv_singer.setText(music.getMusic_author());
        if (music.isPlaying()){
            info.tv_song.setTextColor(Color.rgb(255, 0, 0));
            info.tv_singer.setTextColor(Color.rgb(255, 0, 0));
        }else {
            info.tv_song.setTextColor(Color.rgb(0, 0, 0));
            info.tv_singer.setTextColor(Color.rgb(220, 220, 220));
        }
        info.iv_delete.setOnClickListener(this);
        info.iv_delete.setTag(i);
        return view;
    }


    public interface InnerItemOnclickListener{
        void itemClick(View view);
    }

    public void setOnInnerItemOnclickListener(PlayListItemAdapter.InnerItemOnclickListener listener){
        this.listener = listener;
    }
    @Override
    public void onClick(View view) {
        listener.itemClick(view);
    }


}
