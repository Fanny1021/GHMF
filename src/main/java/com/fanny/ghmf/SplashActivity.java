package com.fanny.ghmf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.fanny.ghmf.util.SharePrefrenceUtil;

public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGHT=3000;
    private String historyAccount;
    private String historyPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏显示：或者xml文件节点下android:theme="@android:style/Theme.NoTitleBar.Fullscreen"
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


        /**
         * 检查是否登录
         */
        historyAccount = (String) SharePrefrenceUtil.getData(getBaseContext(),SharePrefrenceUtil.USER_NAME,"");
        historyPsw = (String) SharePrefrenceUtil.getData(getBaseContext(),SharePrefrenceUtil.USER_PSW,"");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(historyAccount ==null || historyAccount.equals("")){
                    Intent mainIntent=new Intent(SplashActivity.this,UserLoginActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }else {

                    /**
                     * 绑定注册用户数据
                     */
                    Bundle bundle=new Bundle();
                    bundle.putString("account",historyAccount);//这里人为将historyAccount写为姓名中文
                    bundle.putString("psw", historyPsw);

                    /**
                     * 携带用户信息，跳转至主界面
                     */
                    Intent mainIntent=new Intent(SplashActivity.this,MainActivity.class);
                    mainIntent.putExtra("data",bundle);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }

            }
        },SPLASH_DISPLAY_LENGHT);
    }
}
