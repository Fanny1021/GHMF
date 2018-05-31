package com.fanny.ghmf.bean;

/**
 * Created by Fanny on 18/4/2.
 */

public class TempValueInfo {
    private String time;
    private String value;

    public TempValueInfo(String time,String value){
        this.time=time;
        this.value=value;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public String getValue() {
        return value;
    }
}
