package com.music.fm544.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.music.fm544.Bean.MusicImport;
import com.music.fm544.Bean.MusicPO;
import com.music.fm544.R;

import java.util.ArrayList;
import java.util.List;


public class MusicImportAdapter extends BaseAdapter implements View.OnClickListener{
    Context context;
    private List<MusicImport> musics;
    private Boolean isAllCheck = false;
    private InnerItemOnclickListener listener;


    static class Info {
        ImageView img;
        TextView songName;
        TextView singer;
        CheckBox check;
    }


    public MusicImportAdapter(Context context,List<MusicImport> musicList){
        this.context = context;
        this.musics = musicList;
    }



    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Info info = null;
        if (view != null) {
            info = (Info) view.getTag();
        }
        else{
            view = LayoutInflater.from(context).inflate(R.layout.item_import_music,null);
            info = new Info();
            info.img = (ImageView) view.findViewById(R.id.item_music_img1);
            info.songName = (TextView) view.findViewById(R.id.item_music_name1);
            info.singer = (TextView) view.findViewById(R.id.item_music_name2);
            info.check = (CheckBox) view.findViewById(R.id.item_music_choose);
            view.setTag(info);
        }

        MusicImport songs = (MusicImport) getItem(i);
        if (songs.getMusic_pic_path() == null || songs.getMusic_pic_path().equals("")){
            Glide.with(context)
                    .load(R.mipmap.default_music_img)
                    .into(info.img);
        }else {
            Glide.with(context)
                    .load(songs.getMusic_pic_path())
                    .into(info.img);
        }
        info.songName.setText(songs.getMusic_name());
        info.singer.setText(songs.getMusic_author());
        if(isAllCheck){
            info.check.setChecked(true);
        }else{
            info.check.setChecked(false);
        }
        info.check.setOnClickListener(this);
        info.check.setTag(i);
        return view;
    }

    //刷新所有的checkBox
    public void notifyIsAllCheck(boolean isAllCheck) {
        this.isAllCheck = isAllCheck;
        for (MusicImport music : musics) {
            music.setChoose(isAllCheck);
        }
        notifyDataSetChanged();
    }

    public List<MusicPO> getChooseMusic(){
        List<MusicPO> musicChoose = new ArrayList<>();
        for (MusicImport music : musics) {
            if (music.getChoose()){
                musicChoose.add((MusicPO)music);
            }
        }
        return musicChoose;
    }

    //改变单个数据源
    public void changeChooseStatus(int position){
        if(musics.get(position).getChoose()){
            musics.get(position).setChoose(false);
        }else {
            musics.get(position).setChoose(true);
        }
    }

    @Override
    public Object getItem(int i) {
        return musics.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return musics.size();
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
