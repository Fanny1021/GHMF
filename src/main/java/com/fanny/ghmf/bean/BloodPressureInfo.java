package com.fanny.ghmf.bean;

import java.util.ArrayList;

/**
 * Created by Fanny on 18/4/2.
 */

public class BloodPressureInfo {
    /**
     * 服务器返回data的json数据：
     * "data":｛
     * "category":"BloodPressure"
     * "list":[
     * {
     * "time":"2017-03-01 12:30",
     * "sys":"81",
     * "dia":"79",
     * "plus":"80",
     * "map":"80"
     * },
     * {
     * "time":"2017-03-02 12:30",
     * "sys":"81",
     * "dia":"79",
     * "plus":"80",
     * "map":"80"
     * },
     * {
     * "time":"2017-03-03 12:30",
     * "sys":"81",
     * "dia":"79",
     * "plus":"80",
     * "map":"80"
     * },
     * ......
     * ]
     * ｝
     */

    private String category;
    private ArrayList<BloodPressureValueInfo> list;

    public void setCategory(String category) {
        this.category = category;
    }

    public void setList(ArrayList<BloodPressureValueInfo> list) {
        this.list = list;
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<BloodPressureValueInfo> getList() {
        return list;
    }

}
