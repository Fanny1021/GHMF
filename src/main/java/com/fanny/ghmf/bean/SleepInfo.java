package com.fanny.ghmf.bean;

import java.util.ArrayList;

/**
 * Created by Fanny on 18/4/2.
 */

public class SleepInfo {
    /**
     * 服务器返回data的json数据：
     * "data":｛
     * "category":"BloodOxy"
     * "list":[
     * {
     * "time":"2017-03-01 12:30",
     * "sp02":"81",
     * "pr":"79",
     * "pi":"80"
     * },
     * {
     * "time":"2017-03-01 12:30",
     * "sp02":"81",
     * "pr":"79",
     * "pi":"80"
     * },
     * {
     * "time":"2017-03-01 12:30",
     * "sp02":"81",
     * "pr":"79",
     * "pi":"80"
     * },
     * ......
     * ]
     * ｝
     */

    private String category;
    private ArrayList<SleepValueInfo> list;

    public void setCategory(String category) {
        this.category = category;
    }

    public void setList(ArrayList<SleepValueInfo> list) {
        this.list = list;
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<SleepValueInfo> getList() {
        return list;
    }

}
