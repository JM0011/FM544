package com.music.fm544.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.music.fm544.Fragment.CollectFragment;
import com.music.fm544.Fragment.ImportFragment;
import com.music.fm544.Fragment.MineFragment;

/**
 * menu适配器
 */

public class MainMenuAdapter extends FragmentPagerAdapter{

    Fragment[] fragments = {new MineFragment(),new CollectFragment(),new ImportFragment()};

    public MainMenuAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
