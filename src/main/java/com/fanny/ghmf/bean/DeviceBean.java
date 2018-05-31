package com.fanny.ghmf.bean;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;

/**
 * Created by Fanny on 17/10/10.
 */

public class DeviceBean {
    public DeviceBean(String device_name, Bitmap device_pic, int device_num) {
        this.device_name = device_name;
        this.device_pic = device_pic;
        this.device_num=device_num;
    }

    public String device_name;
    public Bitmap device_pic;
    public int device_num;

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public void setDevice_pic(Bitmap device_pic) {
        this.device_pic = device_pic;
    }

    public String getDevice_name() {
        return device_name;
    }

    public Bitmap getDevice_pic() {
        return device_pic;
    }

}
