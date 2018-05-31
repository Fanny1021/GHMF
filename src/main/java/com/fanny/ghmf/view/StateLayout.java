package com.fanny.ghmf.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.fanny.ghmf.R;

/**
 * Created by Fanny on 17/12/13.
 */

public class StateLayout extends FrameLayout {

    private View view;
    private View normalView;
    private View errorView;
    private View loadingView;
    private View empty;

    public StateLayout(@NonNull Context context) {
        this(context,null,0);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.state_layout,this,false);
        this.addView(view);
        normalView = view.findViewById(R.id.normal);
        errorView = view.findViewById(R.id.error);
        loadingView = view.findViewById(R.id.loading);
        empty = view.findViewById(R.id.empty);
    }

    private void resetGone(){
        normalView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
    }

    /**
     * 加载视图显示
     */
    public void showLoading(){
        resetGone();
        loadingView.setVisibility(View.VISIBLE);
    }
    /**
     * 出错视图显示
     */
    public void showError(){
        resetGone();
        errorView.setVisibility(View.VISIBLE);
    }
    /**
     * 无数据视图显示
     */
    public void showEmpty(){
        resetGone();
        empty.setVisibility(View.VISIBLE);
    }
    /**
     * 正常数据视图显示
     */
    public void showNormal(){
        resetGone();
        normalView.setVisibility(View.VISIBLE);
    }

    /**
     * 扩充normal 根据页面实际内容设置
     */
    public void addNormalView(View newNormalView){
        this.normalView.setVisibility(GONE);
        this.normalView=newNormalView;
        this.addView(newNormalView);
    }
}
