package com.music.fm544.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.music.fm544.Fragment.CollectSub.CollectSubOneFragment;
import com.music.fm544.Fragment.CollectSub.CollectSubThreeFragment;
import com.music.fm544.Fragment.CollectSub.CollectSubTwoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jm on 2019/10/26/0026.
        */

public class CollectSubFragmentAdapter extends FragmentPagerAdapter {
    private List<String> names ;


    public CollectSubFragmentAdapter(FragmentManager fm) {
        super(fm);
        this.names = new ArrayList<>();
    }

    public void setList(List<String> datas){
        this.names.clear();
        this.names.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
                fragment = new CollectSubOneFragment();
                bundle.putString("name", names.get(position));
                fragment.setArguments(bundle);
                break;
            case 1:
                fragment = new CollectSubTwoFragment();
                bundle.putString("name",names.get(position));
                fragment.setArguments(bundle);
                break;
            case 2:
                fragment = new CollectSubThreeFragment();
                bundle.putString("name", names.get(position));
                fragment.setArguments(bundle);
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String plateName = names.get(position);
        if (plateName == null) {
            plateName = "";
        } else if (plateName.length() > 15) {
            plateName = plateName.substring(0, 15) + "...";
        }
        return plateName;
    }
}
