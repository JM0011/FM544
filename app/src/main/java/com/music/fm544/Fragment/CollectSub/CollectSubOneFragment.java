package com.music.fm544.Fragment.CollectSub;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.music.fm544.Adapter.CollectAlbumAdapter;
import com.music.fm544.R;
import com.music.fm544.bean.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectSubOneFragment extends Fragment {
    private ListView listView;


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
        CollectAlbumAdapter adapter = new CollectAlbumAdapter(getActivity(),R.layout.album_collect,list);
        listView.setAdapter(adapter);

        return view;
    }

    private List<Album> getData() {

        List<Album> list1 = new ArrayList<Album>();
        for (int i = 0; i < 10; i++) {
            Album m = new Album(R.drawable.background,"FM544","by FM544");
            list1.add(m);
        }
        return list1;

    }
}
