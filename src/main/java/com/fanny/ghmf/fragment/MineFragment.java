package com.fanny.ghmf.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanny.ghmf.MainActivity;
import com.fanny.ghmf.R;
import com.fanny.ghmf.UserEvent;
import com.fanny.ghmf.UserMsgActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Fanny on 17/10/10.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener{
    @InjectView(R.id.toolbar_device)
    RelativeLayout toolbarDevice;
    @InjectView(R.id.user_photo_mine)
    ImageView userPhotoMine;
    @InjectView(R.id.user_name_mine)
    TextView userNameMine;
    @InjectView(R.id.device_num_mine)
    TextView deviceNumMine;
    @InjectView(R.id.ll_userMsg_mine)
    LinearLayout llUserMsgMine;
    @InjectView(R.id.ll_web_health)
    LinearLayout llWebHealth;
    @InjectView(R.id.ll_market)
    LinearLayout llMarket;
    @InjectView(R.id.ll_equipment)
    LinearLayout llEquipment;
    @InjectView(R.id.ll_setup)
    LinearLayout llSetup;


    @Override
    protected int setContView() {
        return R.layout.fragment_mine;
    }


    @Override
    protected void findViews(View rootView) {

        ButterKnife.inject(this, rootView);

        initData();
        llUserMsgMine.setOnClickListener(this);

    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = View.inflate(getActivity(), R.layout.fragment_mine, null);
//
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                Window window = getActivity().getWindow();
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.setStatusBarColor(getResources().getColor(R.color.teal));
//                //底部导航栏
//                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        ButterKnife.inject(this, view);
//
//        initData();
//        llUserMsgMine.setOnClickListener(this);
//
//        return view;
//    }



    private void initData() {
        userNameMine.setText(MainActivity.account);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_userMsg_mine:

                Bundle bundle=new Bundle();
                bundle.putString("account",userNameMine.getText().toString());//这里人为将account写为姓名中文
                bundle.putString("psw",MainActivity.psw);

                Intent intent=new Intent(getActivity(),UserMsgActivity.class);
                intent.putExtra("data",bundle);
                startActivity(intent);
                break;
        }
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMsg(UserEvent event) {
        Log.e("eventbus",event.getName());

        userNameMine.setText(event.getName());
    }
}
