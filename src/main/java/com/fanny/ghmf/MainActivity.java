package com.fanny.ghmf;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.ShapeBadgeItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.fanny.ghmf.fragment.BaseFragment;
import com.fanny.ghmf.fragment.CamerBaiduFragment;
import com.fanny.ghmf.fragment.CartFragment;
import com.fanny.ghmf.fragment.DeviceFragment;
import com.fanny.ghmf.fragment.FindFragment;
import com.fanny.ghmf.fragment.HealthCareFragment;
import com.fanny.ghmf.fragment.MineFragment;
import com.fanny.ghmf.fragment.NetBaseFragment;
import com.fanny.ghmf.util.SharePrefrenceUtil;
import com.fanny.ghmf.util.SocketUtil;
import com.fanny.ghmf.util.StatuBarsUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.R.id.message;
import static android.R.id.switch_widget;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    private String TAG = "MainActivity";
    int lastSelectedPosition = 0;
    TextBadgeItem numberBadgeItem;
    ShapeBadgeItem shapeBadgeItem;
    private BaseFragment deviceFragment;
    private BaseFragment healthFragment;
    private BaseFragment mineFragment;
    private NetBaseFragment cartFragment;
    private BaseFragment findFragment;

    private BottomNavigationBar bottomNavigationBar;
    public static String account;
    public static String psw;
    private ExecutorService mThreadPool;

    Socket socket = null;
    private List<BaseFragment> fragments;
    private Window window;

    @Override
    protected void onStart() {
        super.onStart();
        /**
         * 使用eventbus接受数据
         */
        EventBus.getDefault().register(this);

        /**
         * 开启自动连接socket 并且 重启时检验socket状态
         */
        if (SocketUtil.socket == null || SocketUtil.socket.isClosed()) {
            final String last_ip = (String) SharePrefrenceUtil.getData(this, "SocketIp", "");
            final String last_port = (String) SharePrefrenceUtil.getData(this, "SocketPort", "");
            if (!last_ip.equals("") && !last_port.equals("")) {
                Runnable runnable0 = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            socket = new Socket(last_ip, Integer.parseInt(last_port));
                            socket.setSoTimeout(500);
                            if (socket != null && socket.isConnected()) {
                                Log.e(TAG, "已连接");
                                SocketUtil.setSocket(socket);
                                SocketUtil.setConnectStaus(1);
                                EventBus.getDefault().post("3");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                };
                mThreadPool.execute(runnable0);
            }
        } else {
//            Log.e(TAG,"未连接");

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    private boolean receiveMsg = true;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMsg(String str) {
        switch (str) {
            case "3":
                Log.e(TAG, "event=3");
                /**
                 * 开始监听全局接受输入in数据流
                 */
                if (SocketUtil.socket != null) {
                    if (SocketUtil.socket.isConnected()) {
                        /**
                         * 接收服务器数据，保持监听
                         */
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                InputStream in = SocketUtil.getInputStream();
                                while (receiveMsg) {
                                    String RecMsg = "服务器返回消息";
                                    if (in != null) {
                                        try {
                                            byte buffer[] = new byte[1024 * 4];
                                            int temp = 0;
                                            while ((temp = in.read(buffer)) != -1) {
                                                Log.e("mainactivity", "temp:" + temp + " ");
                                                RecMsg = new String(buffer, 0, temp);
                                                Log.e("mainactivity", "RecMsg:" + RecMsg + " ");
                                                /**
                                                 * 将服务器发送来的数据event下去,发送给icactivitty
                                                 */
                                                Message message = new Message();
                                                message.what = 0x01;
                                                message.obj = RecMsg;
//                                               mainHandler.sendMessage(message);
                                                EventBus.getDefault().post(RecMsg);
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                    }
                                }
                            }
                        };

                        mThreadPool.execute(runnable);

                    }
                }
                break;
            case "4":
                Log.e(TAG, "event=4");
                break;
            case "5":
                Log.e(TAG, "5");
                break;
        }

    }


    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        } else {
////            StatuBarsUtil.setStatusBarColor(this, R.color.green);
//            flag = true;
//        }
        window = getWindow();

        setContentView(R.layout.activity_main);

        initUserMsg();

        initFragment();

        initBottomNavigationBar();

        /**
         * 设置进入初始化界面为设备界面
         */
        setScrollableText(0);

        /**
         * 创建线程池
         */
        mThreadPool = Executors.newCachedThreadPool();

    }

    private void initUserMsg() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        account = bundle.getString("account");
        psw = bundle.getString("psw");
    }

    private void initBottomNavigationBar() {

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        numberBadgeItem = new TextBadgeItem()
                .setBorderWidth(4)
                .setBackgroundColorResource(R.color.blue)
                .setText("" + lastSelectedPosition);
        shapeBadgeItem = new ShapeBadgeItem()
                .setShapeColorResource(R.color.teal)
                .setGravity(Gravity.TOP | Gravity.END);


        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);

        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_white_24dp, "设备").setActiveColorResource(R.color.green))
                .addItem(new BottomNavigationItem(R.drawable.ic_favorite_white_24dp, "健康").setActiveColorResource(R.color.yellow))
                .addItem(new BottomNavigationItem(R.drawable.ic_book_white_24dp, "购物").setActiveColorResource(R.color.blue).setBadgeItem(shapeBadgeItem))
                .addItem(new BottomNavigationItem(R.drawable.ic_find_replace_white_24dp, "定位").setActiveColorResource(R.color.colorAccent).setBadgeItem(numberBadgeItem))
                .addItem(new BottomNavigationItem(R.drawable.ic_tv_white_24dp, "我的").setActiveColorResource(R.color.teal))
                .setFirstSelectedPosition(lastSelectedPosition > 3 ? 3 : lastSelectedPosition)
                .initialise();

    }

    private void initFragment() {
        fragments = new ArrayList<>();

        deviceFragment = new DeviceFragment();
        healthFragment = new HealthCareFragment();
        mineFragment = new MineFragment();
        cartFragment = new CartFragment();
//        cartFragment = new HealthCareFragment();
        findFragment = new FindFragment();

        fragments.add(deviceFragment);
        fragments.add(healthFragment);
        fragments.add(cartFragment);
        fragments.add(findFragment);
        fragments.add(mineFragment);

    }

    @Override
    public void onTabSelected(int position) {

        lastSelectedPosition = position;
        setScrollableText(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {
    }

    /**
     * 设置fragment
     *
     * @param position
     */
    private void setScrollableText(int position) {
        switch (position) {
            case 0:
                loadFragment(0);
//                getSupportFragmentManager().beginTransaction().replace(R.id.home_activity_frag_container, deviceFragment).commitAllowingStateLoss();
                break;
            case 1:
                loadFragment(1);

//                getSupportFragmentManager().beginTransaction().replace(R.id.home_activity_frag_container, healthFragment).commitAllowingStateLoss();
                break;
            case 2:
                loadFragment(2);
//                getSupportFragmentManager().beginTransaction().replace(R.id.home_activity_frag_container, cartFragment).commitAllowingStateLoss();
                break;
            case 3:
                loadFragment(3);
//                getSupportFragmentManager().beginTransaction().replace(R.id.home_activity_frag_container, findFragment).commitAllowingStateLoss();
                break;
            case 4:
                loadFragment(4);
//                getSupportFragmentManager().beginTransaction().replace(R.id.home_activity_frag_container, mineFragment).commitAllowingStateLoss();
                break;
            default:
//                getSupportFragmentManager().beginTransaction().replace(R.id.home_activity_frag_container, mineFragment).commitAllowingStateLoss();
                break;
        }
    }


    private void setStatusBarColor(int colorId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(colorId));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(colorId);
        }
    }

    private BaseFragment mFrag;

    //    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void loadFragment(int position) {

        switch (position) {
            case 0:
//                    window.setStatusBarColor(this.getResources().getColor(R.color.green));
//                    StatuBarsUtil.setStatusBarColor(this, R.color.green);
                setStatusBarColor(R.color.green);
                break;
            case 1:
//                    StatuBarsUtil.setStatusBarColor(this, R.color.yellow);
                setStatusBarColor(R.color.yellow);
                break;
            case 2:
//                    StatuBarsUtil.setStatusBarColor(this, R.color.blue);
                setStatusBarColor(R.color.blue);
                break;
            case 3:
//                    StatuBarsUtil.setStatusBarColor(this, R.color.colorAccent);
                setStatusBarColor(R.color.colorAccent);
                break;
            case 4:
//                    StatuBarsUtil.setStatusBarColor(this, R.color.teal);
                setStatusBarColor(R.color.teal);
                break;

        }
        //从集合中获取相对序号的Fragment
        BaseFragment f = fragments.get(position);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //首先判断mFrag 是否为空，如果不为，先隐藏起来，
        // 接着判断从List 获取的Fragment是否已经添加到Transaction中，如果未添加，添加后显示，如果已经添加，直接显示
        if (mFrag != null) {
            fragmentTransaction.hide(mFrag);
        }
        if (!f.isAdded()) {
            fragmentTransaction.add(R.id.home_activity_frag_container, f);
        } else {
            fragmentTransaction.show(f);

        }
        //将获取的Fragment 赋值给声明的Fragment 中，提交
        mFrag = f;
        fragmentTransaction.commit();
    }


    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
