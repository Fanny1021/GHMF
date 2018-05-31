package com.fanny.ghmf;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fanny.ghmf.bean.TempValueInfo;
import com.fanny.ghmf.fragment.FragmentRecord;
import com.fanny.ghmf.fragment.FragmentTrendency;
import com.fanny.ghmf.util.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

import devlight.io.library.ntb.NavigationTabBar;

public class HealthBaseActivity extends AppCompatActivity implements FragmentRecord.CallBack{

    private FragmentManager myManager;
    private List<Fragment> myFragmentList = new ArrayList<Fragment>();
    private ViewPager viewPager;
    private NavigationTabBar navigationTabBar;
    private String[] colors;
    private Toolbar toolbar;
    private FragmentRecord f1;
    private FragmentTrendency f2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_base);

        toolbar = (Toolbar) findViewById(R.id.health_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**
         * title代表不同的数据类型请求
         */
        Intent intent = getIntent();
        Bundle bundleExtra = intent.getBundleExtra(ConstantUtil.DATA);
        String title = bundleExtra.getString(ConstantUtil.TITLE);
        getSupportActionBar().setTitle(title);

        initFragment(title);

        initTopUI();

    }

    private void initFragment(String title) {

        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtil.TITLE, title);

        myManager = getSupportFragmentManager();

        f1 = new FragmentRecord();
        f1.setArguments(bundle);
        f2 = new FragmentTrendency();
        f2.setArguments(bundle);

        myFragmentList.add(f1);
        myFragmentList.add(f2);

    }

    @Override
    public void onClick(ArrayList<TempValueInfo> tempValueList) {
        f2.setTempList(tempValueList);
    }

    class myFragmentPagerAdapter extends FragmentPagerAdapter {

        FragmentManager fragmentManager;
        List<Fragment> fragmentList;

        public myFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.fragmentManager = fm;
            this.fragmentList = list;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }


    private void initTopUI() {

        viewPager = (ViewPager) findViewById(R.id.vp_content);
        viewPager.setAdapter(new myFragmentPagerAdapter(myManager, myFragmentList) {

            @Override
            public int getCount() {
                return myFragmentList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return myFragmentList.get(position);
            }
        });

        initTopTabBar();
    }

    private void initTopTabBar() {
        /**
         * 定义底部导航的颜色数组
         */
        colors = getResources().getStringArray(R.array.default_preview);

        navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_title);
        ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(new NavigationTabBar.Model.Builder(null, Color.parseColor(colors[0]))
                .title("纪录").build());
        models.add(new NavigationTabBar.Model.Builder(null, Color.parseColor(colors[1]))
                .title("趋势").build());

        navigationTabBar.setModels(models);

        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ALL);
        navigationTabBar.setBadgeGravity(NavigationTabBar.BadgeGravity.BOTTOM);
        navigationTabBar.setBadgePosition(NavigationTabBar.BadgePosition.CENTER);
        navigationTabBar.setTypeface("fonts/custom_font.ttf");
        navigationTabBar.setIsBadged(false);
        navigationTabBar.setIsTitled(true);
        navigationTabBar.setIsTinted(true);
        navigationTabBar.setIsBadgeUseTypeface(true);
        navigationTabBar.setBadgeBgColor(Color.RED);
        navigationTabBar.setBadgeTitleColor(Color.WHITE);
        navigationTabBar.setIsSwiped(true);
        navigationTabBar.setBgColor(Color.WHITE);
        navigationTabBar.setTitleSize(35);

        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
//                changeFragment(position);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_health, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.data_putin:

                break;
            case R.id.data_clean:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("是否清除数据？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
