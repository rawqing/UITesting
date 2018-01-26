package com.yq.milk.constant;

import android.os.Build;

/**
 * Created by king on 16/10/28.
 */

public class DeviceDesc {
    private static DeviceDesc deviceDesc;

    private String model;
    private String device;

    private String product;
    private String serial;
    private String release;
    private int sdk_version;
    private String cpu_abi;

    private DeviceDesc(){
        model = Build.MODEL;
        device = Build.DEVICE;
        cpu_abi = Build.CPU_ABI;
        product = Build.PRODUCT;
        serial = Build.SERIAL;
        release = Build.VERSION.RELEASE;
        sdk_version = Build.VERSION.SDK_INT;
    }
    public static DeviceDesc getDeviceDesc(){
        if(deviceDesc == null) deviceDesc = new DeviceDesc();
        return deviceDesc;
    }

    public String getModel() {
        return model;
    }

    public String getDevice() {
        return device;
    }

    public String getProduct() {
        return product;
    }

    public String getSerial() {
        return serial;
    }

    public String getRelease() {
        return release;
    }

    public int getSdk_version() {
        return sdk_version;
    }

    public String getCpu_abi() {
        return cpu_abi;
    }
}
