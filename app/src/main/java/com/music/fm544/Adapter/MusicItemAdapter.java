package com.music.fm544.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.music.fm544.Bean.MusicPO;
import com.music.fm544.R;

import java.util.List;


public class MusicItemAdapter extends BaseAdapter implements View.OnClickListener{

    //data就是要显示的信息
    private List<MusicPO> data;
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
    public MusicItemAdapter(Context context, List<MusicPO> data)
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
            info.imgview3 = (ImageView)view.findViewById(R.id.item_music_more);
            info.tv1 = (TextView)view.findViewById(R.id.item_music_name1);
            info.tv2 = (TextView)view.findViewById(R.id.item_music_name2);
            view.setTag(info);
        }
        MusicPO m = (MusicPO) getItem(i);

//        String url = Environment.getExternalStorageDirectory().getAbsolutePath() + m.getMusic_pic_path();
//        Glide.with(context)
//                .load(m.getMusic_pic_path())
//                .into(info.imgview);
        if (m.getMusic_pic_path() == null || m.getMusic_pic_path().equals("")){
            info.imgview.setImageResource(R.drawable.icon_logo);
        }else {
            Glide.with(context)
                    .load(m.getMusic_pic_path())
                    .into(info.imgview);
        }
        info.tv1.setText(m.getMusic_name());
        info.tv2.setText(m.getMusic_author());
        info.imgview3.setOnClickListener(this);
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
