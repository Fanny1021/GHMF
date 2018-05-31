package com.fanny.ghmf.Presenter;

import android.content.Context;
import android.util.Log;

import com.fanny.ghmf.adapter.BloodOxyAdapter;
import com.fanny.ghmf.adapter.SleepAdapter;
import com.fanny.ghmf.bean.BloodOXyInfo;
import com.fanny.ghmf.bean.BloodOxyValueInfo;
import com.fanny.ghmf.bean.ResponseInfo;
import com.fanny.ghmf.bean.SleepInfo;
import com.fanny.ghmf.bean.SleepValueInfo;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by Fanny on 18/4/2.
 */

public class SleepPresenter extends BasePresenter {
    private String TAG = "SleepPresenter";
    private String username;
    private ArrayList<SleepValueInfo> list=new ArrayList<>();
    private Context mContext;
    private SleepAdapter adapter;

    public SleepPresenter(String username, SleepAdapter adapter) {
        this.username = username;
        this.adapter=adapter;
    }

    @Override
    protected void paraseJson(String json) {
        Log.e(TAG, json);
        Gson gson = new Gson();
        SleepInfo sleepInfo = gson.fromJson(json, SleepInfo.class);
        if (sleepInfo != null) {
            String category = sleepInfo.getCategory();
            list = sleepInfo.getList();
            adapter.setData(list);
        }else {

        }
    }

    public ArrayList<SleepValueInfo> getSleepValueList() {

        if (list != null) {
            return list;
        }

        /**
         * 测试数据
         *
         */
        list.add(new SleepValueInfo("20170304",81,79,80));
        return list;
    }

    @Override
    protected void showErrorMessage(String message) {
        Log.e(TAG,message);
        /**
         * 测试adapter是否有效
         * 经过测试有效
         */

        list.add(new SleepValueInfo("20170304",81,79,80));
        adapter.setData(list);
    }

    public void getSleepData(String username, String cateogry) {
        Call<ResponseInfo> tempData = responseInfoApi.getHealthInfo(username, cateogry);
        tempData.enqueue(new CallBackAdapter());
    }
}
