package com.fanny.ghmf.Presenter;

import android.app.Activity;
import android.util.Log;

import com.fanny.ghmf.MainActivity;
import com.fanny.ghmf.bean.BedMotionInfo;
import com.fanny.ghmf.bean.RemoteDeviceInfo;
import com.fanny.ghmf.bean.ResponseInfo;
import com.fanny.ghmf.fragment.FindFragment;
import com.fanny.ghmf.util.MapUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by Fanny on 18/4/4.
 */

public class MapPresenter extends BasePresenter {

    private String TAG = "MapFragment";
    private MainActivity activity;
    private FindFragment findFragment;
    private MapUtil mapUtil;

    public MapPresenter(MainActivity activity, FindFragment fragment, MapUtil mapUtil) {
        this.activity = activity;
        this.findFragment = fragment;
        this.mapUtil = mapUtil;
    }


    @Override
    protected void paraseJson(String json) {
        Log.e(TAG, json);
        Gson gson = new Gson();
        RemoteDeviceInfo deviceInfo = gson.fromJson(json, RemoteDeviceInfo.class);
        if (deviceInfo != null) {
            ArrayList<RemoteDeviceInfo.DeviceMessageInfo> list = deviceInfo.getList();
            for (int i = 0; i < list.size(); i++) {
                String deviceName = list.get(i).getDeviceName();
                double latidute = list.get(i).getLatidute();
                double longitude = list.get(i).getLongitude();
                mapUtil.DrawOption(deviceName, latidute, longitude);
            }
        }
    }

    public void showData() {
        double latitude = 39.93923;
        double longitude = 116.397428;
        mapUtil.DrawOption("设备1", latitude - 0.009, longitude + 0.009);
        mapUtil.DrawOption("设备2", latitude - 0.009, longitude - 0.009);
    }

    @Override
    protected void showErrorMessage(String message) {
        Log.e(TAG, message);
        activity.showMessage(message);

    }

    public void getRemoteDevicelocationData(String username, String DeviceMsg) {
        Call<ResponseInfo> deviceLocationInfo = responseInfoApi.getDeviceLocationInfo(username, DeviceMsg);
        deviceLocationInfo.enqueue(new CallBackAdapter());
    }

}
