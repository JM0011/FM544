package com.music.fm544;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.music.fm544.Adapter.MainMenuAdapter;
import com.music.fm544.Utils.PermissionsUtils;
import com.music.fm544.Utils.StatusBarUtils;
import com.music.fm544.Views.PlayMusicTab;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    //菜单名
    private final int[] menu_title = new int[]{R.string.menu_mine,R.string.menu_collect,R.string.menu_import};
    //菜单图标
    private final int[] menu_img = new int[]{R.drawable.tab_main_mine_selector,R.drawable.tab_main_collect_selector,R.drawable.tab_main_import_selector};
    //记录tabLayout中的每个item的View
    private View[] imgViews = new View[3];


    //页卡适配器
    private PagerAdapter adapter;

    private PlayMusicTab musicTab;

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

        //获取读取本地文件的权限
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        PermissionsUtils.showSystemSetting = true;//是否支持显示系统设置权限设置窗口跳转
        //这里的this是Activity对象！
        PermissionsUtils.getInstance().chekPermissions(this, permissions, permissionsResult);



    }

    //创建监听权限的接口对象
    PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissons() {
//            Toast.makeText(MainActivity.this, "权限通过", Toast.LENGTH_SHORT).show();
            SharedPreferences sp = getSharedPreferences("share",MODE_PRIVATE);
            boolean isFirst = sp.getBoolean("isFirstIn",true);
            if (isFirst){
                targetView();
            }
        }

        @Override
        public void forbitPermissons() {
            finish();
            Toast.makeText(MainActivity.this, "权限获取失败，请授予应用权限后重启", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //就多一个参数this
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        musicTab = findViewById(R.id.music_tab);
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
            imgViews[i] = view.findViewById(R.id.img_tab);
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
                snackbar.getView().setBackgroundResource(R.color.main_back);
                snackbar.show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     *用户引导
     */
    public void targetView(){
        TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(imgViews[0], "FM544：音乐主界面","在这里你可以看到导入的歌曲、最近播放和喜爱的歌曲哦")
                                .outerCircleAlpha(0.94f)
                                .tintTarget(false)
                                .drawShadow(true)
                                .cancelable(false)
                                .id(1),
                        TapTarget.forView(imgViews[1], "FM544：歌曲分类", "在这里你可以看到歌曲按专辑和歌手的分类")
                                .outerCircleAlpha(0.94f)
                                .tintTarget(false)
                                .drawShadow(true)
                                .cancelable(false)
                                .id(2),
                        TapTarget.forView(findViewById(R.id.search_img),"FM544: 搜索界面","在这里你可以搜索本地歌曲")
                                .outerCircleAlpha(0.94f)
                                .tintTarget(false)
                                .drawShadow(true)
                                .cancelable(false)
                                .id(3),
                        TapTarget.forView(imgViews[2],"FM544: 导入界面","在这里你可以导入全部或特定的本地歌曲\n"+"快来导入歌曲开始使用吧!")
                                .outerCircleAlpha(0.94f)
                                .tintTarget(false)
                                .drawShadow(true)
                                .cancelable(false)
                                .id(4)
                )
                .listener(new TapTargetSequence.Listener() {

                    @Override
                    public void onSequenceFinish() {
                        //第一次进入显示引导后，将isFirstIn置为false
                        SharedPreferences sp = getSharedPreferences("share", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("isFirstIn",false);
                        editor.commit();
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {

                    }
                });
        sequence.start();
    }

    @Override
    protected void onDestroy() {
        MyApplication app = (MyApplication) getApplication();
        app.savePlayMusicList();
        super.onDestroy();
    }
}
