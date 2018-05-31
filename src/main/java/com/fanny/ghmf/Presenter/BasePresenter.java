package com.fanny.ghmf.Presenter;

import com.fanny.ghmf.bean.ResponseInfo;
import com.fanny.ghmf.net.ResponseInfoApi;
import com.fanny.ghmf.util.ConstantUtil;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Fanny on 18/3/30.
 */

public abstract class BasePresenter {

    protected ResponseInfoApi responseInfoApi;
    private HashMap<String,String> errorMap;

    public BasePresenter(){
        errorMap=new HashMap<>();
        errorMap.put("1","此页面数据没有更新");
        errorMap.put("2","服务器忙");
        errorMap.put("3","请求参数异常");

        //创建Retrofit对象
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ConstantUtil.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //指定Retrofit如何发送具体的请求.具体体现在ResponseInfoApi中
        //请求方式  get post
        //请求路径  url
        //请求参数
        //请求结果
        responseInfoApi=retrofit.create(ResponseInfoApi.class);
    }

    class CallBackAdapter implements Callback<ResponseInfo>{

        @Override
        public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
            ResponseInfo body=response.body();
            /**
             * 返回code值为"0"，代表请求数据成功
             */
            if(body.getCode().equals("0")){
                /**
                 * data值为具体的返回数据
                 */
                String json=body.getData();
                paraseJson(json);
            }else {
                //本次请求有异常,具体的异常类型获取出来
                String errorMessage=errorMap.get(body.getCode());
                onFailure(call,new RuntimeException(errorMessage));
            }
        }

        @Override
        public void onFailure(Call<ResponseInfo> call, Throwable t) {
            if(t instanceof RuntimeException){
                //onFailure方法自己调用
                String message = t.getMessage();
                //自定义一个如何显示异常方法
                showErrorMessage(message);
            }
            //retrofit框架调用
            showErrorMessage("服务器忙,请稍后重试");
        }
    }

    //因为json串对于每一个页面的请求而言,结果都是有差异的,所以无法做具体的解析,抽象
    protected abstract void paraseJson(String json);

    protected abstract void showErrorMessage(String message);

}
