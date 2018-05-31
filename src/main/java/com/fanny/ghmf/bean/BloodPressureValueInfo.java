package com.fanny.ghmf.bean;

/**
 * Created by Fanny on 18/4/3.
 */

public  class BloodPressureValueInfo {

    private String time;

    private int sys;
    private int dia;
    private int plus;
    private int map;

    public void setTime(String time) {
        this.time = time;
    }

    public void setSys(int sys) {
        this.sys = sys;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public void setPlus(int plus) {
        this.plus = plus;
    }

    public void setMap(int map) {
        this.map = map;
    }

    public String getTime() {
        return time;
    }

    public int getSys() {
        return sys;
    }

    public int getDia() {
        return dia;
    }

    public int getPlus() {
        return plus;
    }

    public int getMap() {
        return map;
    }

    public BloodPressureValueInfo(String time, int sys, int dia, int plus, int map) {
        this.time = time;
        this.sys = sys;
        this.dia = dia;
        this.plus = plus;
        this.map = map;
    }

}
