package com.fanny.ghmf.bean;

/**
 * Created by Fanny on 18/4/3.
 */

public  class BloodOxyValueInfo {

    private String time;

    private int sp02;
    private int pr;
    private int pi;

    public void setTime(String time) {
        this.time = time;
    }

    public void setSp02(int sp02) {
        this.sp02 = sp02;
    }

    public void setPr(int pr) {
        this.pr = pr;
    }

    public void setPi(int pi) {
        this.pi = pi;
    }

    public String getTime() {
        return time;
    }

    public int getSp02() {
        return sp02;
    }

    public int getPr() {
        return pr;
    }

    public int getPi() {
        return pi;
    }

    public BloodOxyValueInfo(String time, int sp02, int pr, int pi) {
        this.time = time;
        this.sp02 = sp02;
        this.pr = pr;
        this.pi = pi;
    }
}
