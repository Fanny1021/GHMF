package com.fanny.ghmf.util;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.StringRequest;

/**
 * Created by Fanny on 17/12/13.
 */

public class HttpUtil {
    private static RequestQueue sRequestQueue;

    /**
     * 以nohttp作为网络请求的实现，发送get请求
     */
    public static void get(String url, OnResponseListener<String> listener) {
        //创建请求并进行发送， 处理服务端返回的数据
        //(Request客户端发送给服务端的参数)
        //Request有一个子类StringReqeust,根据返回数据为string(json,xml)
        //创建请求 请求地址 url,请求方法 RequestMethod
        StringRequest request = new StringRequest(url, RequestMethod.GET);
        //创建请求队列(类似handler 的消息队列 )
        //RequestQueue:一个基于Queue编写的请求队列， 先进先出 先请求的数据先执行
        //监听添加到队列里面的请求， 只要添加到队列里面的请求就会运行子线程（ 发送）
        if (sRequestQueue == null) {
            sRequestQueue = NoHttp.newRequestQueue();
        }
        //设置缓存模式， 要求在离线情况下也能正常使用软件
        request.setCacheMode(CacheMode.NONE_CACHE_REQUEST_NETWORK);
        //参数a. what - 类似Handler的Message使用what 共用一个handler前提下 判断不同的处理情况
        //b.request - 网络请求对象。 大多数的项目用的是StringRequest
        // c.responseListener - 监听器|回调对象(作用是处理返回结果， 提供的一些空的方法里面编写处理代码， 而且这些方法的执行是有条件)
        //创建Response(服务端返回参数封装 200 流 )处理对象
        sRequestQueue.add(0, request, listener);
    }
}
