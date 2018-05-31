package com.fanny.ghmf.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.fanny.ghmf.R;

/**
 * Created by Fanny on 18/3/28.
 */

public class BaseFragment extends Fragment {

    protected Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = initParent(inflater, container);
        findViews(rootView);
        return rootView;
    }

    private View initParent(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_base_layout, container, false);
        LinearLayout subCenterView = (LinearLayout) rootView.findViewById(R.id.base_sub_fragment_layout);
        ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        View centerView = View.inflate(mContext, setContView(), null);
        subCenterView.addView(centerView, param);
        return rootView;
    }

    protected  int setContView(){
        return 0;
    }

    protected  void findViews(View rootView){}


}
