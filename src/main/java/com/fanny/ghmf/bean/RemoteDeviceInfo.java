package com.fanny.ghmf.bean;

import java.util.ArrayList;

/**
 * Created by Fanny on 18/4/4.
 */

public class RemoteDeviceInfo {

    /**
     * 服务器返回data的json数据：
     * "data":
     * [
     * {
     * "deviceName":"gh-**-**",
     * "DeviceCategory":"WashHead",
     * "DeviceId:"VSTA-368340-HWBMZ",
     * "Latidute":"39.93923",
     * "Longitude":"116.397428"
     * },
     * <p>
     * {
     * "deviceName":"gh-**-**",
     * "DeviceCategory":"WashHead",
     * "DeviceId:"VSTA-368340-HWBMZ",
     * "Latidute":"39.93923",
     * "Longitude":"116.397428"
     * },
     * <p>
     * {
     * "deviceName":"gh-**-**",
     * "DeviceCategory":"WashHead",
     * "DeviceId:"VSTA-368340-HWBMZ",
     * "Latidute":"39.93923",
     * "Longitude":"116.397428"
     * },
     * ......
     * ]
     */

    public ArrayList<DeviceMessageInfo> getList() {
        return list;
    }

    public void setList(ArrayList<DeviceMessageInfo> list) {
        this.list = list;
    }

    private ArrayList<DeviceMessageInfo> list;

   public class DeviceMessageInfo {


        private String DeviceName;
        private String DeviceCategory;
        private String DeviceId;
        private double Latidute;
        private double Longitude;

        public void setDeviceName(String deviceName) {
            DeviceName = deviceName;
        }

        public void setDeviceCategory(String deviceCategory) {
            DeviceCategory = deviceCategory;
        }

        public void setDeviceId(String deviceId) {
            DeviceId = deviceId;
        }

        public void setLatidute(double latidute) {
            Latidute = latidute;
        }

        public void setLongitude(double longitude) {
            Longitude = longitude;
        }

        public String getDeviceName() {
            return DeviceName;
        }

        public String getDeviceCategory() {
            return DeviceCategory;
        }

        public String getDeviceId() {
            return DeviceId;
        }

        public double getLatidute() {
            return Latidute;
        }

        public double getLongitude() {
            return Longitude;
        }

        public DeviceMessageInfo(String deviceName, String deviceCategory, String deviceId, double latidute, double longitude) {
            DeviceName = deviceName;
            DeviceCategory = deviceCategory;
            DeviceId = deviceId;
            Latidute = latidute;
            Longitude = longitude;
        }
    }
}
