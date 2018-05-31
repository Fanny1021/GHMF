package com.fanny.ghmf.Presenter;

import android.util.Log;

import com.fanny.ghmf.bean.BedMotionInfo;
import com.fanny.ghmf.bean.ResponseInfo;
import com.fanny.ghmf.bean.TempInfo;
import com.fanny.ghmf.bean.TempValueInfo;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;

/**
 * Created by Fanny on 18/4/2.
 */

public class TempPresenter extends BasePresenter {
    private String TAG = "TempPresenter";
    private String username;
    private ArrayList<TempValueInfo> list;

    public TempPresenter(String username) {
        this.username = username;
    }

    @Override
    protected void paraseJson(String json) {
        Log.e(TAG, json);
        Gson gson = new Gson();
        TempInfo tempInfo = gson.fromJson(json, TempInfo.class);
        if (tempInfo != null) {
            String category = tempInfo.getCategory();
            list = tempInfo.getList();
        }else {
        }
    }

    public ArrayList<TempValueInfo> getTempValueList() {

        if (list != null) {
            return list;
        }

        /**
         * 测试数据
         */
        list=new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        Log.e(TAG, String.valueOf(System.currentTimeMillis()));
        String currentTime = formatter.format(curDate);
        Log.e(TAG,currentTime);
        list.add(new TempValueInfo("2018-03-02","35.6"));
        list.add(new TempValueInfo("2018-04-02","35.4"));
        list.add(new TempValueInfo("2018-04-02","35.4"));
        list.add(new TempValueInfo("2018-04-02","35.4"));
        list.add(new TempValueInfo("2018-04-02","35.4"));
        list.add(new TempValueInfo("2018-04-02","35.4"));

        return list;
    }

    @Override
    protected void showErrorMessage(String message) {
        Log.e(TAG,message);
    }

    public void getTempData(String username, String cateogry) {
        Call<ResponseInfo> tempData = responseInfoApi.getHealthInfo(username, cateogry);
        tempData.enqueue(new CallBackAdapter());
    }

}
