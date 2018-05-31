package com.fanny.ghmf.bean;

/**
 * Created by Fanny on 18/4/3.
 */

public  class BloodSugarValueInfo {

    private String time;

    private int glu;
    private int ua;
    private int chol;

    public void setTime(String time) {
        this.time = time;
    }

    public void setGlu(int glu) {
        this.glu = glu;
    }

    public void setUa(int ua) {
        this.ua = ua;
    }

    public void setChol(int chol) {
        this.chol = chol;
    }

    public String getTime() {
        return time;
    }

    public int getGlu() {
        return glu;
    }

    public int getUa() {
        return ua;
    }

    public int getChol() {
        return chol;
    }

    public BloodSugarValueInfo(String time, int glu, int ua, int chol) {
        this.time = time;
        this.glu = glu;
        this.ua = ua;
        this.chol = chol;
    }

}
