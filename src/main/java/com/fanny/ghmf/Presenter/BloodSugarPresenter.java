package com.fanny.ghmf.Presenter;

import android.content.Context;
import android.util.Log;

import com.fanny.ghmf.adapter.BloodPressureAdapter;
import com.fanny.ghmf.adapter.BloodSugarAdapter;
import com.fanny.ghmf.bean.BloodPressureInfo;
import com.fanny.ghmf.bean.BloodPressureValueInfo;
import com.fanny.ghmf.bean.BloodSugarInfo;
import com.fanny.ghmf.bean.BloodSugarValueInfo;
import com.fanny.ghmf.bean.ResponseInfo;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by Fanny on 18/4/2.
 */

public class BloodSugarPresenter extends BasePresenter {
    private String TAG = "BloodSugarPresenter";
    private String username;
    private ArrayList<BloodSugarValueInfo> list=new ArrayList<>();
    private Context mContext;
    private BloodSugarAdapter adapter;

    public BloodSugarPresenter(String username, BloodSugarAdapter adapter) {
        this.username = username;
        this.adapter=adapter;
    }

    @Override
    protected void paraseJson(String json) {
        Log.e(TAG, json);
        Gson gson = new Gson();
        BloodSugarInfo bloodsugarInfo = gson.fromJson(json, BloodSugarInfo.class);
        if (bloodsugarInfo != null) {
            String category = bloodsugarInfo.getCategory();
            list = bloodsugarInfo.getList();
            adapter.setData(list);
        }else {

        }
    }

    public ArrayList<BloodSugarValueInfo> getBloodSugarValueList() {

        if (list != null) {
            return list;
        }

        list.add(new BloodSugarValueInfo("20170304",81,79,80));
        return list;
    }

    @Override
    protected void showErrorMessage(String message) {
        Log.e(TAG,message);

        list.add(new BloodSugarValueInfo("20170304",81,79,80));
        adapter.setData(list);
    }

    public void getBloodSugarData(String username, String cateogry) {
        Call<ResponseInfo> tempData = responseInfoApi.getHealthInfo(username, cateogry);
        tempData.enqueue(new CallBackAdapter());
    }
}
