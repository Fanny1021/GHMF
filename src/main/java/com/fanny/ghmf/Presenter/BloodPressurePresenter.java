package com.fanny.ghmf.Presenter;

import android.content.Context;
import android.util.Log;

import com.fanny.ghmf.adapter.BloodPressureAdapter;
import com.fanny.ghmf.bean.BloodPressureInfo;
import com.fanny.ghmf.bean.BloodPressureValueInfo;
import com.fanny.ghmf.bean.ResponseInfo;
import com.fanny.ghmf.bean.TempInfo;
import com.fanny.ghmf.bean.TempValueInfo;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by Fanny on 18/4/2.
 */

public class BloodPressurePresenter extends BasePresenter {
    private String TAG = "BloodPressurePresenter";
    private String username;
    private ArrayList<BloodPressureValueInfo> list=new ArrayList<>();
    private Context mContext;
    private BloodPressureAdapter adapter;

    public BloodPressurePresenter(String username,BloodPressureAdapter adapter) {
        this.username = username;
        this.adapter=adapter;
    }

    @Override
    protected void paraseJson(String json) {
        Log.e(TAG, json);
        Gson gson = new Gson();
        BloodPressureInfo bloodPressureInfo = gson.fromJson(json, BloodPressureInfo.class);
        if (bloodPressureInfo != null) {
            String category = bloodPressureInfo.getCategory();
            list = bloodPressureInfo.getList();
            adapter.setData(list);
        }else {

        }
    }

    public ArrayList<BloodPressureValueInfo> getBloodPressureValueList() {

        if (list != null) {
            return list;
        }

        /**
         * 测试数据
         *
         */
        list.add(new BloodPressureValueInfo("20170304",81,79,80,80));
        return list;
    }

    @Override
    protected void showErrorMessage(String message) {
        Log.e(TAG,message);
        /**
         * 测试adapter是否有效
         * 经过测试有效
         */

        list.add(new BloodPressureValueInfo("20170304",81,79,80,80));
        adapter.setData(list);
    }

    public void getBloodPressureData(String username, String cateogry) {
        Call<ResponseInfo> tempData = responseInfoApi.getHealthInfo(username, cateogry);
        tempData.enqueue(new CallBackAdapter());
    }
}
