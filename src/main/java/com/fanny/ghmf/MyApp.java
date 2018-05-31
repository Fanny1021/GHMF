package com.fanny.ghmf;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.fanny.ghmf.dao.DBOpenHelper;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.NoHttp;

import java.util.ArrayList;

/**
 * Created by Fanny on 17/10/13.
 */

public class MyApp extends Application {
    public static Context context=null;
    public static Context getContext(){
        return context;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        // 校验key
        SDKInitializer.initialize(getApplicationContext());
        intNet();
        initDatabase();
    }

    private void intNet() {
        /**
         * 初始化网络框架
         */
        InitializationConfig config=InitializationConfig.newBuilder(context)
                .connectionTimeout(30*1000)
                .build();
        NoHttp.initialize(config);

        /**
         * 初始化图片加载框架
         */
        Fresco.initialize(this);
    }

    private SQLiteDatabase db;
    public static DBOpenHelper dbopenHelper;

    private void initDatabase() {

        dbopenHelper = new DBOpenHelper(this);

        db = dbopenHelper.getWritableDatabase();

        for (int i = 0; i < 50; i++) {
            db.execSQL("insert into userlogin(name,sex,age,idcardno) values(?,?,?,?)",
                    new Object[]{"龚梦帆" + i, "女", "26", "420625199109140066"});
        }

        /**
         *  处理数据表单 每人最多20条测试数据
         */

        ArrayList<String> nameList = new ArrayList<>();
        String sql = "select * from userlogin ";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        while (cursor.moveToNext()) {
            nameList.add(cursor.getString(0));
        }

        if (nameList.size() > 20) {
            for (int i = 20; i < nameList.size() - 1; i++) {
                db.delete("userlogin", "name=?", new String[]{nameList.get(i)});  //保留20条测试人员数据
            }
        }


//        db.delete("hoster","",new String[]{});
//
//        db.execSQL("insert into userlogin(name,sex,age,idcardno) values(?,?,?,?)",
//                new Object[]{"龚梦帆", "女","26", "420625199109140066"});
//
//        for(int i=0;i<18;i++){
//            db.execSQL("insert into userlogin(name,sex,age,idcardno) values(?,?,?,?)",
//                    new Object[]{"龚梦帆"+i, "女","26", "420625199109140066"});
//        }

//
//
//        db.execSQL("insert into hoster(account,psw) values(?,?)",
//                new Object[]{"admin", "123456"});


//        db.delete("xueyaData","name=?",new String[]{"龚梦帆"});


//        db.execSQL("insert into tempData(name,time,temp) values(?,?,?)",
//                new Object[]{"龚梦帆", "2017-10-27  17:40:14", 34.9});

//        db.delete("tempData","name=?",new String[]{"龚梦帆"});

//        db.delete("xueyangData","name=?",new String[]{"龚梦帆"});

//        db.delete("xuetangData","name=?",new String[]{"龚梦帆"});

//        db.execSQL("insert into xindianData(name,time,ecg) values(?,?,?)",
//                new Object[]{"龚梦帆", "2015-10-22  14:13:14", 66});

//        db.execSQL("insert into xueyaData(name,time,sys,dia,plus,map) values(?,?,?,?,?,?)",
//                new Object[]{"龚梦帆", "2017-10-27  14:13:14", 66,77,88,99});

//        db.execSQL("insert into xueyangData(name,time,spo2,pr,pi) values(?,?,?,?,?)",
//                new Object[]{"龚梦帆", "2015-10-22  14:13:14", 66,77,88});

//        db.execSQL("insert into xuetangData(name,time,glu,ua,chol) values(?,?,?,?,?)",
//                new Object[]{"龚梦帆", "2015-10-22  14:13:14", 66,77,88});

        db.close();

    }
}
