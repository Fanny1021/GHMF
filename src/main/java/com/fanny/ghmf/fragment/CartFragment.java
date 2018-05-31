package com.fanny.ghmf.fragment;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.fanny.ghmf.R;
import com.fanny.ghmf.bean.CategoryData;
import com.fanny.ghmf.util.ApiUrls;
import com.fanny.ghmf.util.HttpUtil;
import com.fanny.ghmf.util.UiUtils;
import com.google.gson.Gson;
import com.sina.weibo.sdk.ApiUtils;
import com.sina.weibo.sdk.utils.UIUtils;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fanny on 17/10/11.
 */

public class CartFragment extends NetBaseFragment<CategoryData> implements View.OnClickListener{

    private WebView mWebView;
    private EditText et_user;
    private EditText et_psw;
    private Button btn_login;

//    private RecyclerView recyclerView;

    //创建网络状态控件StateLayout与列表控件
    @Override
    protected int getLayoutId() {
//        return R.layout.fragment_cart;
        return R.layout.fragment_webserve_test;
    }

    @Override
    protected void initView(View normalView) {
//        recyclerView= (RecyclerView) normalView.findViewById(R.id.cart_recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        et_user = (EditText) normalView.findViewById(R.id.edt_username);
        et_psw = (EditText) normalView.findViewById(R.id.edt_pwd);
        btn_login = (Button) normalView.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getActivity().getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.blue));
                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

//    @Override
//    protected String loadUrl(int pageIn){
//        return ApiUrls.CATEGORY;
//    }
//
//    @Override
//    protected CategoryData processJson(String json){
//        String newJson="{list:"+json+"}";
//        CategoryData  data=new Gson().fromJson(newJson,CategoryData.class);
//        return data;
//    }
//
//    @Override
//    protected void showData(CategoryData data) {
//        List<Object> newData=new ArrayList<>();
//        for(CategoryData.CategoryInfo info: data.list){
//            newData.add(info.title);
//            newData.addAll(info.infos);
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                final String username=et_user.getText().toString();
                final String psw=et_psw.getText().toString();
                if(TextUtils.isEmpty(username)) {
                    UiUtils.showToast("用户名不能为空");
                }else if (TextUtils.isEmpty(psw)){
                    UiUtils.showToast("密码不能为空");
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //2.2 发送get请求
                            String url = ApiUrls.TESTJSON+ "?username=" + username+ "?password=" + psw;
                            HttpUtil.get(url, listener);
                        }
                    }).start();
                }
                break;
        }
    }
    //2.3.在对应的空的方法里面处理服务端返回数据
    private OnResponseListener<String> listener = new OnResponseListener<String>() {
        //请求开始的加载状态
        @Override
        public void onStart(int what) {

        }
        //请求失败的错误状态
        @Override
        public void onFailed(int what, Response<String> response) {

        }
        //请求结束的状态
        @Override
        public void onFinish(int what) {

        }
        //成功
        @Override
        public void onSucceed(int what, Response<String> response) {
            //2.4获取响应里面的json
            String json=response.get();
            //2.5解析json
          UiUtils.showToast("response"+json);
        }

    };

//    private class MultiTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
//
//        private List<Object> mData;
//        public MultiTypeAdapter(List<Object> newData){
//            mData=newData;
//        }
//
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            int layoutId=0;
//            if(viewType == ITEM_TITLE){
//                layoutId=R.layout.item_category_title;
//            }else if(viewType == ITEM_GRID){
//                layoutId=R.layout.item_category_grid;
//            }
//            View view= LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
//            RecyclerView.ViewHolder hd=new myViewHolder(view);
//            return hd;
//        }
//
//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            int itemType=getItemViewType(position);
//            Object obj=mData.get(position);
//            if(itemType == ITEM_TITLE){
//                if(obj instanceof String ){
////                    holder.setT
//                }
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return mData.size();
//        }
//        private static final int ITEM_TITLE=0;
//        private static final int ITEM_GRID=1;
//
//        @Override
//        public int getItemViewType(int position) {
//            Object item=mData.get(position);
//            if(item instanceof String){
//                return ITEM_TITLE;
//            }else {
//                return ITEM_GRID;
//            }
//        }
//
//        private class myViewHolder extends RecyclerView.ViewHolder {
//
//            TextView tv;
//            public myViewHolder(View itemView) {
//                super(itemView);
//            }
//
//            public void setTv(TextView tv) {
//                this.tv = tv;
//            }
//        }
//    }


//    private void initWebviewData() {
//        mWebView.loadUrl("file:///android_asset/h51/login.html");
////        mWebView.loadUrl(address);
//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
////                view.loadUrl(request);
//                return true;
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                /**
//                 * 设定一个加载loading界面
//                 */
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                /**
//                 * loading加载后
//                 */
//            }
//
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//
//                //步骤1：写一个html文件（error_handle.html），用于出错时展示给用户看的提示页面
//                // 步骤2：将该html文件放置到代码根目录的assets文件夹下
//                // 步骤3：复写WebViewClient的onRecievedError方法
//                // 该方法传回了错误码，根据错误类型可以进行不同的错误分类处理
////                view.loadUrl("file:///android_asset/fanny07.html");
//            }
//
//            //webView默认是不处理https请求的，页面显示空白，需要进行如下设置：
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed();
//                super.onReceivedSslError(view, handler, error);
//            }
//
//        });
//    }
//
//    private void initWebviewConfig() {
//        WebSettings webSettings = mWebView.getSettings();
//        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
//        webSettings.setJavaScriptEnabled(true);
//
//        //设置自适应屏幕，两者合用
//        webSettings.setUseWideViewPort(true);//将图片调整到适合webview的大小
//        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
//
//        //缩放操作
//        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
//        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
//        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
//
//        //其他细节操作
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
//        //缓存模式如下：
//        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
//        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
//        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
//        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
//        webSettings.setAllowFileAccess(true); //设置可以访问文件
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
//        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
//        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
//
//        //离线加载代替缓存方式：
////        if (NetStatusUtil.isConnected(getApplicationContext())) {
////            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
////        } else {
////            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
////        }
////
////        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
////        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
////        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
////
////        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
////        webSettings.setAppCachePath(cacheDirPath); //设置  Application Caches 缓存目录
//
//    }
//
//    @Override
//    public void onDestroy() {
////        if(mWebView!=null){
////            mWebView.loadDataWithBaseURL(null,"","text/html","utf-8",null);
////            mWebView.clearHistory();
////
////            ((ViewGroup)mWebView.getParent()).removeView(mWebView);
////            mWebView.destroy();
////            mWebView=null;
////        }
//        super.onDestroy();
//    }
}
