package com.richardtang.androidkiller4j.listener;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.richardtang.androidkiller4j.core.ddmlib.AndroidDeviceManager;
import com.richardtang.androidkiller4j.ui.control.IconComboBox;
import com.richardtang.androidkiller4j.util.ControlUtil;
import com.richardtang.androidkiller4j.view.toolkit.ToolkitView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;

/**
 * Device设备监听，当有新设备连接、断开、切换时会触发该类中对应的函数。
 *
 * @author RichardTang
 */
@Component
public class AndroidDeviceChangeListener implements AndroidDebugBridge.IDeviceChangeListener {

    @Autowired
    private ToolkitView toolkitView;

    @Autowired
    private AndroidDeviceManager androidDeviceManager;

    public static final Icon DEVICE_ICON = ControlUtil.getSVGIcon("device.svg", 15, 15);

    /**
     * 有新设备被ADB监听到时触发，注意这里并不是用户通过选项卡选中设备时触发。
     *
     * @param device 设备对象
     */
    @Override
    public void deviceConnected(IDevice device) {
        toolkitView.getDeviceIconComboBox().addItem(
                new IconComboBox.IconComboBoxData(device.getSerialNumber(), device.getName(), DEVICE_ICON, device)
        );
        System.out.println("监听到新设备上线: " + device.getName());
    }

    /**
     * 设备断开连接时触发，当在adb devices中看不到该设备时就代表该设备断开了连接。
     *
     * @param device 设备对象
     */
    @Override
    public void deviceDisconnected(IDevice device) {
        // 当有设备断开，并且断开的设备是选中的那台设备，更新相应的ui。
        IDevice selectDevice = androidDeviceManager.getDevice();
        if (selectDevice != null && device == selectDevice) {
            androidDeviceManager.setDevice(null);
            toolkitView.setDeviceConnectStateUI(true);
        }
        toolkitView.getDeviceIconComboBox().removeItemById(device.getSerialNumber());
        System.out.println("监听到设备断开连接: " + device.getName());
    }

    @Override
    public void deviceChanged(IDevice device, int changeMask) {

    }
}