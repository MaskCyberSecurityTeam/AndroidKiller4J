package com.richardtang.androidkiller4j.ddmlib.listener;

import cn.hutool.log.StaticLog;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.richardtang.androidkiller4j.Application;
import com.richardtang.androidkiller4j.MainWindow;
import com.richardtang.androidkiller4j.constant.Size;
import com.richardtang.androidkiller4j.ddmlib.AndroidDeviceManager;
import com.richardtang.androidkiller4j.ui.control.IconComboBox;
import com.richardtang.androidkiller4j.util.ControlUtils;

import javax.swing.*;

/**
 * Device设备监听，当有新设备连接、断开、切换时会触发该类中对应的函数。
 *
 * @author RichardTang
 */
public class AndroidDeviceChangeListener implements AndroidDebugBridge.IDeviceChangeListener {

    // 设备图标
    public final Icon DEVICE_ICON = ControlUtils.getSVGIcon("device.svg", Size.MEDIUM);

    /**
     * 电脑使用数据线连接了新的设备时触发
     *
     * @param device 设备对象
     */
    @Override
    public void deviceConnected(IDevice device) {
        MainWindow.toolkitView.getDeviceComboBox().addItem(
                new IconComboBox.IconComboBoxData(device.getSerialNumber(), device.getName(), DEVICE_ICON, device)
        );
        StaticLog.info("新设备上线: " + device.getName());
    }

    /**
     * 设备断开连接时触发，当在adb devices中看不到该设备时就代表该设备断开了连接。
     *
     * @param device 设备对象
     */
    @Override
    public void deviceDisconnected(IDevice device) {
        IDevice selectDevice = AndroidDeviceManager.getInstance().getDevice();
        if (selectDevice != null && device == selectDevice) {
            AndroidDeviceManager.getInstance().setDevice(null);
            MainWindow.toolkitView.setDeviceConnectStateUI(true);
        }
        MainWindow.toolkitView.getDeviceComboBox().removeItemById(device.getSerialNumber());
        StaticLog.info("设备断开连接: " + device.getName());
    }

    @Override
    public void deviceChanged(IDevice device, int changeMask) {

    }
}