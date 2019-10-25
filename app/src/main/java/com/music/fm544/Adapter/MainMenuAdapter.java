package com.music.fm544.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.music.fm544.Fragment.ImportFragment;
import com.music.fm544.Fragment.MineFragment;
import com.music.fm544.Fragment.SearchFragment;
/**
 * menu适配器
 */

public class MainMenuAdapter extends FragmentPagerAdapter{

    public MainMenuAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new MineFragment();
                break;
            case 1:
                fragment = new ImportFragment();
                break;
            case 2:
                fragment = new SearchFragment();
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
