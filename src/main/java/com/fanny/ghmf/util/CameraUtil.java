package com.fanny.ghmf.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.ImageButton;

import com.fanny.ghmf.service.BridgeService;
import com.fanny.ghmf.view.MyRender;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import vstc2.nativecaller.NativeCaller;

/**
 * Created by Fanny on 18/4/4.
 */

public class CameraUtil implements BridgeService.AddCameraInterface, BridgeService.IpcamClientInterface,
        BridgeService.CallBackMessageInterface, BridgeService.PlayInterface, CustomAudioRecorder.AudioRecordResult{

    private String TAG="CameraUtil";
    private Activity activity;
    private String deviceId;
    private WifiManager wifiManager;
    private MyRender myRender;

    public CameraUtil(Activity activity,String DeviceId,MyRender render){
        this.activity=activity;
        this.deviceId=DeviceId;
        this.myRender=render;
    }

    public void startService(){
        //初始化wifi管理
        Context context = activity.getApplicationContext();
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        //1.启动service服务
        Intent in = new Intent();
        in.setClass(activity, BridgeService.class);
        activity.startService(in);
        //2. 初始化服务器.注意：耗时操作后期要放在子线程中
//        NativeCaller.PPPPInitialOther("ADCBBFAOPPJAHGJGBBGLFLAGDBJJHNJGGMBFBKHIBBNKOKLDHOBHCBOEHOKJJJKJBPMFLGCPPJMJAPDOIPNL");
        new Thread(new Runnable() {
            @Override
            public void run() {
                NativeCaller.PPPPInitialOther("ADCBBFAOPPJAHGJGBBGLFLAGDBJJHNJGGMBFBKHIBBNKOKLDHOBHCBOEHOKJJJKJBPMFLGCPPJMJAPDOIPNL");
            }
        }).start();

        BridgeService.setAddCameraInterface(this);
        BridgeService.setCallBackMessage(this);

    }

    private int option = ContentCommon.INVALID_OPTION;
    private String strUser;
//    private String strDID;
    private String strPwd;
    private Bitmap mBmp;

    private static final int AUDIO_BUFFER_START_CODE = 0xff00ff;
    //视频数据部分
    private byte[] videodata = null;
    private int videoDataLen = 0;
    public int nVideoWidths = 0;
    public int nVideoHeights = 0;
    //相关参数
    private final int BRIGHT = 1;//亮度标志
    private final int CONTRAST = 2;//对比度标志
    private final int IR_STATE = 14;//IR夜视
    private int nResolution = 0;//分辨率值
    private int nBrightness = 0;//亮度值
    private int nContrast = 0;//对比度

    private int nStreamCodeType;//分辨率格式

    //?
    private String stqvga = "qvga";
    private String stvga = "vga";
    private String stqvga1 = "qvga1";
    private String stvga1 = "vga1";
    private String stp720 = "p720";
    private String sthigh = "high";
    private String stmiddle = "middle";
    private String stmax = "max";

    //分辨率标识符
    private boolean ismax = false;
    private boolean ishigh = false;
    private boolean isp720 = false;
    private boolean ismiddle = false;
    private boolean isqvga1 = false;
    private boolean isvga1 = false;
    private boolean isqvga = false;
    private boolean isvga = false;

    private Animation showAnim;
    private boolean isTakepic = false;
    private boolean isPictSave = false;
    private boolean isTalking = false;//是否在说话
    //    private boolean isMcriophone = false;//是否在
    //视频录像方法
//    private CustomVideoRecord myvideoRecorder;
    public boolean isH264 = false;//是否是H264格式标志
    public boolean isJpeg = false;
    private boolean isTakeVideo = false;
    private long videotime = 0;// 录每张图片的时间

    private Animation dismissAnim;
    private int timeTag = 0;
    private int timeOne = 0;
    private int timeTwo = 0;
    private ImageButton button_back;
    private BitmapDrawable drawable = null;
    //    private boolean bAudioRecordStart = false;
    //送话器
    private CustomAudioRecorder customAudioRecorder;

    //镜像标志
    private boolean m_bUpDownMirror;
    private boolean m_bLeftRightMirror;

    private int i = 0;//拍照张数标志
    private ImageButton btn_say;
    private ImageButton btn_hear;
    private ImageButton btn_up_control;
    private ImageButton btn_down_control;
    private ImageButton btn_right_control;
    private ImageButton btn_left_control;

    //默认视频参数
    private void defaultVideoParams() {
        nBrightness = 1;
        nContrast = 128;
        NativeCaller.PPPPCameraControl(deviceId, 1, 0);
        NativeCaller.PPPPCameraControl(deviceId, 2, 128);

    }

    //设置视频可见
    private void SetViewVisible() {
        getCameraParams();
    }

    private void getCameraParams() {
        NativeCaller.PPPPGetSystemParams(deviceId, ContentCommon.MSG_TYPE_GET_CAMERA_PARAMS);
    }


    /**
     * 获取resolution
     */
    public static Map<String, Map<Object, Object>> reslutionlist = new HashMap<String, Map<Object, Object>>();

    /**
     * @param Resolution
     */
    protected void setResolution(int Resolution) {
        NativeCaller.PPPPCameraControl(deviceId, 16, Resolution);
    }

    /**
     * 增加reslution
     *
     * @param mess
     * @param isfast
     */
    private void addReslution(String mess, boolean isfast) {
        if (reslutionlist.size() != 0) {
            if (reslutionlist.containsKey(deviceId)) {
                reslutionlist.remove(deviceId);
            }
        }
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put(mess, isfast);
        reslutionlist.put(deviceId, map);
    }

    /**
     * 设置分辨率
     */
    private void getReslution() {
        if (reslutionlist.containsKey(deviceId)) {
            Map<Object, Object> map = reslutionlist.get(deviceId);
            if (map.containsKey("qvga")) {
                isqvga = true;
            } else if (map.containsKey("vga")) {
                isvga = true;
            } else if (map.containsKey("qvga1")) {
                isqvga1 = true;
            } else if (map.containsKey("vga1")) {
                isvga1 = true;
            } else if (map.containsKey("p720")) {
                isp720 = true;
            } else if (map.containsKey("high")) {
                ishigh = true;
            } else if (map.containsKey("middle")) {
                ismiddle = true;
            } else if (map.containsKey("max")) {
                ismax = true;
            }
        }
    }

    /**
     * 处理视频数据的handler
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 || msg.what == 2) {
                SetViewVisible();
            }

            switch (msg.what) {

                case 0x07: //视频预览
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            BridgeService.setPlayInterface(this);
//                            //开启视频流画面
//                            NativeCaller.StartPPPPLivestream(strDID,10,1);
//                            getCameraParams();
//                        }
//                    });

                    break;
                case 1: // h264
                {
                    if (reslutionlist.size() == 0) {
                        if (nResolution == 0) {
                            ismax = true;
                            ismiddle = false;
                            ishigh = false;
                            isp720 = false;
                            isqvga1 = false;
                            isvga1 = false;
                            addReslution(stmax, ismax);
                        } else if (nResolution == 1) {
                            ismax = false;
                            ismiddle = false;
                            ishigh = true;
                            isp720 = false;
                            isqvga1 = false;
                            isvga1 = false;
                            addReslution(sthigh, ishigh);
                        } else if (nResolution == 2) {
                            ismax = false;
                            ismiddle = true;
                            ishigh = false;
                            isp720 = false;
                            isqvga1 = false;
                            isvga1 = false;
                            addReslution(stmiddle, ismiddle);
                        } else if (nResolution == 3) {
                            ismax = false;
                            ismiddle = false;
                            ishigh = false;
                            isp720 = true;
                            isqvga1 = false;
                            isvga1 = false;
                            addReslution(stp720, isp720);
                            nResolution = 3;
                        } else if (nResolution == 4) {
                            ismax = false;
                            ismiddle = false;
                            ishigh = false;
                            isp720 = false;
                            isqvga1 = false;
                            isvga1 = true;
                            addReslution(stvga1, isvga1);
                        } else if (nResolution == 5) {
                            ismax = false;
                            ismiddle = false;
                            ishigh = false;
                            isp720 = false;
                            isqvga1 = true;
                            isvga1 = false;
                            addReslution(stqvga1, isqvga1);
                        }
                    } else {
                        if (reslutionlist.containsKey(deviceId)) {
                            getReslution();
                        } else {
                            if (nResolution == 0) {
                                ismax = true;
                                ismiddle = false;
                                ishigh = false;
                                isp720 = false;
                                isqvga1 = false;
                                isvga1 = false;
                                addReslution(stmax, ismax);
                            } else if (nResolution == 1) {
                                ismax = false;
                                ismiddle = false;
                                ishigh = true;
                                isp720 = false;
                                isqvga1 = false;
                                isvga1 = false;
                                addReslution(sthigh, ishigh);
                            } else if (nResolution == 2) {
                                ismax = false;
                                ismiddle = true;
                                ishigh = false;
                                isp720 = false;
                                isqvga1 = false;
                                isvga1 = false;
                                addReslution(stmiddle, ismiddle);
                            } else if (nResolution == 3) {
                                ismax = false;
                                ismiddle = false;
                                ishigh = false;
                                isp720 = true;
                                isqvga1 = false;
                                isvga1 = false;
                                addReslution(stp720, isp720);
                                nResolution = 3;
                            } else if (nResolution == 4) {
                                ismax = false;
                                ismiddle = false;
                                ishigh = false;
                                isp720 = false;
                                isqvga1 = false;
                                isvga1 = true;
                                addReslution(stvga1, isvga1);
                            } else if (nResolution == 5) {
                                ismax = false;
                                ismiddle = false;
                                ishigh = false;
                                isp720 = false;
                                isqvga1 = true;
                                isvga1 = false;
                                addReslution(stqvga1, isqvga1);
                            }
                        }

                    }

                    myRender.writeSample(videodata, nVideoWidths, nVideoHeights);

                }
                break;
                case 2: // JPEG
                {
                    if (reslutionlist.size() == 0) {
                        if (nResolution == 1) {
                            isvga = true;
                            isqvga = false;
                            addReslution(stvga, isvga);
                        } else if (nResolution == 0) {
                            isqvga = true;
                            isvga = false;
                            addReslution(stqvga, isqvga);
                        }
                    } else {
                        if (reslutionlist.containsKey(deviceId)) {
                            getReslution();
                        } else {
                            if (nResolution == 1) {
                                isvga = true;
                                isqvga = false;
                                addReslution(stvga, isvga);
                            } else if (nResolution == 0) {
                                isqvga = true;
                                isvga = false;
                                addReslution(stqvga, isqvga);
                            }
                        }
                    }
                    mBmp = BitmapFactory.decodeByteArray(videodata, 0,
                            videoDataLen);
                    if (mBmp == null) {
                        bDisplayFinished = true;
                        return;
                    }
//                    if (isTakepic) {
//                        takePicture(mBmp);
//                        isTakepic = false;
//                    }
//                    if (mBmp.getWidth() > mBmp.getHeight()) {

                    nVideoWidths = mBmp.getWidth();
                    nVideoHeights = mBmp.getHeight();
//                    } else {
//                        nVideoWidths = mBmp.getHeight();
//                        nVideoHeights = mBmp.getWidth();
//                    }

//                    if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                        // Bitmap
//                        Bitmap bitmap = Bitmap.createScaledBitmap(mBmp, windowWidth,
//                                windowWidth * 3 / 4, true);
//                        //videoViewLandscape.setVisibility(View.GONE);
//                        videoViewPortrait.setVisibility(View.VISIBLE);
//                        videoViewPortrait.setImageBitmap(bitmap);
//
//                    } else if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                        Bitmap bitmap = Bitmap.createScaledBitmap(mBmp, windowWidth, windowHeight, true);
//                        videoViewPortrait.setVisibility(View.GONE);
//                        //videoViewLandscape.setVisibility(View.VISIBLE);
//                        //videoViewLandscape.setImageBitmap(bitmap);
//                    }

                }
                break;
                default:
                    break;
            }
            if (msg.what == 1 || msg.what == 2) {
                bDisplayFinished = true;
            }
        }

    };


    public void openCameraMointor() {
        //搜索设备
        beginSearch();

        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                if (isSerachted = true) {
                    connect();
                }

            }
        };
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                if (isSerachted = true) {
//                                            BridgeService.setPlayInterface(this);
//                                            //开启视频流画面
//                                            NativeCaller.StartPPPPLivestream(strDID, 10, 1);
//                                            getCameraParams();
                    cameraPic();
                }

            }
        };
        Timer timer = new Timer();
        timer.schedule(task1, 3000);
        timer.schedule(task2, 5000);

    }


    private void startSearch() {
        new Thread(new SearchThread()).start();
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // TODO: inflate a fragment view
//        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//        ButterKnife.inject(this, rootView);
//        return rootView;
//    }

    private class SearchThread implements Runnable {
        @Override
        public void run() {
            NativeCaller.StartSearch();
            Log.e("MainActivity", "startSearch");
        }
    }


    private void beginSearch() {
        //先断开连接
        stopCameraPPPP();
        SystemValue.deviceId = null;
        searchCamera();
    }

    private void searchCamera() {
        startSearch();
//        ......................
        //callBackSearchResultData()方法里获取搜索结果

    }

    //创建客户端与摄像头连接
    private void connect() {
        //用户信息
        strUser = "admin";
//        strDID = "VSTA-368340-HWBMZ";
        strPwd = "888888";
//        strPwd = "15701695175";

        Intent intent = new Intent();

        if (option == ContentCommon.INVALID_OPTION) {
            option = ContentCommon.ADD_CAMERA;
        }
        int CameraType = ContentCommon.CAMERA_TYPE_MJPEG;
        intent.putExtra(ContentCommon.CAMERA_OPTION, option);
        intent.putExtra(ContentCommon.STR_CAMERA_ID, deviceId);
        intent.putExtra(ContentCommon.STR_CAMERA_USER, strUser);
        intent.putExtra(ContentCommon.STR_CAMERA_PWD, strPwd);
        intent.putExtra(ContentCommon.STR_CAMERA_TYPE, CameraType);

        SystemValue.deviceName = strUser;
        SystemValue.deviceId = deviceId;
        SystemValue.devicePass = strPwd;

        BridgeService.setIpcamClientInterface(this);
        //3.初始化回调
        NativeCaller.Init();


        //开启p2p连接
        new Thread(new StartPPPPThread()).start();

        //以下方法可以返回连接状态i
        int i = NativeCaller.PPPPCameraStatus(deviceId, 0x7F);
        Log.e("status", String.valueOf(i));


    }

    private void cameraPic() {
        BridgeService.setPlayInterface(this);
        NativeCaller.StartPPPPLivestream(deviceId, 10, 1);
        getCameraParams();
    }


    private class StartPPPPThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(100);
                startCameraPPPP();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void startCameraPPPP() {
        if (SystemValue.deviceId.toLowerCase().startsWith("vsta")) {

            NativeCaller.StartPPPPExt(SystemValue.deviceId, SystemValue.deviceName,
                    SystemValue.devicePass, 1, "", "EFGFFBBOKAIEGHJAEDHJFEEOHMNGDCNJCDFKAKHLEBJHKEKMCAFCDLLLHAOCJPPMBHMNOMCJKGJEBGGHJHIOMFBDNPKNFEGCEGCBGCALMFOHBCGMFK");

            Log.e(TAG, "lianjie1");

            /**
             * 初始化camreapppp之后，通知主界面可以进行视频预览
             */

            Message msg = new Message();
            msg.what = 0x07;
            mHandler.sendMessage(msg);


//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
////                    BridgeService.setPlayInterface(this);
//                    //开启视频流画面
//                    NativeCaller.StartPPPPLivestream(strDID,10,1);
//                    getCameraParams();
//                }
//            });


        } else {
            NativeCaller.StartPPPP(SystemValue.deviceId, SystemValue.deviceName, SystemValue.devicePass, 1, "");
            Log.e(TAG, "lianjie2");
        }
    }


    public void stopCameraPPPP() {
        NativeCaller.StopPPPP(SystemValue.deviceId);
    }
    public void StopPPPPLivestream(String id){
        NativeCaller.StopPPPPLivestream(id);
    }

    private class MyBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("ip", "MainActivity.this.finish()");
        }
    }


    @Override
    public void BSMsgNotifyData(String did, int type, int param) {

        Log.e(TAG, "bsmsgNotifydata---"+"type:" + type + " param:" + param);

    }

    @Override
    public void BSSnapshotNotify(String did, byte[] bImage, int len) {

    }

    @Override
    public void callBackUserParams(String did, String user1, String pwd1, String user2, String pwd2, String user3, String pwd3) {

    }

    @Override
    public void CameraStatus(String did, int status) {

        Log.e(TAG,"camerastatus:"+ did + ";" + "status");
    }

    /**
     * playinterface的回调方法
     *
     * @param did
     * @param resolution
     * @param brightness
     * @param contrast
     * @param hue
     * @param saturation
     * @param flip
     * @param mode
     */
    //视频参数回调
    private boolean bInitCameraParam = false;

    @Override
    public void callBackCameraParamNotify(String did, int resolution, int brightness, int contrast, int hue, int saturation, int flip, int mode) {

        Log.e("设备返回的参数：", resolution + "," + brightness + "," + contrast + "," + hue + "," + saturation + "," + flip + "," + mode);
        nBrightness = brightness;
        nContrast = contrast;
        nResolution = resolution;
        bInitCameraParam = true;
    }

    private boolean bDisplayFinished = true;

    /**
     * 视频数据流回调   另外在此回到方法内处理拍照和录像
     *
     * @param videobuf 一帧视频数据
     * @param h264Data 0:普请摄像数据  1:高清摄像数据
     * @param len      一帧数据大小
     * @param width    一帧数据宽
     * @param height   一帧数据高
     */
    @Override
    public void callBackVideoData(byte[] videobuf, int h264Data, int len, int width, int height) {
        if (!bDisplayFinished) return;
        bDisplayFinished = false;
        videodata = videobuf;
        videoDataLen = len;
        Message msg = new Message();
        if (h264Data == 1) {
            nVideoWidths = width;
            nVideoHeights = height;
//            if(isTakepic){
//
//            }
            isH264 = true;
            msg.what = 1;
        } else {
            isJpeg = true;
            msg.what = 2;
        }
        mHandler.sendMessage(msg);

    }

    @Override
    public void callBackMessageNotify(String did, int msgType, int param) {

        if (msgType == ContentCommon.PPPP_MSG_TYPE_STREAM) {
            //设置分辨率格式
            nStreamCodeType = param;
            return;
        }
        if (msgType == ContentCommon.PPPP_MSG_TYPE_PPPP_STATUS) {
            return;
        }
        if (!did.equals(deviceId)) {
            return;
        }
        //其余情况下，通知ui已掉线，写一个handler：
    }

    @Override
    public void callBackAudioData(byte[] pcm, int len) {
//        if(!audioPlayer.isAudioPlaying()){
//            return;
//        }
//        CustomBufferHead head=new CustomBufferHead();
//        CustomBufferData data=new CustomBufferData();
//        head.length=len;
//        head.startcode=AUDIO_BUFFER_START_CODE;
//        data.head=head;
//        data.data=pcm;
//        AudioBuffer.addData(data);

    }

    @Override
    public void callBackH264Data(byte[] h264, int type, int size) {

    }

    public static boolean isSerachted = false;

    @Override
    public void callBackSearchResultData(int cameraType, String strMac, String strName, String strDeviceID, String strIpAddr, int port) {

        /**
         * 此回调里获取搜索结果
         */
        Log.e("SearchResult", strDeviceID + ";" + strName + ";" + strMac);
        if (strDeviceID.equals("VSTA368340HWBMZ")) {
//            connect();
            isSerachted = true;

        }
//        if(searchListAdapter.AddCamera(strMac,strName,strDeviceID)){
//            return;
//        }
    }

    @Override
    public void CallBackGetStatus(String did, String resultPbuf, int cmd) {

        if (cmd == ContentCommon.CGI_IEGET_STATUS) {
            Log.e("resultPbuf", "values:" + resultPbuf);
        }
    }

    /**
     * 对讲数据回调
     *
     * @param data
     * @param len
     */
    @Override
    public void AudioRecordData(byte[] data, int len) {
//        if(bAudioRecordStart && len >0){
//            NativeCaller.PPPPTalkAudioData(strDID,data,len);
//        }
    }

    //定义录像接口
    public void setVideoRecord(VideoRecorder videoRecord) {
        this.videoRecorder = videoRecord;
    }

    public VideoRecorder videoRecorder;

    public interface VideoRecorder {
        abstract public void VideoRecordData(int type, byte[] videodata, int width, int height, int time);
    }

}
