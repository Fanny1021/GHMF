package com.fanny.ghmf.bean;

import java.util.ArrayList;

/**
 * Created by Fanny on 18/4/2.
 */

public class TempInfo {
    /**
     * 服务器返回data的json数据：
     * "data":｛
     * "category":"temp"
     * "list":[
     * {
     * "time":"2017-03-01 12:30",
     * "value:"31.0"
     * },
     * {
     * "time":"2017-03-02 12:30",
     * "value:"31.0"
     * },
     * {
     * "time":"2017-03-03 12:30",
     * "value:"31.0"
     * },
     * ......
     * ]
     * ｝
     */

    private String category;
    private ArrayList<TempValueInfo> list;

    public void setCategory(String category) {
        this.category = category;
    }

    public void setList(ArrayList<TempValueInfo> list) {
        this.list = list;
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<TempValueInfo> getList() {
        return list;
    }

}
