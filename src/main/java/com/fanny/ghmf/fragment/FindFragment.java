package com.fanny.ghmf.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
import com.fanny.ghmf.MainActivity;
import com.fanny.ghmf.Presenter.MapPresenter;
import com.fanny.ghmf.R;
import com.fanny.ghmf.service.BridgeService;
import com.fanny.ghmf.util.CameraUtil;
import com.fanny.ghmf.util.ContentCommon;
import com.fanny.ghmf.util.CustomAudioRecorder;
import com.fanny.ghmf.util.MapUtil;
import com.fanny.ghmf.util.SystemValue;
import com.fanny.ghmf.view.MyRender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import vstc2.nativecaller.NativeCaller;

/**
 * Created by Fanny on 17/10/11.
 */

public class FindFragment extends BaseFragment implements BaiduMap.OnMarkerClickListener,
        View.OnClickListener {

    private String TAG = "FindFragment";
    //    @InjectView(R.id.baidumap)
    MapView baidumap;

    @InjectView(R.id.mysurfaceview)
    GLSurfaceView mySurface;
    @InjectView(R.id.videoview)
    ImageView videoViewPortrait;
    @InjectView(R.id.videoview_standard)
    ImageButton videoViewStandard;
    @InjectView(R.id.im_zoom)
    ImageView imZoom;
    @InjectView(R.id.rl_srufaceview)
    RelativeLayout rlSrufaceview;

    @InjectView(R.id.rl_map_shade)
    LinearLayout rlMapShade;

    private BaiduMap mybaiduMap;
    private LinearLayout ll_map_click;
    private TextView tv_map_device_name;
    private LatLng latLng;
    private View pop;
    private double latitude;
    private double longitude;
    private MyRender myRender;
    private Activity activity;
    private int windowWidth;
    private int windowHeight;
    private ViewGroup.LayoutParams hisLp;
    private int hisWidth;
    private int hisHeight;
    private float density;
    private RelativeLayout fullscreen;

    private View view;
    private TextView tv_load;
    private WifiManager wifiManager;
    private LoadMapSocket loadMapSocket;

    @Override
    protected int setContView() {
        return R.layout.fragment_find;
    }

    @Override
    protected void findViews(View rootView) {
        view = rootView;
        ButterKnife.inject(this, rootView);

        /**
         * 获取窗口属性
         */
        windowWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
        windowHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
        DisplayMetrics dm = new DisplayMetrics();
        dm = activity.getResources().getDisplayMetrics();
        density = dm.density;
        /**
         * mainactivity横纵预留的全屏rl
         */
        fullscreen = (RelativeLayout) activity.findViewById(R.id.fullscreen_surface);

        /**
         * 初始化摄像头数据
         */
        initCameraView(view);

        /**
         * 地图遮罩
         */
        rlMapShade = (LinearLayout) rootView.findViewById(R.id.rl_map_shade);
        tv_load = (TextView) rootView.findViewById(R.id.tv_load);

        loadMapSocket = new LoadMapSocket();
        loadMapSocket.execute();

    }

    private class LoadMapSocket extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /**
             * 这里可以获取网络状态
             * 如果未联网，则显示连接失败或者加载离线地图
             * 联网则开始加载
             */
            //检测当前网络是否可用，当前可用的网络是属于WIFI还是MOBILE
            // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
            Context context = activity.getApplicationContext();
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            // 获取NetworkInfo对象
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    Log.e(TAG, "wifi");
//                    return networkInfo.isAvailable();
                    return true;
                } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
//                    return networkInfo.isAvailable();
                    return true;
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean value) {
            super.onPostExecute(value);
            if (value) {

                /**
                 * 通知页面处理数据处理数据
                 */
                DrawMapView();

                rlMapShade.setVisibility(View.GONE);
            } else {
                tv_load.setText("网络未连接");
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    private void drawPopView(LatLng latLng) {
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

        ll_map_click.setOnClickListener(this);
    }

    /**
     * 初始化百度地图的mapview
     */
    private void DrawMapView() {

        FrameLayout subCenterView = (FrameLayout) view.findViewById(R.id.fl_map_father);
        ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        View view = View.inflate(mContext, R.layout.baidumap_layout, null);
        baidumap = (MapView) view.findViewById(R.id.baidumap_only);
        if (subCenterView.getChildAt(0) != null) {
            subCenterView.removeAllViews();
        }
        subCenterView.addView(view, param);

        // 去掉标尺
        baidumap.showScaleControl(false);
        // 去掉缩放按钮
        baidumap.showZoomControls(false);

        // 设置初始中心点 默认是天安门
        latitude = 39.93923;
        longitude = 116.397428;
        latLng = new LatLng(latitude, longitude);
        drawPopView(latLng);

        //获取百度map实例
        MapUtil mapUtil = new MapUtil(getContext(), baidumap, latLng);
        mapUtil.setLatitude(39.93923);
        mapUtil.setLongitude(116.397428);
        mybaiduMap = mapUtil.drawMap("航天光华");

        /**
         * 添加设备位置
         * 这里的位置信息是从服务器端获取到的数据
         */
        MapPresenter mapPresenter = new MapPresenter((MainActivity) getActivity(), this, mapUtil);
        mapPresenter.getRemoteDevicelocationData("username", "all");
        mapPresenter.showData();

        mapUtil.DrawOption("设备1", latitude + 0.009, longitude - 0.009);
        mapUtil.DrawOption("设备2", latitude + 0.009, longitude + 0.009);

        mybaiduMap.setOnMarkerClickListener(this);

        rlMapShade.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (baidumap != null) {
            baidumap.onResume();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (baidumap != null) {

            baidumap.onPause();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (baidumap != null) {

            baidumap.onDestroy();
        }
        ButterKnife.reset(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        //  展示Pop控件，更新位置
        MapViewLayoutParams params = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)// 控件可以随地图移动
                .position(marker.getPosition())
                .width(MapViewLayoutParams.WRAP_CONTENT)
                .height(MapViewLayoutParams.WRAP_CONTENT)
                .yOffset(-10)
                .build();
        baidumap.updateViewLayout(pop, params);
        pop.setVisibility(View.VISIBLE);
        tv_map_device_name.setText(marker.getTitle());
        /**
         * 设备点击事件，是否打开监控
         */
        pop.setOnClickListener(this);


        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_map_device_click:
                /**
                 * 设备点击事件，是否打开监控
                 */
                MaterialDialog.Builder modeDialog = new MaterialDialog.Builder(getActivity());
                View view = View.inflate(getActivity(), R.layout.dialog_camera, null);
                modeDialog.customView(view, true);
                modeDialog.positiveText("确定")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                /**
                                 * 打开视频监控
                                 */

                                cameraUtil.openCameraMointor();


                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .show();
                break;

//            case R.id.im_zoom:
//                Toast.makeText(activity.getBaseContext(), "点击了缩放按钮", Toast.LENGTH_SHORT).show();
//                if (CamOriention == true) {
//
//                    ViewGroup.LayoutParams lp = mySurface.getLayoutParams();
//                    lp.width = windowWidth;
//                    lp.height = windowWidth * 9 / 16;
//                    mySurface.setLayoutParams(lp);
//
//                    CamOriention = !CamOriention;
//                } else {
//                    ViewGroup.LayoutParams lp = mySurface.getLayoutParams();
//                    lp.width = hisWidth;
//                    lp.height = hisHeight;
//                    mySurface.setLayoutParams(lp);
//
//                    CamOriention = !CamOriention;
//                }
//
//                break;

        }

    }

    private CameraUtil cameraUtil;
    private String DeviceId = "VSTA-368340-HWBMZ";

    private void initCameraView(View rootView) {

        mySurface = (GLSurfaceView) rootView.findViewById(R.id.mysurfaceview);
        /**
         * 实现视频画面置于顶层
         */
        mySurface.setZOrderOnTop(true);

        /**
         * 实现缩放按钮置于顶层
         */
        mySurface.setZOrderMediaOverlay(true);

        myRender = new MyRender(mySurface);
        mySurface.setRenderer(myRender);

        hisLp = mySurface.getLayoutParams();
        hisWidth = hisLp.width;
        hisHeight = hisLp.height;

        /**
         * 开启视频画面
         */
        cameraUtil = new CameraUtil(getActivity(), DeviceId, myRender);
        cameraUtil.startService();

//        //初始化wifi管理
//        Context context = activity.getApplicationContext();
//        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//
//        //1.启动service服务
//        Intent in = new Intent();
//        in.setClass(getActivity(), BridgeService.class);
//        getActivity().startService(in);
//        //2. 初始化服务器.注意：耗时操作后期要放在子线程中
////        NativeCaller.PPPPInitialOther("ADCBBFAOPPJAHGJGBBGLFLAGDBJJHNJGGMBFBKHIBBNKOKLDHOBHCBOEHOKJJJKJBPMFLGCPPJMJAPDOIPNL");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                NativeCaller.PPPPInitialOther("ADCBBFAOPPJAHGJGBBGLFLAGDBJJHNJGGMBFBKHIBBNKOKLDHOBHCBOEHOKJJJKJBPMFLGCPPJMJAPDOIPNL");
//            }
//        }).start();
//
//        BridgeService.setAddCameraInterface(this);
//        BridgeService.setCallBackMessage(this);

//        imZoom.setOnClickListener(this);

    }

//    private int option = ContentCommon.INVALID_OPTION;
//    private String strUser;
//    private String strDID;
//    private String strPwd;
//    private Bitmap mBmp;
//
//    private static final int AUDIO_BUFFER_START_CODE = 0xff00ff;
//    //视频数据部分
//    private byte[] videodata = null;
//    private int videoDataLen = 0;
//    public int nVideoWidths = 0;
//    public int nVideoHeights = 0;
//    //相关参数
//    private final int BRIGHT = 1;//亮度标志
//    private final int CONTRAST = 2;//对比度标志
//    private final int IR_STATE = 14;//IR夜视
//    private int nResolution = 0;//分辨率值
//    private int nBrightness = 0;//亮度值
//    private int nContrast = 0;//对比度
//
//    private int nStreamCodeType;//分辨率格式
//
//    //?
//    private String stqvga = "qvga";
//    private String stvga = "vga";
//    private String stqvga1 = "qvga1";
//    private String stvga1 = "vga1";
//    private String stp720 = "p720";
//    private String sthigh = "high";
//    private String stmiddle = "middle";
//    private String stmax = "max";
//
//    //分辨率标识符
//    private boolean ismax = false;
//    private boolean ishigh = false;
//    private boolean isp720 = false;
//    private boolean ismiddle = false;
//    private boolean isqvga1 = false;
//    private boolean isvga1 = false;
//    private boolean isqvga = false;
//    private boolean isvga = false;
//
//    private Animation showAnim;
//    private boolean isTakepic = false;
//    private boolean isPictSave = false;
//    private boolean isTalking = false;//是否在说话
//    //    private boolean isMcriophone = false;//是否在
//    //视频录像方法
////    private CustomVideoRecord myvideoRecorder;
//    public boolean isH264 = false;//是否是H264格式标志
//    public boolean isJpeg = false;
//    private boolean isTakeVideo = false;
//    private long videotime = 0;// 录每张图片的时间
//
//    private Animation dismissAnim;
//    private int timeTag = 0;
//    private int timeOne = 0;
//    private int timeTwo = 0;
//    private ImageButton button_back;
//    private BitmapDrawable drawable = null;
//    //    private boolean bAudioRecordStart = false;
//    //送话器
//    private CustomAudioRecorder customAudioRecorder;
//
//
//    //镜像标志
//    private boolean m_bUpDownMirror;
//    private boolean m_bLeftRightMirror;
//
//
//    private int i = 0;//拍照张数标志
//    private ImageButton btn_say;
//    private ImageButton btn_hear;
//    private ImageButton btn_up_control;
//    private ImageButton btn_down_control;
//    private ImageButton btn_right_control;
//    private ImageButton btn_left_control;
//
//
//    //默认视频参数
//    private void defaultVideoParams() {
//        nBrightness = 1;
//        nContrast = 128;
//        NativeCaller.PPPPCameraControl(strDID, 1, 0);
//        NativeCaller.PPPPCameraControl(strDID, 2, 128);
//
//    }
//
//    //设置视频可见
//    private void SetViewVisible() {
////        videoViewPortrait.setVisibility(View.VISIBLE);
////        videoViewStandard.setVisibility(View.VISIBLE);
//        getCameraParams();
//    }
//
//    private void getCameraParams() {
//        NativeCaller.PPPPGetSystemParams(strDID, ContentCommon.MSG_TYPE_GET_CAMERA_PARAMS);
//
//    }
//
//
//    /**
//     * 获取resolution
//     */
//    public static Map<String, Map<Object, Object>> reslutionlist = new HashMap<String, Map<Object, Object>>();
//
//    /**
//     * @param Resolution
//     */
//    protected void setResolution(int Resolution) {
//        NativeCaller.PPPPCameraControl(strDID, 16, Resolution);
//    }
//
//
//    /**
//     * 增加reslution
//     *
//     * @param mess
//     * @param isfast
//     */
//    private void addReslution(String mess, boolean isfast) {
//        if (reslutionlist.size() != 0) {
//            if (reslutionlist.containsKey(strDID)) {
//                reslutionlist.remove(strDID);
//            }
//        }
//        Map<Object, Object> map = new HashMap<Object, Object>();
//        map.put(mess, isfast);
//        reslutionlist.put(strDID, map);
//    }
//
//    /**
//     * 设置分辨率
//     */
//    private void getReslution() {
//        if (reslutionlist.containsKey(strDID)) {
//            Map<Object, Object> map = reslutionlist.get(strDID);
//            if (map.containsKey("qvga")) {
//                isqvga = true;
//            } else if (map.containsKey("vga")) {
//                isvga = true;
//            } else if (map.containsKey("qvga1")) {
//                isqvga1 = true;
//            } else if (map.containsKey("vga1")) {
//                isvga1 = true;
//            } else if (map.containsKey("p720")) {
//                isp720 = true;
//            } else if (map.containsKey("high")) {
//                ishigh = true;
//            } else if (map.containsKey("middle")) {
//                ismiddle = true;
//            } else if (map.containsKey("max")) {
//                ismax = true;
//            }
//        }
//    }
//
//    /**
//     * 处理视频数据的handler
//     */
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 1 || msg.what == 2) {
//                SetViewVisible();
//            }
////            if (!isPTZPrompt)
////            {
////                isPTZPrompt = true;
////                showToast(R.string.ptz_control);
////            }
//
//
////            windowWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
////            windowHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
//
//
//            switch (msg.what) {
//
//                case 0x07: //视频预览
////                    getActivity().runOnUiThread(new Runnable() {
////                        @Override
////                        public void run() {
////                            BridgeService.setPlayInterface(this);
////                            //开启视频流画面
////                            NativeCaller.StartPPPPLivestream(strDID,10,1);
////                            getCameraParams();
////                        }
////                    });
//
//                    break;
//                case 1: // h264
//                {
//                    if (reslutionlist.size() == 0) {
//                        if (nResolution == 0) {
//                            ismax = true;
//                            ismiddle = false;
//                            ishigh = false;
//                            isp720 = false;
//                            isqvga1 = false;
//                            isvga1 = false;
//                            addReslution(stmax, ismax);
//                        } else if (nResolution == 1) {
//                            ismax = false;
//                            ismiddle = false;
//                            ishigh = true;
//                            isp720 = false;
//                            isqvga1 = false;
//                            isvga1 = false;
//                            addReslution(sthigh, ishigh);
//                        } else if (nResolution == 2) {
//                            ismax = false;
//                            ismiddle = true;
//                            ishigh = false;
//                            isp720 = false;
//                            isqvga1 = false;
//                            isvga1 = false;
//                            addReslution(stmiddle, ismiddle);
//                        } else if (nResolution == 3) {
//                            ismax = false;
//                            ismiddle = false;
//                            ishigh = false;
//                            isp720 = true;
//                            isqvga1 = false;
//                            isvga1 = false;
//                            addReslution(stp720, isp720);
//                            nResolution = 3;
//                        } else if (nResolution == 4) {
//                            ismax = false;
//                            ismiddle = false;
//                            ishigh = false;
//                            isp720 = false;
//                            isqvga1 = false;
//                            isvga1 = true;
//                            addReslution(stvga1, isvga1);
//                        } else if (nResolution == 5) {
//                            ismax = false;
//                            ismiddle = false;
//                            ishigh = false;
//                            isp720 = false;
//                            isqvga1 = true;
//                            isvga1 = false;
//                            addReslution(stqvga1, isqvga1);
//                        }
//                    } else {
//                        if (reslutionlist.containsKey(strDID)) {
//                            getReslution();
//                        } else {
//                            if (nResolution == 0) {
//                                ismax = true;
//                                ismiddle = false;
//                                ishigh = false;
//                                isp720 = false;
//                                isqvga1 = false;
//                                isvga1 = false;
//                                addReslution(stmax, ismax);
//                            } else if (nResolution == 1) {
//                                ismax = false;
//                                ismiddle = false;
//                                ishigh = true;
//                                isp720 = false;
//                                isqvga1 = false;
//                                isvga1 = false;
//                                addReslution(sthigh, ishigh);
//                            } else if (nResolution == 2) {
//                                ismax = false;
//                                ismiddle = true;
//                                ishigh = false;
//                                isp720 = false;
//                                isqvga1 = false;
//                                isvga1 = false;
//                                addReslution(stmiddle, ismiddle);
//                            } else if (nResolution == 3) {
//                                ismax = false;
//                                ismiddle = false;
//                                ishigh = false;
//                                isp720 = true;
//                                isqvga1 = false;
//                                isvga1 = false;
//                                addReslution(stp720, isp720);
//                                nResolution = 3;
//                            } else if (nResolution == 4) {
//                                ismax = false;
//                                ismiddle = false;
//                                ishigh = false;
//                                isp720 = false;
//                                isqvga1 = false;
//                                isvga1 = true;
//                                addReslution(stvga1, isvga1);
//                            } else if (nResolution == 5) {
//                                ismax = false;
//                                ismiddle = false;
//                                ishigh = false;
//                                isp720 = false;
//                                isqvga1 = true;
//                                isvga1 = false;
//                                addReslution(stqvga1, isqvga1);
//                            }
//                        }
//
//                    }
//
//
////                    if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
////                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
////                                windowWidth, windowWidth * 3 / 4);
//////                        lp.gravity = Gravity.CENTER;
////                        mySurface.setLayoutParams(lp);
////                    } else if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
////                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
////                                windowWidth, windowHeight);
//////                        lp.gravity = Gravity.CENTER;
////                        mySurface.setLayoutParams(lp);
////                    }
//
//
//                    myRender.writeSample(videodata, nVideoWidths, nVideoHeights);
//
//                }
//                break;
//                case 2: // JPEG
//                {
//                    if (reslutionlist.size() == 0) {
//                        if (nResolution == 1) {
//                            isvga = true;
//                            isqvga = false;
//                            addReslution(stvga, isvga);
//                        } else if (nResolution == 0) {
//                            isqvga = true;
//                            isvga = false;
//                            addReslution(stqvga, isqvga);
//                        }
//                    } else {
//                        if (reslutionlist.containsKey(strDID)) {
//                            getReslution();
//                        } else {
//                            if (nResolution == 1) {
//                                isvga = true;
//                                isqvga = false;
//                                addReslution(stvga, isvga);
//                            } else if (nResolution == 0) {
//                                isqvga = true;
//                                isvga = false;
//                                addReslution(stqvga, isqvga);
//                            }
//                        }
//                    }
//                    mBmp = BitmapFactory.decodeByteArray(videodata, 0,
//                            videoDataLen);
//                    if (mBmp == null) {
//                        bDisplayFinished = true;
//                        return;
//                    }
////                    if (isTakepic) {
////                        takePicture(mBmp);
////                        isTakepic = false;
////                    }
////                    if (mBmp.getWidth() > mBmp.getHeight()) {
//
//                    nVideoWidths = mBmp.getWidth();
//                    nVideoHeights = mBmp.getHeight();
////                    } else {
////                        nVideoWidths = mBmp.getHeight();
////                        nVideoHeights = mBmp.getWidth();
////                    }
//
////                    if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
////                        // Bitmap
////                        Bitmap bitmap = Bitmap.createScaledBitmap(mBmp, windowWidth,
////                                windowWidth * 3 / 4, true);
////                        //videoViewLandscape.setVisibility(View.GONE);
////                        videoViewPortrait.setVisibility(View.VISIBLE);
////                        videoViewPortrait.setImageBitmap(bitmap);
////
////                    } else if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
////                        Bitmap bitmap = Bitmap.createScaledBitmap(mBmp, windowWidth, windowHeight, true);
////                        videoViewPortrait.setVisibility(View.GONE);
////                        //videoViewLandscape.setVisibility(View.VISIBLE);
////                        //videoViewLandscape.setImageBitmap(bitmap);
////                    }
//
//                }
//                break;
//                default:
//                    break;
//            }
//            if (msg.what == 1 || msg.what == 2) {
//                bDisplayFinished = true;
//            }
//        }
//
//    };
//
//
//
//    private static boolean CamOriention = true;
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
////        if(mySurface!=null){
////            /**
////             * 在activity中监听到横竖屏变化时调用播放器的监听方法来实现播放器大小切换
////             */
//////            mySurface.onConfigurationChanged(newConfig);
////            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
////                fullscreen.setVisibility(View.GONE);
////                fullscreen.removeAllViews();
////            }else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
////                fullscreen.addView(mySurface);
////                fullscreen.setVisibility(View.VISIBLE);
////            }
////        }
//        super.onConfigurationChanged(newConfig);
//    }
//
//
//
//
//    private void openCameraMointor() {
//        //搜索设备
//        beginSearch();
//
//        TimerTask task1 = new TimerTask() {
//            @Override
//            public void run() {
//                if (isSerachted = true) {
//                    connect();
//                }
//
//            }
//        };
//        TimerTask task2 = new TimerTask() {
//            @Override
//            public void run() {
//                if (isSerachted = true) {
////                                            BridgeService.setPlayInterface(this);
////                                            //开启视频流画面
////                                            NativeCaller.StartPPPPLivestream(strDID, 10, 1);
////                                            getCameraParams();
//                    cameraPic();
//                }
//
//            }
//        };
//        Timer timer = new Timer();
//        timer.schedule(task1, 3000);
//        timer.schedule(task2, 5000);
//
//    }
//
//
//    private void startSearch() {
//        new Thread(new SearchThread()).start();
//    }
//
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
////        // TODO: inflate a fragment view
////        View rootView = super.onCreateView(inflater, container, savedInstanceState);
////        ButterKnife.inject(this, rootView);
////        return rootView;
////    }
//
//    private class SearchThread implements Runnable {
//        @Override
//        public void run() {
//            NativeCaller.StartSearch();
//            Log.e("MainActivity", "startSearch");
//        }
//    }
//
//
//    private void beginSearch() {
//        //先断开连接
//        stopCameraPPPP();
//        SystemValue.deviceId = null;
//        searchCamera();
//    }
//
//    private void searchCamera() {
//        startSearch();
////        ......................
//        //callBackSearchResultData()方法里获取搜索结果
//
//    }
//
//    //创建客户端与摄像头连接
//    private void connect() {
//        //用户信息
//        strUser = "admin";
//        strDID = "VSTA-368340-HWBMZ";
//        strPwd = "888888";
////        strPwd = "15701695175";
//
//        Intent intent = new Intent();
//
//        if (option == ContentCommon.INVALID_OPTION) {
//            option = ContentCommon.ADD_CAMERA;
//        }
//        int CameraType = ContentCommon.CAMERA_TYPE_MJPEG;
//        intent.putExtra(ContentCommon.CAMERA_OPTION, option);
//        intent.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
//        intent.putExtra(ContentCommon.STR_CAMERA_USER, strUser);
//        intent.putExtra(ContentCommon.STR_CAMERA_PWD, strPwd);
//        intent.putExtra(ContentCommon.STR_CAMERA_TYPE, CameraType);
//
//        SystemValue.deviceName = strUser;
//        SystemValue.deviceId = strDID;
//        SystemValue.devicePass = strPwd;
//
//        BridgeService.setIpcamClientInterface(this);
//        //3.初始化回调
//        NativeCaller.Init();
//
//
//        //开启p2p连接
//        new Thread(new StartPPPPThread()).start();
//
//        //以下方法可以返回连接状态i
//        int i = NativeCaller.PPPPCameraStatus(strDID, 0x7F);
//        Log.e("status", String.valueOf(i));
//
//
//    }
//
//    private void cameraPic() {
//        BridgeService.setPlayInterface(this);
//        NativeCaller.StartPPPPLivestream(strDID, 10, 1);
//        getCameraParams();
//    }
//
//
//    private class StartPPPPThread implements Runnable {
//        @Override
//        public void run() {
//            try {
//                Thread.sleep(100);
//                startCameraPPPP();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//
//    private void startCameraPPPP() {
//        if (SystemValue.deviceId.toLowerCase().startsWith("vsta")) {
//
//            NativeCaller.StartPPPPExt(SystemValue.deviceId, SystemValue.deviceName,
//                    SystemValue.devicePass, 1, "", "EFGFFBBOKAIEGHJAEDHJFEEOHMNGDCNJCDFKAKHLEBJHKEKMCAFCDLLLHAOCJPPMBHMNOMCJKGJEBGGHJHIOMFBDNPKNFEGCEGCBGCALMFOHBCGMFK");
//
//            Log.e("MainActivity", "lianjie1");
//
//            /**
//             * 初始化camreapppp之后，通知主界面可以进行视频预览
//             */
//
//            Message msg = new Message();
//            msg.what = 0x07;
//            mHandler.sendMessage(msg);
//
//
////            getActivity().runOnUiThread(new Runnable() {
////                @Override
////                public void run() {
//////                    BridgeService.setPlayInterface(this);
////                    //开启视频流画面
////                    NativeCaller.StartPPPPLivestream(strDID,10,1);
////                    getCameraParams();
////                }
////            });
//
//
//        } else {
//            NativeCaller.StartPPPP(SystemValue.deviceId, SystemValue.deviceName, SystemValue.devicePass, 1, "");
//            Log.e("MainActivity", "lianjie2");
//        }
//    }
//
//    private void stopCameraPPPP() {
//        NativeCaller.StopPPPP(SystemValue.deviceId);
//    }
//
//    private class MyBroadCast extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.e("ip", "MainActivity.this.finish()");
//        }
//    }
//
//
//    @Override
//    public void BSMsgNotifyData(String did, int type, int param) {
//
//        Log.e("bsmsgNotifydata", "type:" + type + " param:" + param);
//
//    }
//
//    @Override
//    public void BSSnapshotNotify(String did, byte[] bImage, int len) {
//
//    }
//
//    @Override
//    public void callBackUserParams(String did, String user1, String pwd1, String user2, String pwd2, String user3, String pwd3) {
//
//    }
//
//    @Override
//    public void CameraStatus(String did, int status) {
//
//        Log.e("camerastatus", did + ";" + "status");
//    }
//
//    /**
//     * playinterface的回调方法
//     *
//     * @param did
//     * @param resolution
//     * @param brightness
//     * @param contrast
//     * @param hue
//     * @param saturation
//     * @param flip
//     * @param mode
//     */
//    //视频参数回调
//    private boolean bInitCameraParam = false;
//
//    @Override
//    public void callBackCameraParamNotify(String did, int resolution, int brightness, int contrast, int hue, int saturation, int flip, int mode) {
//
//        Log.e("设备返回的参数：", resolution + "," + brightness + "," + contrast + "," + hue + "," + saturation + "," + flip + "," + mode);
//        nBrightness = brightness;
//        nContrast = contrast;
//        nResolution = resolution;
//        bInitCameraParam = true;
//    }
//
//    private boolean bDisplayFinished = true;
//
//    /**
//     * 视频数据流回调   另外在此回到方法内处理拍照和录像
//     *
//     * @param videobuf 一帧视频数据
//     * @param h264Data 0:普请摄像数据  1:高清摄像数据
//     * @param len      一帧数据大小
//     * @param width    一帧数据宽
//     * @param height   一帧数据高
//     */
//    @Override
//    public void callBackVideoData(byte[] videobuf, int h264Data, int len, int width, int height) {
//        if (!bDisplayFinished) return;
//        bDisplayFinished = false;
//        videodata = videobuf;
//        videoDataLen = len;
//        Message msg = new Message();
//        if (h264Data == 1) {
//            nVideoWidths = width;
//            nVideoHeights = height;
////            if(isTakepic){
////
////            }
//            isH264 = true;
//            msg.what = 1;
//        } else {
//            isJpeg = true;
//            msg.what = 2;
//        }
//        mHandler.sendMessage(msg);
//
//    }
//
//    @Override
//    public void callBackMessageNotify(String did, int msgType, int param) {
//
//        if (msgType == ContentCommon.PPPP_MSG_TYPE_STREAM) {
//            //设置分辨率格式
//            nStreamCodeType = param;
//            return;
//        }
//        if (msgType == ContentCommon.PPPP_MSG_TYPE_PPPP_STATUS) {
//            return;
//        }
//        if (!did.equals(strDID)) {
//            return;
//        }
//        //其余情况下，通知ui已掉线，写一个handler：
//    }
//
//    @Override
//    public void callBackAudioData(byte[] pcm, int len) {
////        if(!audioPlayer.isAudioPlaying()){
////            return;
////        }
////        CustomBufferHead head=new CustomBufferHead();
////        CustomBufferData data=new CustomBufferData();
////        head.length=len;
////        head.startcode=AUDIO_BUFFER_START_CODE;
////        data.head=head;
////        data.data=pcm;
////        AudioBuffer.addData(data);
//
//    }
//
//    @Override
//    public void callBackH264Data(byte[] h264, int type, int size) {
//
//    }
//
//    public static boolean isSerachted = false;
//
//    @Override
//    public void callBackSearchResultData(int cameraType, String strMac, String strName, String strDeviceID, String strIpAddr, int port) {
//
//        /**
//         * 此回调里获取搜索结果
//         */
//        Log.e("SearchResult", strDeviceID + ";" + strName + ";" + strMac);
//        if (strDeviceID.equals("VSTA368340HWBMZ")) {
////            connect();
//            isSerachted = true;
//
//        }
////        if(searchListAdapter.AddCamera(strMac,strName,strDeviceID)){
////            return;
////        }
//    }
//
//    @Override
//    public void CallBackGetStatus(String did, String resultPbuf, int cmd) {
//
//        if (cmd == ContentCommon.CGI_IEGET_STATUS) {
//            Log.e("resultPbuf", "values:" + resultPbuf);
//        }
//    }
//
//    /**
//     * 对讲数据回调
//     *
//     * @param data
//     * @param len
//     */
//    @Override
//    public void AudioRecordData(byte[] data, int len) {
////        if(bAudioRecordStart && len >0){
////            NativeCaller.PPPPTalkAudioData(strDID,data,len);
////        }
//    }
//
//    //定义录像接口
//    public void setVideoRecord(VideoRecorder videoRecord) {
//        this.videoRecorder = videoRecord;
//    }
//
//    public VideoRecorder videoRecorder;
//
//    public interface VideoRecorder {
//        abstract public void VideoRecordData(int type, byte[] videodata, int width, int height, int time);
//    }


    @Override
    public void onStop() {
        super.onStop();
//        stopCameraPPPP();
        cameraUtil.stopCameraPPPP();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        NativeCaller.StopPPPPLivestream(strDID);
        cameraUtil.StopPPPPLivestream(DeviceId);

    }
}
