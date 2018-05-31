package com.fanny.ghmf.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.fanny.ghmf.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Fanny on 18/4/4.
 */

public class MapUtil {

    private Context mContext;
    private MapView mMapView;
    private BaiduMap mybaiduMap;//返回的map实例
    private LatLng latLng;//中心坐标
    private double latitude;

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private double longitude;


    public MapUtil(Context context, MapView mMap, LatLng latLng) {
        this.mContext = context;
        this.mMapView = mMap;
        this.latLng = latLng;
    }


    public BaiduMap drawMap(String title) {
        // 设置缩放级别
        // 1.X 3~18 2.X 3~19 3.4 3~20
        //        BaiduMap 控制Mapview：缩放、移动、旋转
        /**
         * 获取到地图map实例
         */
        mybaiduMap = mMapView.getMap();
        MapStatusUpdate status = MapStatusUpdateFactory.zoomTo(15);// 3~19
        mybaiduMap.setMapStatus(status);

        MapStatusUpdate status2 = MapStatusUpdateFactory.newLatLng(latLng);
        mybaiduMap.setMapStatus(status2);

        ArrayList<BitmapDescriptor> icons = new ArrayList<>();
        BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.location);
        BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.geo);
        icons.add(icon1);
        icons.add(icon2);
        MarkerOptions option = new MarkerOptions();
        option.title(title);
        option.icons(icons);
        option.position(latLng);
        option.draggable(true);// 设置可以拖拽
        mybaiduMap.addOverlay(option);

        return mybaiduMap;
    }

    public void DrawOption(String DeviceName,double latitude,double longitude) {
        BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.geo);
        MarkerOptions option = new MarkerOptions().title(DeviceName)
                .position(new LatLng(latitude, longitude )).icon(icon1);
        mybaiduMap.addOverlay(option);
    }
}
