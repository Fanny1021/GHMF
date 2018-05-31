package com.fanny.ghmf.bean;

/**
 * Created by Fanny on 18/4/2.
 */

public class BedMotionInfo {

    /**
     * 服务器返回data的json数据：
     * "data":｛
     *   "bedsuccess":"true"
     *   ｝
     */

    private boolean bedsuccess;

    public void setBedsuccess(boolean bedsuccess) {
        this.bedsuccess = bedsuccess;
    }

    public boolean getBedsuccess() {
        return bedsuccess;
    }
}
