package com.fanny.ghmf;

/**
 * Created by Fanny on 17/10/11.
 */

public class MessageEvent {
    String DeviceName;
    String DeviceId;

    public MessageEvent(String deviceName, String deviceId) {
        DeviceName = deviceName;
        DeviceId = deviceId;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public String getDeviceId() {
        return DeviceId;
    }

}
