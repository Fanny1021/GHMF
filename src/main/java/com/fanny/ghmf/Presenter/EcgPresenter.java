package com.fanny.ghmf.Presenter;

import android.content.Context;
import android.util.Log;

import com.fanny.ghmf.adapter.BloodOxyAdapter;
import com.fanny.ghmf.adapter.EcgAdapter;
import com.fanny.ghmf.bean.BloodOXyInfo;
import com.fanny.ghmf.bean.BloodOxyValueInfo;
import com.fanny.ghmf.bean.EcgInfo;
import com.fanny.ghmf.bean.EcgValueInfo;
import com.fanny.ghmf.bean.ResponseInfo;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by Fanny on 18/4/2.
 */

public class EcgPresenter extends BasePresenter {
    private String TAG = "EcgPresenter";
    private String username;
    private ArrayList<EcgValueInfo> list=new ArrayList<>();
    private Context mContext;
    private EcgAdapter adapter;

    public EcgPresenter(String username, EcgAdapter adapter) {
        this.username = username;
        this.adapter=adapter;
    }

    @Override
    protected void paraseJson(String json) {
        Log.e(TAG, json);
        Gson gson = new Gson();
        EcgInfo ecgInfo = gson.fromJson(json, EcgInfo.class);
        if (ecgInfo != null) {
            String category = ecgInfo.getCategory();
            list = ecgInfo.getList();
            adapter.setData(list);
        }else {

        }
    }

//    public ArrayList<EcgValueInfo> getEcgValueList() {
//
//        if (list != null) {
//            return list;
//        }
//
//        list.add(new EcgValueInfo("20170304","88"));
//        return list;
//    }

    @Override
    protected void showErrorMessage(String message) {
        Log.e(TAG,message);

        list.add(new EcgValueInfo("20170304","88"));
        adapter.setData(list);
    }

    public void getEcgData(String username, String cateogry) {
        Call<ResponseInfo> tempData = responseInfoApi.getHealthInfo(username, cateogry);
        tempData.enqueue(new CallBackAdapter());
    }
}
