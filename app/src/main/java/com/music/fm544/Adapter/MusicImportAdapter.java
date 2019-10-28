package com.music.fm544.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.music.fm544.R;
import com.music.fm544.bean.MusicImport;

import java.util.List;


public class MusicImportAdapter extends RecyclerView.Adapter<MusicImportAdapter.ViewHolder>{
    Context context;
    private List<MusicImport> musics;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView songName;
        TextView singer;

        public ViewHolder (View view)
        {
            super(view);
            img = (ImageView) view.findViewById(R.id.item_music_img1);
            songName = (TextView) view.findViewById(R.id.item_music_name1);
            singer = (TextView) view.findViewById(R.id.item_music_name2);
        }

    }


    public MusicImportAdapter(Context context,List<MusicImport> musicList){
        this.context = context;
        this.musics = musicList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_import_music,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MusicImport songs = musics.get(position);
        holder.img.setImageResource(songs.getImgId());
        holder.songName.setText(songs.getSongName());
        holder.singer.setText(songs.getSinger());
    }


    @Override
    public int getItemCount() {
        return musics.size();
    }
}
