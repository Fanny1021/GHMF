package com.fanny.ghmf.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.fanny.ghmf.BedActivity;
import com.fanny.ghmf.BedNewActivity;
import com.fanny.ghmf.ICActivity;
import com.fanny.ghmf.MainActivity;
import com.fanny.ghmf.MessageEvent;
import com.fanny.ghmf.R;
import com.fanny.ghmf.SearchActivity;
import com.fanny.ghmf.UserEvent;
import com.fanny.ghmf.UserMsgActivity;
import com.fanny.ghmf.adapter.DeciveAdapter;
import com.fanny.ghmf.bean.DeviceBean;
import com.fanny.ghmf.dialog.SocketConDialog;
import com.fanny.ghmf.util.SocketUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Fanny on 17/10/10.
 */

public class DeviceFragment extends BaseFragment implements View.OnClickListener {

    private ImageView img_menu;
    private ImageView img_add;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView dec_listview;
    private ArrayList<DeviceBean> devicelist;
    private DeciveAdapter myAdapter;
    private View headerView;
    private ImageView user_photo;
    private TextView user_name;
    private TextView steup_num;

    @Override
    protected int setContView() {
        return  R.layout.fragment_device;
    }



    @Override
    protected void findViews(View rootView) {


        /**
         * 抽屉 侧拉菜单
         */
        drawerLayout = (DrawerLayout) rootView.findViewById(R.id.device_drawlayout);

        img_menu = (ImageView) rootView.findViewById(R.id.img_device_menu);
        img_add = (ImageView) rootView.findViewById(R.id.img_device_add);
        img_menu.setOnClickListener(this);
        img_add.setOnClickListener(this);

        /**
         * 侧拉导航的控制事件
         */
        navigationView = (NavigationView) rootView.findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);//用户自定义图标颜色
        navigationView.setItemTextAppearance(R.style.NavigationItemTextStyle);
        setNavigationMenuLineStyle(navigationView, getResources().getColor(R.color.green), 5);

        //获取头布局文件
        headerView = navigationView.getHeaderView(0);
        user_photo = (ImageView) headerView.findViewById(R.id.user_photo_drawer);
        user_name = (TextView) headerView.findViewById(R.id.user_name_drawer);
        steup_num = (TextView) headerView.findViewById(R.id.user_num_setup_drawer);


        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserMsgActivity.class);
                String name;
                if (user_name.getText() != null) {
                    name = user_name.getText().toString();
                } else {
                    name = "未登录";
                }
                Bundle bundle = new Bundle();
                bundle.putString("account", name);
                intent.putExtra("data", bundle);
                startActivity(intent);
                drawerLayout.closeDrawers();
            }
        });

        /**
         *
         */
        //侧拉导航的每个item点击事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.drawer_device:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.drawer_manager:

                        break;
                    case R.id.drawer_socket:
                        /**
                         * socket连接dialog
                         */
                        drawerLayout.closeDrawers();
                        if (SocketUtil.socket != null ) {
//                            if(SocketUtil.socket.isConnected()){
                            try {
                                SocketUtil.socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                            }
                        }
                        SocketConDialog socketConDialog = new SocketConDialog();
                        socketConDialog.show(getFragmentManager(), "socket_con");
                        break;
                }
                return true;
            }
        });

        /**
         * 设备列表内容
         */
        dec_listview = (RecyclerView) rootView.findViewById(R.id.rcView_device);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        dec_listview.setLayoutManager(gridLayoutManager);

        /**
         * 初始化数据信息
         */
        initData();

        myAdapter = new DeciveAdapter(getContext(), devicelist);
        dec_listview.setAdapter(myAdapter);

        myAdapter.setOnItemClickListener(new DeciveAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                String deviceName = devicelist.get(position).getDevice_name();
                switch (deviceName) {
                    case "护理床":

                        Intent intent = new Intent();
                        intent.setClass(getActivity(), BedNewActivity.class);
                        startActivity(intent);

                        break;

                    case "护理器":

                        Intent intent1 = new Intent();
                        intent1.setClass(getActivity(), ICActivity.class);
                        startActivity(intent1);

                        break;
                    case "视屏监控":

                        break;
                    case "洗澡机":

                        break;
                    case "洗头机":

                        break;
                    case "机器人":

                        break;
                    case "无人机":

                        break;
                    default:
                        break;

                }
            }
        });

    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = View.inflate(getActivity(), R.layout.fragment_device, null);
//
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                Window window = getActivity().getWindow();
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.setStatusBarColor(getResources().getColor(R.color.green));
//                //底部导航栏
//                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//
//        /**
//         * 抽屉 侧拉菜单
//         */
//        drawerLayout = (DrawerLayout) view.findViewById(R.id.device_drawlayout);
//
//        img_menu = (ImageView) view.findViewById(R.id.img_device_menu);
//        img_add = (ImageView) view.findViewById(R.id.img_device_add);
//        img_menu.setOnClickListener(this);
//        img_add.setOnClickListener(this);
//
//        /**
//         * 侧拉导航的控制事件
//         */
//        navigationView = (NavigationView) view.findViewById(R.id.nav_view);
//        navigationView.setItemIconTintList(null);//用户自定义图标颜色
//        navigationView.setItemTextAppearance(R.style.NavigationItemTextStyle);
//        setNavigationMenuLineStyle(navigationView, getResources().getColor(R.color.green), 5);
//
//        //获取头布局文件
//        headerView = navigationView.getHeaderView(0);
//        user_photo = (ImageView) headerView.findViewById(R.id.user_photo_drawer);
//        user_name = (TextView) headerView.findViewById(R.id.user_name_drawer);
//        steup_num = (TextView) headerView.findViewById(R.id.user_num_setup_drawer);
//
//
//        headerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), UserMsgActivity.class);
//                String name;
//                if (user_name.getText() != null) {
//                    name = user_name.getText().toString();
//                } else {
//                    name = "未登录";
//                }
//                Bundle bundle = new Bundle();
//                bundle.putString("account", name);
//                intent.putExtra("data", bundle);
//                startActivity(intent);
//                drawerLayout.closeDrawers();
//            }
//        });
//
//        /**
//         *
//         */
//        //侧拉导航的每个item点击事件
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.drawer_device:
//                        drawerLayout.closeDrawers();
//                        break;
//                    case R.id.drawer_manager:
//
//                        break;
//                    case R.id.drawer_socket:
//                        /**
//                         * socket连接dialog
//                         */
//                        drawerLayout.closeDrawers();
//                        if (SocketUtil.socket != null ) {
////                            if(SocketUtil.socket.isConnected()){
//                                try {
//                                    SocketUtil.socket.close();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
////                            }
//                        }
//                        SocketConDialog socketConDialog = new SocketConDialog();
//                        socketConDialog.show(getFragmentManager(), "socket_con");
//                        break;
//                }
//                return true;
//            }
//        });
//
//        /**
//         * 设备列表内容
//         */
//        dec_listview = (RecyclerView) view.findViewById(R.id.rcView_device);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
//        dec_listview.setLayoutManager(gridLayoutManager);
//
//        /**
//         * 初始化数据信息
//         */
//        initData();
//
//        myAdapter = new DeciveAdapter(getContext(), devicelist);
//        dec_listview.setAdapter(myAdapter);
//
//        myAdapter.setOnItemClickListener(new DeciveAdapter.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(View view, int position) {
//                String deviceName = devicelist.get(position).getDevice_name();
//                switch (deviceName) {
//                    case "护理床":
//
//                        Intent intent = new Intent();
//                        intent.setClass(getActivity(), BedActivity.class);
//                        startActivity(intent);
//
//                        break;
//
//                    case "护理器":
//
//                        Intent intent1 = new Intent();
//                        intent1.setClass(getActivity(), ICActivity.class);
//                        startActivity(intent1);
//
//                        break;
//                    case "视屏监控":
//
//                        break;
//                    case "洗澡机":
//
//                        break;
//                    case "洗头机":
//
//                        break;
//                    case "机器人":
//
//                        break;
//                    case "无人机":
//
//                        break;
//                    default:
//                        break;
//
//                }
//            }
//        });


//        return view;
//    }




    private void initData() {

        /**
         * 用户信息
         */
        user_name.setText(MainActivity.account);

        /**
         * 将用户姓名/id发送服务器（网络操作），获取用户信息：设备数目＋设备名称，存储至下方的devicelist中
         */

        /********************************连网操作**********************************/

        /**
         * 用户设备列表信息（模拟数据）
         */
        devicelist = new ArrayList<>();
        devicelist.add(new DeviceBean("护理床", BitmapFactory.decodeResource(getResources(), R.mipmap.device_bed), 1));
        devicelist.add(new DeviceBean("护理器", BitmapFactory.decodeResource(getResources(), R.mipmap.device_hlq), 1));
        devicelist.add(new DeviceBean("视屏监控", BitmapFactory.decodeResource(getResources(), R.mipmap.device_camer), 1));
        devicelist.add(new DeviceBean("洗澡机", BitmapFactory.decodeResource(getResources(), R.mipmap.device_body), 1));
        devicelist.add(new DeviceBean("洗头机", BitmapFactory.decodeResource(getResources(), R.mipmap.device_head), 1));

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_device_menu:
                if (drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(Gravity.START);
                }
                break;
            case R.id.img_device_add:

                final Dialog dialog = new Dialog(getActivity(), R.style.ActionSheetDialogStyle);

                View view_add = View.inflate(getContext(), R.layout.dialog_device_add, null);
                TextView tv_add = (TextView) view_add.findViewById(R.id.dia_device_add);
                ImageView im_quit = (ImageView) view_add.findViewById(R.id.dia_add_quit);

                dialog.setContentView(view_add);
                Window dialogWindow = dialog.getWindow();
                dialogWindow.setGravity(Gravity.TOP);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.y = 20;
                dialogWindow.setAttributes(lp);
                dialog.show();

                tv_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                        startActivity(searchIntent);
                        dialog.dismiss();
                    }
                });

                im_quit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                break;
        }
    }


    /**
     * 自定义navigationstyle
     *
     * @param navigationView
     * @param color
     * @param height
     */
    public static void setNavigationMenuLineStyle(NavigationView navigationView, @ColorInt final int color, final int height) {
        try {
            Field fieldByPressenter = navigationView.getClass().getDeclaredField("mPresenter");
            fieldByPressenter.setAccessible(true);
            NavigationMenuPresenter menuPresenter = (NavigationMenuPresenter) fieldByPressenter.get(navigationView);
            Field fieldByMenuView = menuPresenter.getClass().getDeclaredField("mMenuView");
            fieldByMenuView.setAccessible(true);
            final NavigationMenuView mMenuView = (NavigationMenuView) fieldByMenuView.get(menuPresenter);
            mMenuView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
                @Override
                public void onChildViewAttachedToWindow(View view) {
                    RecyclerView.ViewHolder viewHolder = mMenuView.getChildViewHolder(view);
                    if (viewHolder != null && "SeparatorViewHolder".equals(viewHolder.getClass().getSimpleName()) && viewHolder.itemView != null) {
                        if (viewHolder.itemView instanceof FrameLayout) {
                            FrameLayout frameLayout = (FrameLayout) viewHolder.itemView;
                            View line = frameLayout.getChildAt(0);
                            line.setBackgroundColor(color);
                            line.getLayoutParams().height = height;
                            line.setLayoutParams(line.getLayoutParams());
                        }
                    }
                }

                @Override
                public void onChildViewDetachedFromWindow(View view) {

                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 使用eventbus接受数据
         */
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvevt(MessageEvent event) {
        Log.e("eventbus", event.getDeviceName());

        if (event.getDeviceName().equals("无人机")) {
            devicelist.add(new DeviceBean(event.getDeviceName(), BitmapFactory.decodeResource(getResources(), R.mipmap.device_airplane), 1));
            myAdapter.notifyDataSetChanged();
        }
        if (event.getDeviceName().equals("机器人")) {
            devicelist.add(new DeviceBean(event.getDeviceName(), BitmapFactory.decodeResource(getResources(), R.mipmap.device_robot), 1));
            myAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMsg(UserEvent event) {
        Log.e("eventbus", event.getName());

        user_name.setText(event.getName());
    }
}
