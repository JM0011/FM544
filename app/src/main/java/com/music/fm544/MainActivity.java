package com.music.fm544;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.music.fm544.Adapter.MainMenuAdapter;
import com.music.fm544.Utils.StatusBarUtils;
import com.music.fm544.Views.PlayMusicTab;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    //菜单名
    private final int[] menu_title = new int[]{R.string.menu_mine,R.string.menu_collect,R.string.menu_import};
    //菜单图标
    private final int[] menu_img = new int[]{R.drawable.tab_main_mine_selector,R.drawable.tab_main_collect_selector,R.drawable.tab_main_import_selector};

    //页卡适配器
    private PagerAdapter adapter;

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.search_img)
    ImageView searchBtn;
    @BindView(R.id.log_img)
    ImageView playingBtn;

    FragmentManager fragmentManager;

    //退出时间
    private long exitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initStatusBar();
        ButterKnife.bind(this);

        // 初始化页卡
        initPager();

        setTabs(tabLayout, getLayoutInflater(), menu_title, menu_img);

        searchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(i);
            }
        });

        playingBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,PlayingMusicActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        PlayMusicTab musicTab = findViewById(R.id.music_tab);
        musicTab.initView();
    }

    //设置状态栏颜色
    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtils.setStatusBarColor(MainActivity.this, R.color.statusTab);
        }
    }


    //设置页卡显示效果
    private void setTabs(TabLayout tabLayout, LayoutInflater inflater, int[] tabTitlees, int[] tabImgs) {
        for (int i = 0; i < tabImgs.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = inflater.inflate(R.layout.item_main_menu, null);
            // 使用自定义视图，目的是为了便于修改，也可使用自带的视图
            tab.setCustomView(view);

            ImageView imgTab = (ImageView) view.findViewById(R.id.img_tab);
            imgTab.setImageResource(tabImgs[i]);
            tabLayout.addTab(tab);
        }
    }

    private void initPager() {

        PlayMusicTab tab = findViewById(R.id.music_tab);
        tab.setFragmentManager(getFragmentManager());

        adapter = new MainMenuAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // 关联切换
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 取消平滑切换
                viewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            // 重写键盘事件分发，onKeyDown方法某些情况下捕获不到，只能在这里写
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Snackbar snackbar = Snackbar.make(viewPager, "再按一次退出程序", Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundResource(R.color.colorPrimary);
                snackbar.show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

}
