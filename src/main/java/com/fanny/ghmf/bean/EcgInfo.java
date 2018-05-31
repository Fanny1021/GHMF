package com.fanny.ghmf.bean;

import java.util.ArrayList;

/**
 * Created by Fanny on 18/4/2.
 */

public class EcgInfo {
    /**
     * 服务器返回data的json数据：
     * "data":｛
     * "category":"ecg"
     * "list":[
     * {
     * "time":"2017-03-01 12:30",
     * "value:"87"
     * },
     * {
     * "time":"2017-03-02 12:30",
     * "value:"100"
     * },
     * {
     * "time":"2017-03-03 12:30",
     * "value:"99"
     * },
     * ......
     * ]
     * ｝
     */

    private String category;
    private ArrayList<EcgValueInfo> list;

    public void setCategory(String category) {
        this.category = category;
    }

    public void setList(ArrayList<EcgValueInfo> list) {
        this.list = list;
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<EcgValueInfo> getList() {
        return list;
    }

}
