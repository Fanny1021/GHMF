package com.fanny.ghmf.Presenter;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.fanny.ghmf.BedNewActivity;
import com.fanny.ghmf.bean.BedMotionInfo;
import com.fanny.ghmf.bean.ResponseInfo;
import com.google.gson.Gson;

import retrofit2.Call;

/**
 * Created by Fanny on 18/4/2.
 */

public class BedPresenter extends BasePresenter {

    private BedNewActivity activity;
    private String TAG = "BedPresenter";

    public BedPresenter(BedNewActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void paraseJson(String json) {
        Log.e(TAG, json);
        Gson gson = new Gson();
        BedMotionInfo bedMotionInfo = gson.fromJson(json, BedMotionInfo.class);
        if (bedMotionInfo != null) {
            boolean bedsuccess = bedMotionInfo.getBedsuccess();
            if (bedsuccess) {
                /**
                 *说明发送数据控制护理床成功
                 */
            }
        }

    }

    @Override
    protected void showErrorMessage(String message) {
        Log.e(TAG,message);
        activity.showMessage(message);
    }

    public void getBedActData(String motion) {
        Call<ResponseInfo> bedactInfo = responseInfoApi.getBedInfo(motion);
        bedactInfo.enqueue(new CallBackAdapter());
    }
}
