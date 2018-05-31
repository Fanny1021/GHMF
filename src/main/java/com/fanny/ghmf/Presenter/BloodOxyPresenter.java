package com.fanny.ghmf.Presenter;

import android.content.Context;
import android.util.Log;

import com.fanny.ghmf.adapter.BloodOxyAdapter;
import com.fanny.ghmf.adapter.BloodSugarAdapter;
import com.fanny.ghmf.bean.BloodOXyInfo;
import com.fanny.ghmf.bean.BloodOxyValueInfo;
import com.fanny.ghmf.bean.BloodSugarInfo;
import com.fanny.ghmf.bean.BloodSugarValueInfo;
import com.fanny.ghmf.bean.ResponseInfo;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by Fanny on 18/4/2.
 */

public class BloodOxyPresenter extends BasePresenter {
    private String TAG = "BloodOxyPresenter";
    private String username;
    private ArrayList<BloodOxyValueInfo> list=new ArrayList<>();
    private Context mContext;
    private BloodOxyAdapter adapter;

    public BloodOxyPresenter(String username, BloodOxyAdapter adapter) {
        this.username = username;
        this.adapter=adapter;
    }

    @Override
    protected void paraseJson(String json) {
        Log.e(TAG, json);
        Gson gson = new Gson();
        BloodOXyInfo bloodoxyInfo = gson.fromJson(json, BloodOXyInfo.class);
        if (bloodoxyInfo != null) {
            String category = bloodoxyInfo.getCategory();
            list = bloodoxyInfo.getList();
            adapter.setData(list);
        }else {

        }
    }

    public ArrayList<BloodOxyValueInfo> getBloodOxyValueList() {

        if (list != null) {
            return list;
        }

        /**
         * 测试数据
         *
         */
        list.add(new BloodOxyValueInfo("20170304",81,79,80));
        return list;
    }

    @Override
    protected void showErrorMessage(String message) {
        Log.e(TAG,message);
        /**
         * 测试adapter是否有效
         * 经过测试有效
         */

        list.add(new BloodOxyValueInfo("20170304",81,79,80));
        adapter.setData(list);
    }

    public void getBloodOxyData(String username, String cateogry) {
        Call<ResponseInfo> tempData = responseInfoApi.getHealthInfo(username, cateogry);
        tempData.enqueue(new CallBackAdapter());
    }
}
