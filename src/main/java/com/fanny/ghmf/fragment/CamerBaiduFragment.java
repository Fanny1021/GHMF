package com.fanny.ghmf.fragment;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.fanny.ghmf.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Fanny on 18/3/29.
 */

public class CamerBaiduFragment extends BaseFragment implements BaiduMap.OnMarkerClickListener {


    private Activity activity;
    private MapView baidumap;
    private BaiduMap mybaiduMap;
    private LatLng latLng;
    private View pop;
    private double latitude;
    private double longitude;
    private LinearLayout ll_map_click;
    private TextView tv_map_device_name;
    private LinearLayout ll_shade;
    private View view;


    @Override
    protected int setContView() {
        return R.layout.fragment_find1;
    }

    @Override
    protected void findViews(View rootView) {

        view = rootView;

        ll_shade = (LinearLayout) rootView.findViewById(R.id.rl_map_shade1);

        LoadMapSocket loadMapSocket = new LoadMapSocket();
        loadMapSocket.execute();

//        initData();
//
//        initView();
//
//        draw();
//
//        ll_shade.setVisibility(View.GONE);


    }

    private class LoadMapSocket extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean value) {
            super.onPostExecute(value);
            if (value) {

                initData();

                initView();

                draw();

                ll_shade.setVisibility(View.GONE);
            }
        }
    }


    private void initView() {

        pop = View.inflate(getActivity(), R.layout.pop, null);
        ll_map_click = (LinearLayout) pop.findViewById(R.id.ll_map_device_click);
        tv_map_device_name = (TextView) pop.findViewById(R.id.map_device_name);
        MapViewLayoutParams params = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)// 控件可以随地图移动
                .position(latLng)
                .width(MapViewLayoutParams.WRAP_CONTENT)
                .height(MapViewLayoutParams.WRAP_CONTENT)
                .build();
        baidumap.addView(pop, params);

        pop.setVisibility(View.INVISIBLE);

//        ll_map_click.setOnClickListener(this);
    }

    private void initData() {

//        baidumap = (MapView) view.findViewById(R.id.baidumap_only);
        FrameLayout subCenterView = (FrameLayout) view.findViewById(R.id.fl_map_father);
        ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        View view = View.inflate(mContext, R.layout.baidumap_layout, null);
        baidumap = (MapView) view.findViewById(R.id.baidumap_only);
        subCenterView.addView(view, param);

        // 去掉标尺
        baidumap.showScaleControl(false);
        // 去掉缩放按钮
        baidumap.showZoomControls(false);

        // 设置缩放级别
        // 1.X 3~18 2.X 3~19 3.4 3~20
        //        BaiduMap 控制Mapview：缩放、移动、旋转
        mybaiduMap = baidumap.getMap();
        MapStatusUpdate status = MapStatusUpdateFactory.zoomTo(3);// 3~19
        mybaiduMap.setMapStatus(status);

        // 设置中心点 默认是天安门
        latitude = 39.93923;
        longitude = 116.397428;
        latLng = new LatLng(39.93923, 116.397428);
        MapStatusUpdate status2 = MapStatusUpdateFactory.newLatLng(latLng);
        mybaiduMap.setMapStatus(status2);
    }

    private void draw() {

        ArrayList<BitmapDescriptor> icons = new ArrayList<>();
        BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.location);
        BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.geo);
        icons.add(icon1);
        icons.add(icon2);
        MarkerOptions option = new MarkerOptions();
        option.title("航天光华");
        option.icons(icons);
        option.position(latLng);
        option.draggable(true);// 设置可以拖拽
        mybaiduMap.addOverlay(option);

        option = new MarkerOptions().title("护理器1")
                .position(new LatLng(latitude + 0.009, longitude - 0.009)).icon(icon1);
        mybaiduMap.addOverlay(option);

        option = new MarkerOptions().title("护理器2")
                .position(new LatLng(latitude + 0.009, longitude + 0.009)).icon(icon1);
        mybaiduMap.addOverlay(option);

        option = new MarkerOptions().title("护理床1")
                .position(new LatLng(latitude - 0.009, longitude + 0.009)).icon(icon1);
        mybaiduMap.addOverlay(option);

        option = new MarkerOptions().title("护理床2")
                .position(new LatLng(latitude + 0.009, longitude - 0.009)).icon(icon1);
        mybaiduMap.addOverlay(option);

        option = new MarkerOptions().title("洗头机1")
                .position(new LatLng(latitude - 0.012, longitude)).icon(icon1);
        mybaiduMap.addOverlay(option);

        option = new MarkerOptions().title("洗头机2")
                .position(new LatLng(latitude, longitude - 0.016)).icon(icon1);
        mybaiduMap.addOverlay(option);

        option = new MarkerOptions().title("洗澡机1")
                .position(new LatLng(latitude + 0.012, longitude)).icon(icon1);
        mybaiduMap.addOverlay(option);

        option = new MarkerOptions().title("洗澡机2")
                .position(new LatLng(latitude, longitude + 0.016)).icon(icon1);
        mybaiduMap.addOverlay(option);

        // 处理Marker覆盖物点击事件
        mybaiduMap.setOnMarkerClickListener(this);

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        baidumap.onPause();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        baidumap.onDestroy();
        ButterKnife.reset(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();

    }
}
