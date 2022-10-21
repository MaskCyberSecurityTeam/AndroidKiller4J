package com.richardtang.androidkiller4j.validator;

import com.android.ddmlib.IDevice;
import com.richardtang.androidkiller4j.Application;
import com.richardtang.androidkiller4j.ddmlib.AndroidDeviceManager;
import com.richardtang.androidkiller4j.util.ControlUtils;

/**
 * 校验当前用户是否选择需要操作的设备
 *
 * @author RichardTang
 */
public class DeviceOnlineValidator {

    public static boolean verify() {
        IDevice device = AndroidDeviceManager.getInstance().getDevice();
        // 判断是否连接了相应ADB设备
        if (device != null && device.isOnline()) {
            return true;
        } else {
            ControlUtils.showMsgDialog("提示", "请先连接设备！");
            return false;
        }
    }
}