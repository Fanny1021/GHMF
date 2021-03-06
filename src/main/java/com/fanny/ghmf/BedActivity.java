package com.fanny.ghmf;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.fanny.ghmf.util.SocketUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Fanny on 17/4/12.
 */

public class BedActivity extends Activity implements View.OnTouchListener {

    @InjectView(R.id.ll_vp)
    LinearLayout llVp;
    @InjectView(R.id.btn_turnleft)
    ImageButton btnTurnleft;
    @InjectView(R.id.btn_turnright)
    ImageButton btnTurnright;
    @InjectView(R.id.btn_liftfoot)
    ImageButton btnLiftfoot;
    @InjectView(R.id.btn_stayfoot)
    ImageButton btnStayfoot;
    @InjectView(R.id.btn_autioA)
    ImageButton btnAutioA;
    @InjectView(R.id.btn_autioB)
    ImageButton btnAutioB;
    @InjectView(R.id.btn_situp)
    ImageButton btnSitup;
    @InjectView(R.id.btn_liedown)
    ImageButton btnLiedown;
    @InjectView(R.id.btn_open_bedpan)
    ImageButton btnOpenBedpan;
    @InjectView(R.id.btn_close_bedpan)
    ImageButton btnCloseBedpan;
    @InjectView(R.id.btn_reset)
    ImageButton btnReset;

    char SendData1[] = {'$', 'A', 'A', '0', '0', '0', 'x'};   //
    char SendData2[] = {'$', 'A', 'B', '0', '0', '0', 'x'};   //
    char SendData3[] = {'$', 'A', 'C', '0', '0', '0', 'x'};   //
    char SendData4[] = {'$', 'A', 'D', '0', '0', '0', 'x'};   //
    char SendData5[] = {'$', 'A', 'E', '0', '0', '0', 'x'};   //
    char SendData6[] = {'$', 'A', 'F', '0', '0', '0', 'x'};   //
    char SendData7[] = {'$', 'A', 'G', '0', '0', '0', 'x'};   //
    char SendData8[] = {'$', 'A', 'H', '0', '0', '0', 'x'};   //
    char SendData9[] = {'$', 'A', 'I', '0', '0', '0', 'x'};   //
    char SendData10[] = {'$', 'A', 'J', '0', '0', '0', 'x'};   //
    char SendData11[] = {'$', 'A', 'K', '0', '0', '0', 'x'};   //
    char SendData12[] = {'$', 'A', 'L', '0', '0', '0', 'x'};   //

    //    private SocketUtil socketUtil;
    private String ipaddr = "10.10.100.254";
    private int portnum = 8899;
    private Vibrator vibrator;
    private ExecutorService mThreadPool;

    private Handler sendHandler;
    private OutputStream out;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏显示：或者xml文件节点下android:theme="@android:style/Theme.NoTitleBar.Fullscreen"
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_bed);
        ButterKnife.inject(this);

        btnTurnleft = (ImageButton) findViewById(R.id.btn_turnleft);
        btnTurnright = (ImageButton) findViewById(R.id.btn_turnright);
        btnLiftfoot = (ImageButton) findViewById(R.id.btn_liftfoot);
        btnStayfoot = (ImageButton) findViewById(R.id.btn_stayfoot);
        btnAutioA = (ImageButton) findViewById(R.id.btn_autioA);
        btnAutioB = (ImageButton) findViewById(R.id.btn_autioB);
        btnSitup = (ImageButton) findViewById(R.id.btn_situp);
        btnLiedown = (ImageButton) findViewById(R.id.btn_liedown);
        btnOpenBedpan = (ImageButton) findViewById(R.id.btn_open_bedpan);
        btnCloseBedpan = (ImageButton) findViewById(R.id.btn_close_bedpan);
        btnReset = (ImageButton) findViewById(R.id.btn_reset);

        btnTurnleft.setOnTouchListener(this);
        btnTurnright.setOnTouchListener(this);
        btnLiftfoot.setOnTouchListener(this);
        btnStayfoot.setOnTouchListener(this);
        btnAutioA.setOnTouchListener(this);
        btnAutioB.setOnTouchListener(this);
        btnSitup.setOnTouchListener(this);
        btnLiedown.setOnTouchListener(this);
        btnOpenBedpan.setOnTouchListener(this);
        btnCloseBedpan.setOnTouchListener(this);
        btnReset.setOnTouchListener(this);

        //点击控件震动效果
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        /**
         * 进入界面，提示连接状态
         */
        int connect = SocketUtil.connectStaus;
        switch (connect) {
            case 0:
                Toast.makeText(BedActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
                break;
            case -1:
                Toast.makeText(BedActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(BedActivity.this, "网络连接成功", Toast.LENGTH_SHORT).show();
                mThreadPool = Executors.newCachedThreadPool();
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (SocketUtil.socket != null && SocketUtil.socket.isConnected()) {

                            try {
                                out = SocketUtil.getSocket().getOutputStream();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Looper.prepare();
                            sendHandler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    super.handleMessage(msg);
                                    switch (msg.what) {
                                        case 0x01:
                                            SocketUtil.SendData(out, (char[]) msg.obj);
                                            break;
                                        case 0x02:

                                            break;
                                        default:
                                            break;
                                    }
                                }
                            };
                            Looper.loop();
                        }

                    }
                });
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (out != null) {
            try {
                out.flush();
//            out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 中断线程池中的任务
         */
        mThreadPool.shutdownNow();

    }

    private Message message;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.btn_turnleft:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vibrator.vibrate(100);
                    if (SocketUtil.connectStaus != 1) {
                        Toast.makeText(BedActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                        return false;
                    }
//                    SocketUtil.SendData(SendData1);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData1;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    SocketUtil.SendData(SendData12);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData12;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }
                }

                break;
            case R.id.btn_turnright:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vibrator.vibrate(100);
                    if (SocketUtil.connectStaus != 1) {
                        Toast.makeText(BedActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                        return false;
                    }
//                    SocketUtil.SendData(SendData2);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData2;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    SocketUtil.SendData(SendData12);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData12;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }

                }

                break;
            case R.id.btn_situp:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vibrator.vibrate(100);
                    if (SocketUtil.connectStaus != 1) {
                        Toast.makeText(BedActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                        return false;
                    }
//                    SocketUtil.SendData(SendData3);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData3;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    SocketUtil.SendData(SendData12);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData12;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }
                }
                break;
            case R.id.btn_liedown:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vibrator.vibrate(100);
                    if (SocketUtil.connectStaus != 1) {
                        Toast.makeText(BedActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                        return false;
                    }
//                    SocketUtil.SendData(SendData4);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData4;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    SocketUtil.SendData(SendData12);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData12;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }
                }
                break;
            case R.id.btn_liftfoot:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vibrator.vibrate(100);
                    if (SocketUtil.connectStaus != 1) {
                        Toast.makeText(BedActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                        return false;
                    }
//                    SocketUtil.SendData(SendData5);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData5;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    SocketUtil.SendData(SendData12);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData12;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }
                }
                break;
            case R.id.btn_stayfoot:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vibrator.vibrate(100);
                    if (SocketUtil.connectStaus != 1) {
                        Toast.makeText(BedActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                        return false;
                    }
//                    SocketUtil.SendData(SendData6);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData6;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    SocketUtil.SendData(SendData12);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData12;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }
                }
                break;
            case R.id.btn_open_bedpan:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vibrator.vibrate(100);
                    if (SocketUtil.connectStaus != 1) {
                        Toast.makeText(BedActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                        return false;
                    }
//                    SocketUtil.SendData(SendData7);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData7;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    SocketUtil.SendData(SendData12);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData12;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }
                }
                break;
            case R.id.btn_close_bedpan:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vibrator.vibrate(100);
                    if (SocketUtil.connectStaus != 1) {
                        Toast.makeText(BedActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                        return false;
                    }
//                    SocketUtil.SendData(SendData8);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData8;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    SocketUtil.SendData(SendData12);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData12;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }
                }
                break;
            case R.id.btn_autioA:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vibrator.vibrate(100);
                    if (SocketUtil.connectStaus != 1) {
                        Toast.makeText(BedActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                        return false;
                    }
//                    SocketUtil.SendData(SendData9);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData9;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    SocketUtil.SendData(SendData12);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData12;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }
                }
                break;
            case R.id.btn_autioB:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vibrator.vibrate(100);
                    if (SocketUtil.connectStaus != 1) {
                        Toast.makeText(BedActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                        return false;
                    }
//                    SocketUtil.SendData(SendData10);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData10;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    SocketUtil.SendData(SendData12);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData12;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }
                }
                break;
            case R.id.btn_reset:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vibrator.vibrate(100);
                    if (SocketUtil.connectStaus != 1) {
                        Toast.makeText(BedActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                        return false;
                    }
//                    SocketUtil.SendData(SendData11);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData11;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    SocketUtil.SendData(SendData12);
                    message = new Message();
                    message.what = 0x01;
                    message.obj = SendData12;
                    if (sendHandler != null) {
                        sendHandler.sendMessage(message);
                    }
                }
                break;

            default:
                break;
        }
        return false;
    }

}
