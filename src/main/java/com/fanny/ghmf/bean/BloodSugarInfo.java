package com.fanny.ghmf.bean;

import java.util.ArrayList;

/**
 * Created by Fanny on 18/4/2.
 */

public class BloodSugarInfo {
    /**
     * 服务器返回data的json数据：
     * "data":｛
     * "category":"BloodSugar"
     * "list":[
     * {
     * "time":"2017-03-01 12:30",
     * "glu":"81",
     * "ua":"79",
     * "chol":"80"
     * },
     * {
     * "time":"2017-03-01 12:30",
     * "glu":"81",
     * "ua":"79",
     * "chol":"80"
     * },
     *  * {
     * "time":"2017-03-01 12:30",
     * "glu":"81",
     * "ua":"79",
     * "chol":"80"
     * },
     * ......
     * ]
     * ｝
     */

    private String category;
    private ArrayList<BloodSugarValueInfo> list;

    public void setCategory(String category) {
        this.category = category;
    }

    public void setList(ArrayList<BloodSugarValueInfo> list) {
        this.list = list;
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<BloodSugarValueInfo> getList() {
        return list;
    }

}
