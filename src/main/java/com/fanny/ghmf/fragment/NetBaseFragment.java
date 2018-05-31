package com.fanny.ghmf.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanny.ghmf.util.HttpUtil;
import com.fanny.ghmf.util.UiUtils;
import com.fanny.ghmf.view.StateLayout;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * Created by Fanny on 17/12/13.
 */

public class NetBaseFragment<T> extends BaseFragment {

    protected int ACTION = 0;
    protected final int ACTION_INIT = 1;
    protected final int ACTION_PULL_DOWN = 2;
    protected final int ACTION_LOAD_MORE = 3;
    private StateLayout stateLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        stateLayout = new StateLayout(container.getContext());
        View normalView = inflater.inflate(getLayoutId(), container, false);
        stateLayout.addNormalView(normalView);
        initView(normalView);
        stateLayout.showNormal();
        ACTION = ACTION_INIT;
//        getPageData(0);
        return stateLayout;
    }

//    @Override
//    protected int setContView() {
//        return getLayoutId();
//    }
//
//    @Override
//    protected void findViews(View rootView) {
//        stateLayout = new StateLayout(rootView.getContext());
//        stateLayout.addNormalView(rootView);
//        initView(rootView);
//        stateLayout.showNormal();
//        ACTION = ACTION_INIT;
////        getPageData(0);
////        return stateLayout;
//    }

    /**
     * 控制下拉刷新和上拉加载的页数
     */
    protected int mCurrentPage = 0;

    private void getPageData(final int pageIndex) {
        if (pageIndex > 5) {
            UiUtils.showToast("没有更多数据");
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCurrentPage = pageIndex;
                /**
                 * 发送请求，获取数据
                 */
                String url = loadUrl(pageIndex);
                HttpUtil.get(url, listener);
            }
        }, 3000);
    }

    private OnResponseListener<String> listener=new OnResponseListener<String>() {
        //开启请求
        @Override
        public void onStart(int what) {
            if(ACTION==ACTION_INIT){
                stateLayout.showLoading();
            }
        }

        //请求成功
        @Override
        public void onSucceed(int what, Response<String> response) {
            String json=response.get();
            T data=processJson(json);
            showData(data);
        }

        //请求失败
        @Override
        public void onFailed(int what, Response<String> response) {
            if(ACTION==ACTION_INIT){
                response.getException().printStackTrace();
                stateLayout.showError();
            }
        }

        //结束成功
        @Override
        public void onFinish(int what) {
            if(ACTION==ACTION_INIT){
                stateLayout.showNormal();
            }
        }
    };
    protected void showData(T data) {

    }

    protected T processJson(String json) {
        return null;
    }

    protected String loadUrl(int pageIndex) {
        return null;
    }

    protected void initView(View normalView) {
    }

    protected int getLayoutId() {
        return 0;
    }
}
