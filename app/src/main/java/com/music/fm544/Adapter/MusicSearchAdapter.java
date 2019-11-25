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


public class MusicSearchAdapter extends BaseAdapter implements View.OnClickListener{

    //data就是要显示的信息
    private List<Music> data;
    private Context context;
    private InnerItemOnclickListener listener;

    static class Info
    {
        private ImageView imgview;
        private ImageView imgview2;
        private ImageView imgview3;
        private TextView tv1;
        private TextView tv2;
    }
    public MusicSearchAdapter(Context context,List<Music> data)
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
        Info info = null;
        if (view != null) {
            info = (Info) view.getTag();
        }
        else{
            view = LayoutInflater.from(context).inflate(R.layout.item_mine_music,null);
            info = new Info();
            info.imgview = (ImageView)view.findViewById(R.id.item_music_img1);
            info.imgview2 = (ImageView)view.findViewById(R.id.item_music_play);
            info.imgview3 = (ImageView)view.findViewById(R.id.item_music_more);
            info.tv1 = (TextView)view.findViewById(R.id.item_music_name1);
            info.tv2 = (TextView)view.findViewById(R.id.item_music_name2);
            view.setTag(info);
        }
        Music m = (Music)getItem(i);
        info.imgview.setImageResource(R.drawable.music);
        info.tv1.setText(m.getSongName());
        info.tv2.setText(m.getSinger());
        info.imgview2.setOnClickListener(this);
        info.imgview3.setOnClickListener(this);
        info.imgview2.setTag(i);
        info.imgview3.setTag(i);
        return view;
    }


    public interface InnerItemOnclickListener{
        void itemClick(View view);
    }

    public void setOnInnerItemOnclickListener(InnerItemOnclickListener listener){
        this.listener = listener;
    }
    @Override
    public void onClick(View view) {
        listener.itemClick(view);
    }

}
