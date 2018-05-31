package com.fanny.ghmf.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fanny.ghmf.HealthBaseActivity;
import com.fanny.ghmf.R;
import com.fanny.ghmf.util.ConstantUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Fanny on 17/10/10.
 */

public class HealthCareFragment extends BaseFragment {
    @InjectView(R.id.ll0)
    LinearLayout ll0;
    @InjectView(R.id.ll1)
    LinearLayout ll1;
    @InjectView(R.id.ll2)
    LinearLayout ll2;
    @InjectView(R.id.ll3)
    LinearLayout ll3;
    @InjectView(R.id.ll4)
    LinearLayout ll4;
    @InjectView(R.id.ll5)
    LinearLayout ll5;

    @Override
    protected int setContView() {
        return R.layout.fragment_health;
    }


    @Override
    protected void findViews(View rootView) {
        ButterKnife.inject(this, rootView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.ll0, R.id.ll1, R.id.ll2, R.id.ll3, R.id.ll4, R.id.ll5})
    public void onViewClicked(View view) {

        Intent intent = new Intent(getActivity(), HealthBaseActivity.class);
        String title = "数据";
        String name = "用户";
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.ll0:
                title = "体温数据";
                break;
            case R.id.ll1:
                title = "血压数据";
                break;
            case R.id.ll2:
                title = "血糖数据";
                break;
            case R.id.ll3:
                title = "血氧数据";
                break;
            case R.id.ll4:
                title = "心电数据";
                break;
            case R.id.ll5:
                title = "睡眠数据";
                break;
            default:
                break;
        }
        bundle.putString(ConstantUtil.TITLE, title);
        bundle.putString(ConstantUtil.NAME, name);
        intent.putExtra(ConstantUtil.DATA, bundle);
        startActivity(intent);
    }
}
