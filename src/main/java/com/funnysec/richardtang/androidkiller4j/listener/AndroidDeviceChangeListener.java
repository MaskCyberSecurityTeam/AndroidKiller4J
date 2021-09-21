package com.funnysec.richardtang.androidkiller4j.listener;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.funnysec.richardtang.androidkiller4j.view.ToolkitView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Device设备监听，当有新设备连接、断开、切换时会触发该类中对应的函数。
 *
 * @author RichardTang
 */
@Component
public class AndroidDeviceChangeListener implements AndroidDebugBridge.IDeviceChangeListener {

    @Autowired
    private ToolkitView toolkitView;

    /**
     * 有新设备被ADB监听到时触发，注意这里并不是用户通过选项卡选中设备时触发。
     *
     * @param device 设备对象
     */
    @Override
    public void deviceConnected(IDevice device) {
        toolkitView.getDeviceChoiceBox().getItems().add(device);
        System.out.println("监听到新设备上线: " + device.getName());
    }

    /**
     * 设备断开连接时触发，当在adb devices中看不到该设备时就代表该设备断开了连接。
     *
     * @param device 设备对象
     */
    @Override
    public void deviceDisconnected(IDevice device) {
        toolkitView.getDeviceChoiceBox().getItems().remove(device);
        System.out.println("监听到设备断开连接: " + device.getName());
    }

    @Override
    public void deviceChanged(IDevice device, int changeMask) {

    }
}