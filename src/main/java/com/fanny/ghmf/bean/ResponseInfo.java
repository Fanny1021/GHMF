package com.fanny.ghmf.bean;

/**
 * Created by Fanny on 18/4/2.
 */

public class ResponseInfo {

    /**
     * 封装网络请求结果
     * 服务器端返回json
     * ｛
     *   "code":"0",
     *   "data":"{}"
     *  ｝
     */
    private String code;
    private String data;

    public String getCode() {
        return code;
    }

    public String getData() {
        return data;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setData(String data) {
        this.data = data;
    }
}
