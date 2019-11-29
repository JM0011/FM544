package com.music.fm544.Fragment.CollectSub;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.music.fm544.Adapter.CollectAlbumAdapter;
import com.music.fm544.Bean.Album;
import com.music.fm544.Helps.MusicDao;
import com.music.fm544.MyApplication;
import com.music.fm544.R;
import com.music.fm544.SubActivity.AlbumDetailActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectSubOneFragment extends Fragment implements AdapterView.OnItemClickListener{
    private ListView listView;
    private CollectAlbumAdapter mAdapter;


    public CollectSubOneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collect_sub_one, container, false);

        listView = view.findViewById(R.id.listView);
        List<Album> list = getData();
        mAdapter = new CollectAlbumAdapter(getActivity(),list);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);



        return view;
    }

    private List<Album> getData() {
        MyApplication app = (MyApplication) this.getActivity().getApplication();
        MusicDao musicDao = new MusicDao(app.getDatebaseHelper(),this.getActivity());

        return musicDao.get_music_group_by_album();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Album album = (Album) mAdapter.getItem(i);
        Intent intent = new Intent(getActivity().getApplicationContext(),AlbumDetailActivity.class);
        intent.putExtra("album",album);
        startActivity(intent);
    }

}
