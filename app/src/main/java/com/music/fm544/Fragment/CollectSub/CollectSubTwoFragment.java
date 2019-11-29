package com.music.fm544.Fragment.CollectSub;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.music.fm544.Adapter.CollectSingerAdapter;
import com.music.fm544.Bean.Singer;
import com.music.fm544.Helps.MusicDao;
import com.music.fm544.MyApplication;
import com.music.fm544.R;
import com.music.fm544.SubActivity.SingerDetailActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectSubTwoFragment extends Fragment implements AdapterView.OnItemClickListener{
    private ListView listView;
    private CollectSingerAdapter mAdapter;


    public CollectSubTwoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collect_sub_two, container, false);

        listView = view.findViewById(R.id.listView);
        List<Singer> list = getData();
        mAdapter = new CollectSingerAdapter(getActivity(),list);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);



        return view;
    }

    private List<Singer> getData() {
        MyApplication app = (MyApplication) this.getActivity().getApplication();
        MusicDao musicDao = new MusicDao(app.getDatebaseHelper(),this.getActivity());

        return musicDao.get_singer_group_by_album();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Singer singers = (Singer) mAdapter.getItem(i);
        Intent intent = new Intent(getActivity().getApplicationContext(),SingerDetailActivity.class);
        intent.putExtra("singer",singers);
        startActivity(intent);
    }


}
